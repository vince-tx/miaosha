package com.tx.smalldemo.miaosha.web;

import com.tx.smalldemo.miaosha.service.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sun.nio.ch.ThreadPool;

import java.util.Queue;
import java.util.UUID;

/**
 * @author tangxiao
 * @create 2019-05-05 10:37
 * @Since --
 **/
@RestController
public class ThreadTestController {

    @Autowired
    private ThreadPoolManager threadPoolManager;

    @GetMapping("/start/{id}")
    public String start(@PathVariable Long id) {
        //模拟随机数
        String orderNo = System.currentTimeMillis() + UUID.randomUUID().toString();

        threadPoolManager.addOrders(orderNo);
        return "Test TreadPoolExecutor start";
    }


    @GetMapping("/stop/{id}")
    public String stop(@PathVariable Long id) {
        threadPoolManager.shutdown();
        Queue<Object> queue = threadPoolManager.getMsgQueue();
        System.out.println("关闭了线程服务，还有未处理的信息条数：" + queue.size());
        return "Test ThreadPool stop";
    }
}
