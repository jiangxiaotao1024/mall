package mall.orderservice.serviceImpl;

import bean.Order;
import bean.OrderItem;
import com.alibaba.dubbo.config.annotation.Service;
import mall.mq.ActiveMQUtil;
import mall.orderservice.mapper.OrderItemMapper;
import mall.orderservice.mapper.OrderMapper;
import mall.util.RedisUtil;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import service.OrderService;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ActiveMQUtil activeMQUtil;
    @Override
    public String genTradeCode(String memberId) {
        Jedis jedis= redisUtil.getJedis();
        String tradeCode= UUID.randomUUID().toString();
        jedis.setex("user:"+memberId+":tradeCode",60*15,tradeCode);
        jedis.close();
        return tradeCode;
    }

    @Override
    public String checkTradeCode(String memberId, String tradeCode) {
        Jedis jedis=null;
        try {
            jedis=redisUtil.getJedis();
            String tradeKey="user:"+memberId+":tradeCode";
            String checkTradeCode=jedis.get(tradeKey);
            System.out.println(checkTradeCode);
            //lua脚本发现key的同时删除tradeCode
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Long eval = (Long) jedis.eval(script, Collections.singletonList(tradeKey), Collections.singletonList(tradeCode));
            if(eval!=null&&eval!=0){
                //jedis.del(tradekrey)
                return "success";
            }
            else return "false";
        }finally {
            jedis.close();
        }
    }

    @Override
    public void saveOrder(Order order) {
        orderMapper.insertSelective(order);
        String orderId=order.getId();
        List<OrderItem> orderItemList=order.getOrderItems();
        for(OrderItem orderItem:orderItemList){
            orderItem.setOrderId(orderId);
            orderItemMapper.insertSelective(orderItem);
            //删除购物车数据
        }
    }

    @Override
    public Order getOrderByOutTradeNo(String outTradeNo) {
        Order order=new Order();
        order.setOrderSn(outTradeNo);
        return orderMapper.selectOne(order);
    }

    @Override
    public void updateOrder(Order order) {
        Example e=new Example(Order.class);
        e.createCriteria().andEqualTo("orderSn",order.getOrderSn());
        Order order1=new Order();
        order1.setStatus("1");
        //发送一个订单已支付队列
        Connection connection=null;
        Session session=null;
        try {
            orderMapper.updateByExampleSelective(order1,e);
            connection=activeMQUtil.getConnectionFactory().createConnection();
            session=connection.createSession(true,Session.SESSION_TRANSACTED);
            Queue order_pay_queue = session.createQueue("ORDER_PAY_QUEUE");
            MessageProducer producer=session.createProducer(order_pay_queue);
            MapMessage mapMessage=new ActiveMQMapMessage();
            producer.send(mapMessage);
            session.commit();
        } catch (JMSException ex) {
            try {
                session.rollback();
            } catch (JMSException exc) {
                exc.printStackTrace();
            }
        }finally {
            try {
                connection.close();
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
        }
    }
}
