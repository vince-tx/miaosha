package com.tx.smalldemo.miaosha.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tangxiao
 * @create 2019-03-23 18:34
 * @Since --
 **/
@Service
public class MsgSender {

    @Autowired
    AmqpTemplate amqpTemplate;


    public void sendMessage(Object message) {
        amqpTemplate.convertAndSend(MQConfig.QUEUE, message);
    }

}
