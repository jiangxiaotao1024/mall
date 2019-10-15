package mall.payment.serviceImpl;

import bean.PaymentInfo;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import mall.mq.ActiveMQUtil;
import mall.payment.mapper.PaymentMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import service.PaymentService;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    PaymentMapper paymentMapper;
    @Autowired
    ActiveMQUtil activeMQUtil;
    @Autowired
    AlipayClient alipayClient;
    @Override
    public void savePaymentInfo(PaymentInfo paymentInfo) {
       paymentMapper.insertSelective(paymentInfo);
    }

    @Override
    public void updatePayment(PaymentInfo paymentInfo){
        //幂等性检查
        PaymentInfo paymentInfo1=new PaymentInfo();
        paymentInfo1.setOrderSn(paymentInfo.getOrderSn());
        PaymentInfo paymentInfo2=paymentMapper.selectOne(paymentInfo1);
        if(StringUtils.isNotBlank(paymentInfo2.getPaymentStatus())&&paymentInfo2.getPaymentStatus().equals("已支付")){
            return;
        }else {
            String orderSn = paymentInfo.getOrderSn();
            Example e=new Example(PaymentInfo.class);
            e.createCriteria().andEqualTo("orderSn",orderSn);
            Connection connection = null;
            Session session=null;
            try {
                paymentMapper.updateByExampleSelective(paymentInfo,e);
                connection = activeMQUtil.getConnectionFactory().createConnection();
                session = connection.createSession(true, Session.SESSION_TRANSACTED);
                Queue payment_success_queue=session.createQueue("PAYMENT_SUCCESS_QUEUE");
                MessageProducer producer=session.createProducer(payment_success_queue);
                MapMessage mapMessage=new ActiveMQMapMessage();
                mapMessage.setString("out_trade_no",orderSn);
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

    @Override
    public void sendDelayPaymentResultCheckQueue(String outTradeNo, int i) {
        Connection connection=null;
        Session session=null;
        try {
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session=connection.createSession(true,Session.SESSION_TRANSACTED);
            Queue payment_check_queue = session.createQueue("PAYMENT_CHECK_QUEUE");
            MessageProducer producer = session.createProducer(payment_check_queue);
            MapMessage mapMessage=new ActiveMQMapMessage();
            mapMessage.setString("out_trade_no",outTradeNo);
            mapMessage.setInt("count",i);
            mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,1000*10);
            producer.send(mapMessage);
            session.commit();
        } catch (JMSException e) {
            try {
                session.rollback();
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
        }finally {
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<String, Object> checkAlipayPayment(String out_trade_no) {
        Map<String,Object> resultMap=new HashMap<>();
        AlipayTradeQueryRequest request=new AlipayTradeQueryRequest();
        Map<String,Object> requestMap=new HashMap<>();
        requestMap.put("out_trade_no",out_trade_no);
        request.setBizContent(JSON.toJSONString(requestMap));
        AlipayTradeQueryResponse response=null;
        try {
            response=alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("交易已创建");
            resultMap.put("out_trade_no",response.getOutTradeNo());
            resultMap.put("trade_no",response.getTradeNo());
            resultMap.put("trade_status",response.getTradeStatus());
            resultMap.put("call_back_content",response.getMsg());
        }else {
            System.out.println("交易创建失败");
        }
        return resultMap;
    }
}
