package com.tx.smalldemo.miaosha.mq.delayMQ;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author tangxiao
 * @create 2019-05-06 14:20
 * @Since --
 **/
public class DelayConsumer {

    public static void receive() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = MqConnectionFactory.getConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel();

        //声明一个死信路由
        String dlxExchangeName = "dlx_delay";
        channel.exchangeDeclare(dlxExchangeName, BuiltinExchangeType.TOPIC);
        // 声明一个死信订单队列
        String dlxQueueName = "dlx_delay_order";
        channel.queueDeclare(dlxExchangeName, true, true, false, null);
        // 绑定
        channel.exchangeBind(dlxQueueName, dlxExchangeName, "#");
        // 声明一个交换机
        channel.exchangeDeclare(DelayProducer.exchangeName, BuiltinExchangeType.DIRECT);
        // 声明一个延时队列，并绑定死信路由器
        String queueName = "delay_order";
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", dlxExchangeName);
        // 五秒过期
        arguments.put("x-message-ttl", 5 * 1000);
        channel.queueDeclare(queueName, true, false, false, arguments);
        // 绑定
        channel.queueBind(queueName, DelayProducer.exchangeName, DelayProducer.routeKey);

        // 声明一个消费者（关闭为支付订单的服务）
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received and close [" + envelope.getRoutingKey() + "]" + message);
            }
        };

        // 消费者消费-队列
        channel.basicConsume(dlxQueueName, true, consumer);
    }

}
