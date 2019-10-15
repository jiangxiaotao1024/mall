package mall.orderservice.mq;

import bean.Order;
import com.alibaba.dubbo.config.annotation.Service;
import mall.mq.ActiveMQUtil;
import mall.orderservice.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import service.OrderService;

import javax.jms.JMSException;
import javax.jms.MapMessage;

@Component
public class OrderServiceMqListener {
    @Autowired
    OrderService orderService;
    @JmsListener(destination = "PAYMENT_SUCCESS_QUEUE",containerFactory = "jmsQueueListener")
    public void consumerPaymentResult(MapMessage mapMessage) throws JMSException {
        String out_trade_no=mapMessage.getString("out_trade_no");
        Order order=new Order();
        order.setOrderSn(out_trade_no);
        orderService.updateOrder(order);
    }
}
