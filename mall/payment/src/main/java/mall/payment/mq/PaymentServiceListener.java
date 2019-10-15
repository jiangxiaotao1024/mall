package mall.payment.mq;

import bean.PaymentInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import service.PaymentService;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.util.Date;
import java.util.Map;

@Component
public class PaymentServiceListener {
    @Autowired
    PaymentService paymentService;
    @JmsListener(destination = "PAYMENT_CHECK_QUEUE",containerFactory = "jmsQueueListener")
    public void consumePaymentCheckResult(MapMessage mapMessage) throws JMSException {
        String out_trade_no=mapMessage.getString("out_trade_no");
        Integer count=mapMessage.getInt("count");
        //调用支付宝检查端口
        System.out.println("进行延迟检查");
        Map<String,Object> resultMap=paymentService.checkAlipayPayment(out_trade_no);
        if(resultMap!=null&&!resultMap.isEmpty()){
            //交易创建成功
            //查询支付状态
            String trade_status= (String) resultMap.get("trade_status");
            if(StringUtils.isNotBlank(trade_status)&&trade_status.equals("TRADE_SUCCESS")){
                //支付成功
                PaymentInfo paymentInfo = new PaymentInfo();
                paymentInfo.setOrderSn(out_trade_no);
                paymentInfo.setPaymentStatus("已支付");
                paymentInfo.setAlipayTradeNo((String)resultMap.get("trade_no"));// 支付宝的交易凭证号
                paymentInfo.setCallbackContent((String)resultMap.get(("call_back_content")));//回调请求字符串
                paymentInfo.setCallbackTime(new Date());
                System.out.println("支付成功");
                paymentService.updatePayment(paymentInfo);
                return;
            }
        }
        if(count>0){
            System.out.println("发送延迟队列，剩余次数为"+count);
            count--;
            paymentService.sendDelayPaymentResultCheckQueue(out_trade_no,count);
        }else {
            System.out.println("次数耗尽");
        }
    }
}
