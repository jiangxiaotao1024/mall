package mall.cartservice.serviceImpl;

import bean.Cart;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import mall.cartservice.mapper.CartMapper;
import mall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import service.CartService;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    CartMapper cartMapper;
    @Override
    //读取缓存购物车数据
    public List<Cart> cartList(String menberId) {
        List<Cart> cartList=new ArrayList<>();
        Jedis jedis=null;
        try {
            jedis=redisUtil.getJedis();
            List<String> hvals=jedis.hvals("member:"+menberId+":cart");
            for(String hval:hvals){
                Cart cart= JSON.parseObject(hval,Cart.class);
                cartList.add(cart);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
           jedis.close();
        }
        return cartList;
    }

    @Override
    //读取数据库中该购物车，不存在为空
    public Cart ifCartExist(String memberId, String productSkuId) {
        Cart cart=new Cart();
        cart.setMemberId(memberId);
        cart.setProductSkuId(productSkuId);
        cart=cartMapper.selectOne(cart);
        return cart;
    }

    @Override
    //新增购物车
    public void addToService(Cart cart) {
        cartMapper.insertSelective(cart);
    }

    @Override
    //更新购物车
    public void updateCart(Cart cart) {
        Example example=new Example(Cart.class);
        example.createCriteria().andEqualTo("id",cart.getId());
        cartMapper.updateByExampleSelective(cart,example);
    }
    //同步缓存
    public void flushCartCache(String memberId){
        Cart cart=new Cart();
        cart.setMemberId(memberId);
        //获取改用户数据库购物车数据
        List<Cart> cartList=cartMapper.select(cart);
        Jedis jedis=redisUtil.getJedis();
        Map<String,String> map=new HashMap<>();
        for(Cart cart1:cartList){
            cart1.setTotalPrice(cart1.getPrice().multiply(cart1.getQuantity()));
            map.put(cart1.getProductSkuId(), JSON.toJSONString(cart1));
        }
        //清除缓存
        jedis.del("member:"+memberId+":cart");
        //设置缓存
        jedis.hmset("member:"+memberId+":cart",map);
        jedis.close();
    }

    @Override
    public void checkCart(Cart cart) {
        Example example=new Example(Cart.class);
        example.createCriteria().andEqualTo("memberId",cart.getMemberId()).andEqualTo("productSkuId",cart.getProductSkuId());
        cartMapper.updateByExampleSelective(cart,example);
        flushCartCache(cart.getMemberId());
    }
}
