package mall.payment.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

public class TestMq {

    public static void main(String[] args) {
        //工厂
        ConnectionFactory connect = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try {
            //连接
            Connection connection = connect.createConnection();
            connection.start();
            //创建session
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);// 开启事务
            //创建消息
            Queue testqueue = session.createQueue("drink");// 队列模式的消息

            //Topic t = session.createTopic("");// 话题模式的消息

            //创建producer
            MessageProducer producer = session.createProducer(testqueue);
            //消息内容
            TextMessage textMessage=new ActiveMQTextMessage();
            textMessage.setText("我渴了，谁能帮我打一杯水！");
            //持久化
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            //发送
            producer.send(textMessage);
            session.commit();// 提交事务
            connection.close();//关闭链接

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
