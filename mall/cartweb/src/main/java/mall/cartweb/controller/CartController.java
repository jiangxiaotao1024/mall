package mall.cartweb.controller;

import bean.Cart;
import bean.SkuInfo;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import mall.annotations.LoginRequired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import service.CartService;
import mall.util.CookieUtil;
import service.SkuService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {
  @Reference
  CartService cartService;
  @Reference
  SkuService skuService;
  //展现购物车列表
  @RequestMapping("cartList")
  @LoginRequired(loginSuccess = false)
   public String cartList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
    List<Cart> cartList=new ArrayList<>();
    String menberId= (String) request.getAttribute("memberId");
    String nickName= (String) request.getAttribute("nickName");
    //判断用户是否登入
    if(StringUtils.isNotBlank(menberId)){
        //登入，读取redis
       cartList=cartService.cartList(menberId);
    }else {
        //未登入，读取cookie数据
       String cartListCookie= CookieUtil.getCookieValue(request,"cartListCookie",true);
       //判断cookie是否存在
       if(StringUtils.isNotBlank(cartListCookie)){
           //存在时读取cookie数据
           cartList= JSON.parseArray(cartListCookie,Cart.class);
       }
    }
    modelMap.put("cartList",cartList);
    BigDecimal totalAmount=new BigDecimal("0");
    for(Cart cart:cartList){
        if(cart.getIsChecked().equals("1")){
            totalAmount=totalAmount.add(cart.getTotalPrice());
        }
    }
    modelMap.put("totalAmount",totalAmount);
    return "cartList";
  }
  @RequestMapping("addToCart")
  @LoginRequired(loginSuccess = false)
  public String addToCart(String skuId,int quantity,HttpServletResponse response,HttpServletRequest request){
      String memberId= (String) request.getAttribute("memberId");
      String nickName= (String) request.getAttribute("nickName");
      List<Cart> cartList=new ArrayList<>();
      SkuInfo skuInfo=skuService.getSkuById(skuId);
      Cart cart=new Cart();
      cart.setCreateDate(new Date());
      cart.setDeleteStatus(0);
      cart.setModifyDate(new Date());
      cart.setPrice(skuInfo.getPrice());
      cart.setProductAttr("");
      cart.setProductBrand("");
      cart.setProductCategoryId(skuInfo.getCatalog3Id());
      cart.setProductId(skuInfo.getSpuId());
      cart.setProductName(skuInfo.getSkuName());
      cart.setProductPic(skuInfo.getSkuDefaultImg());
      cart.setProductSkuCode("11111111111");
      cart.setProductSkuId(skuId);
      cart.setQuantity(new BigDecimal(quantity));
      //判断用户是否登入
      if(StringUtils.isNotBlank(memberId)){
          //登入
          Cart cartFromDb=cartService.ifCartExist(memberId,cart.getProductSkuId());
          //判断商品是否在购物车里
          if(cartFromDb==null){
              //不在 新增
              cart.setMemberId(memberId);
              cartService.addToService(cart);
          }else{
              //在 更新
              cartFromDb.setQuantity(cartFromDb.getQuantity().add(cart.getQuantity()));
              cartService.updateCart(cartFromDb);
          }
          //同步缓存
          cartService.flushCartCache(memberId);
      }else {
          //未登入
          cart.setIsChecked("1");
          String cartListCookie=CookieUtil.getCookieValue(request,"cartListCookie",true);
          //判断cookie是否存在
          if(StringUtils.isNotBlank(cartListCookie)){
              //cookie存在
              cartList= JSON.parseArray(cartListCookie,Cart.class);
              boolean exist=isCartExit(cartList,cart);
              //判断cookie中是否有该商品
              if(exist){
                  //有，更新数量
                  for(Cart cart1:cartList){
                      if(cart1.getProductSkuId().equals(cart.getProductSkuId())){
                          cart1.setQuantity(cart1.getQuantity().add(cart.getQuantity()));
                      }
                      cart1.setTotalPrice(cart1.getPrice().multiply(cart1.getQuantity()));
                  }
              }else{
                  //没有，新增
                  cart.setTotalPrice(cart.getPrice().multiply(cart.getQuantity()));
                  cartList.add(cart);
              }
          }else {
              //cookie不存在
              cart.setTotalPrice(cart.getPrice().multiply(cart.getQuantity()));
              cartList.add(cart);
          }
          //更新cookie
          CookieUtil.setCookie(request,response, "cartListCookie",JSON.toJSONString(cartList),60*60*72,true);
      }
      return "redirect:/success.html";
  }
  //选择商品
  @RequestMapping("checkCart")
  @LoginRequired(loginSuccess = false)
  public String checkCart(String isChecked,String skuId,HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
      String memberId= (String) request.getAttribute("memberId");
      String nickName= (String) request.getAttribute("nickName");
      Cart cart=new Cart();
      cart.setMemberId(memberId);
      cart.setIsChecked(isChecked);
      cart.setProductSkuId(skuId);
      List<Cart> cartList=new ArrayList<>();
      if(StringUtils.isNotBlank(memberId)){
          cartService.checkCart(cart);
          cartList=cartService.cartList(memberId);
      }
      else{
          String cartListCookie=CookieUtil.getCookieValue(request,"cartListCookie",true);
          cartList=JSON.parseArray(cartListCookie,Cart.class);
          for (Cart cart1:cartList){
              if(cart1.getProductSkuId().equals(cart.getProductSkuId())){
                  cart1.setIsChecked(isChecked);
              }
          }
          CookieUtil.setCookie(request,response,"cartListCookie", JSON.toJSONString(cartList),60*60*72,true);
      }
      modelMap.put("cartList",cartList);
      BigDecimal totalAmount=new BigDecimal("0");
      for(Cart cart1:cartList){
          if(cart1.getIsChecked().equals("1")){
              totalAmount=totalAmount.add(cart1.getTotalPrice());
          }
      }
      modelMap.put("totalAmount",totalAmount);
      return "cartListInner";
  }
   //判断购物车中该商品是否存在
    private boolean isCartExit(List<Cart> cartList, Cart cart) {
         boolean exit=false;
         for (Cart cart1:cartList){
             if(cart1.getProductSkuId().equals(cart.getProductSkuId())){
                 exit=true;
             }
         }
         return exit;
    }
}
