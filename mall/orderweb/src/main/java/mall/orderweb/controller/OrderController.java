package mall.orderweb.controller;

import bean.Cart;
import bean.MemberReceiveAddress;
import bean.Order;
import bean.OrderItem;
import com.alibaba.dubbo.config.annotation.Reference;
import mall.annotations.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import service.CartService;
import service.OrderService;
import service.SkuService;
import service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {
    @Reference
    UserService userService;
    @Reference
    CartService cartService;
    @Reference
    OrderService orderService;
    @Reference
    SkuService skuService;
    @RequestMapping("submitOrder")
    @LoginRequired(loginSuccess = true)
    public ModelAndView submitOrder(String receiveAddressId, String tradeCode, BigDecimal totalAmount, HttpServletResponse response, HttpServletRequest request, ModelMap modelMap){
        String memberId= (String) request.getAttribute("memberId");
        String nickname= (String) request.getAttribute("nickname");
        String success=orderService.checkTradeCode(memberId,tradeCode);
        if(success.equals("success")){
            //生成订单
            // 订单对象
            Order order = new Order();
            order.setAutoConfirmDay(7);
            order.setCreateTime(new Date());
            order.setDiscountAmount(null);
            //omsOrder.setFreightAmount(); 运费，支付后，在生成物流信息时
            order.setMemberId(memberId);
            order.setMemberUsername(nickname);
            order.setNote("快点发货");
            String outTradeNo = "gmall";
            outTradeNo = outTradeNo + System.currentTimeMillis();// 将毫秒时间戳拼接到外部订单号
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
            outTradeNo = outTradeNo + sdf.format(new Date());// 将时间字符串拼接到外部订单号

            order.setOrderSn(outTradeNo);//外部订单号
            order.setPayAmount(totalAmount);
            order.setOrderType(1);
            MemberReceiveAddress memberReceiveAddress = userService.getReceiveAddressById(receiveAddressId);
            order.setReceiverCity(memberReceiveAddress.getCity());
            order.setReceiverDetailAddress(memberReceiveAddress.getDetailAddress());
            order.setReceiverName(memberReceiveAddress.getName());
            order.setReceiverPhone(memberReceiveAddress.getPhoneNumber());
            order.setReceiverPostCode(memberReceiveAddress.getPostCode());
            order.setReceiverProvince(memberReceiveAddress.getProvince());
            order.setReceiverRegion(memberReceiveAddress.getRegion());
            // 当前日期加一天，一天后配送
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE,1);
            Date time = c.getTime();
            order.setReceiveTime(time);
            order.setSourceType(0);
            order.setStatus("0");
            order.setOrderType(0);
            order.setTotalAmount(totalAmount);
            //获取商品列表和总价格
            List<OrderItem> orderItemList=new ArrayList<>();
            List<Cart> cartList=cartService.cartList(memberId);
            for (Cart cart:cartList){
                OrderItem orderItem=new OrderItem();
                // 检价
                boolean b = skuService.checkPrice(cart.getProductSkuId(),cart.getPrice());
                if (b == false) {
                    ModelAndView mv=new ModelAndView("tradeFail");
                    return mv;
                }
                // 验库存,远程调用库存系统
                orderItem.setProductPic(cart.getProductPic());
                orderItem.setProductName(cart.getProductName());
                orderItem.setOrderSn(outTradeNo);// 外部订单号，用来和其他系统进行交互，防止重复
                orderItem.setProductCategoryId(cart.getProductCategoryId());
                orderItem.setProductPrice(cart.getPrice());
                orderItem.setRealAmount(cart.getTotalPrice());
                orderItem.setProductQuantity(cart.getQuantity());
                orderItem.setProductSkuCode("111111111111");
                orderItem.setProductSkuId(cart.getProductSkuId());
                orderItem.setProductId(cart.getProductId());
                orderItem.setProductSn("仓库对应的商品编号");// 在仓库中的skuId
                orderItemList.add(orderItem);
            }
            order.setOrderItems(orderItemList);
            //订单写入数据库
            //删除购物车对应商品
            orderService.saveOrder(order);
            //重定向到支付系统
            ModelAndView mv = new ModelAndView("redirect:http://localhost:8011/index");
            mv.addObject("outTradeNo",outTradeNo);
            mv.addObject("totalAmount",totalAmount);
            return mv;
        }else {
            //生成订单失败
            ModelAndView mv=new ModelAndView("tradeFail");
            return mv;
        }
    }
    @RequestMapping("toTrade")
    @LoginRequired(loginSuccess = true)
    public String toTrade(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap){
        String memberId= (String) request.getAttribute("memberId");
        String nickName= (String) request.getAttribute("nickName");
        //查询收货地址
        List<MemberReceiveAddress> memberReceiveAddressList=userService.getReceiveAddress(memberId);
        modelMap.put("userAddressList",memberReceiveAddressList);
        //将购物车转换为清单集合
        List<Cart> cartList=cartService.cartList(memberId);
        List<OrderItem> orderItemList=new ArrayList<>();
        for(Cart cart:cartList){
            if(cart.getIsChecked().equals("1")){
                OrderItem orderItem=new OrderItem();
                orderItem.setProductName(cart.getProductName());
                orderItem.setProductPic(cart.getProductPic());
                orderItemList.add(orderItem);
            }
        }
        modelMap.put("omsOrderItems",orderItemList);
        //总金额
        BigDecimal totalAmount=getTotalAmount(cartList);
        modelMap.put("totalAmount",totalAmount);
        //生成交易码，提交订单时校验
        String tradeCode=orderService.genTradeCode(memberId);
        modelMap.put("tradeCode",tradeCode);
        return "trade";
    }

    private BigDecimal getTotalAmount(List<Cart> cartList) {
        BigDecimal totalAmount=new BigDecimal("0");
        for (Cart cart:cartList){
            if(cart.getIsChecked().equals("1")){
                totalAmount=totalAmount.add(cart.getTotalPrice());
            }
        }
        return totalAmount;
    }

}
