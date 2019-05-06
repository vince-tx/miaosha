package com.tx.smalldemo.miaosha.mq.delayMQ;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author tangxiao
 * @create 2019-05-06 14:20
 * @Since --
 **/
public class DelayProducer {

    public static Logger logger = LoggerFactory.getLogger(DelayProducer.class);

    // 交换机
    public static final String exchangeName = "delay_Task";

    // 路由键
    public static final String routeKey = "delay_order";


    public static void pend(List<String> orderNos) throws IOException, TimeoutException {

        // 通过单例工厂获取
        ConnectionFactory connectionFactory = MqConnectionFactory.getConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel();
        // 声明一个交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);

        // 消息绑定
        orderNos.stream().forEach(o -> {
            try {
                channel.basicPublish(exchangeName, routeKey, null, o.getBytes());
                logger.info("发送订单：" + orderNos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        channel.close();
        connection.close();
    }

}
