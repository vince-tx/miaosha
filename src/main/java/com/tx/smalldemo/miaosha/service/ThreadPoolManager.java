package com.tx.smalldemo.miaosha.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author tangxiao
 * @create 2019-04-30 16:42
 * @Since --
 **/
@Component
public class ThreadPoolManager {

    // 线程池维护线程的最少数量
    private final static int CORE_POOL_SIZE = 2;

    // 线程池的最大数量
    private final static int MAX_POOL_SIZE = 10;

    // 线程池维护线程所允许的空闲时间
    private final static int KEEP_ALIVE_TIME = 0;

    // 线程池所使用的缓冲队列大小
    private final static int WORK_QUEUE_SIZE = 50;

    /**
     * 作为订单缓冲队列,当线程池满了，则将订单存入
     **/
    Queue<Object> queue = new LinkedBlockingQueue<Object>();

    final RejectedExecutionHandler handler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // todo 订单加入缓冲队列

        }
    };

   // final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue(WORK_QUEUE_SIZE), );


    /**
     * 用于存储在队列中订单，防止重复提交 ，真实场景可用redis代替验证重复
     */
    Map cacheMap = new ConcurrentHashMap<>();


    /**
     * 将任务加入线程池
     **/
    public void addOrders(String orderId) {
        // 缓存中不存在则执行线程
        if (cacheMap.get(orderId) == null) {
            cacheMap.put(orderId, new Object());
            BusinessThread businessThread = new BusinessThread(orderId);

        }

    }
}
