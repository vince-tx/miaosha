package com.tx.smalldemo.miaosha.mq.delayMQ;

import com.rabbitmq.client.ConnectionFactory;

/**
 * 单例的工厂
 *
 * @author tangxiao
 * @create 2019-05-06 14:21
 * @Since --
 **/
public class MqConnectionFactory {

    private volatile static ConnectionFactory connectionFactory;

    private MqConnectionFactory() {
    }

    public static ConnectionFactory getConnectionFactory() {
        if (connectionFactory == null) {
            synchronized (MqConnectionFactory.class) {
                if (connectionFactory == null) {
                    connectionFactory = new ConnectionFactory();
                    connectionFactory.setHost("192.168.198.131");
                    return connectionFactory;
                }
            }
        }
        return connectionFactory;
    }

}
