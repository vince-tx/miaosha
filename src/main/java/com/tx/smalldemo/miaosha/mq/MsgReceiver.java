package com.tx.smalldemo.miaosha.mq;

import com.tx.smalldemo.miaosha.dao.MiaoshaDAO;
import com.tx.smalldemo.miaosha.service.MiaoshaService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tangxiao
 * @create 2019-03-23 18:34
 * @Since --
 **/
@Service
public class MsgReceiver {

    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    private MiaoshaDAO miaoshaDAO;


    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(Object message) {
        int i = miaoshaDAO.deductApple();
        if (i == 1) {
            System.out.println("okayokay" + message);
        }
    }

}
