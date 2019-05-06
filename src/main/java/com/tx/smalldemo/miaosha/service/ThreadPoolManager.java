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
     * 用于存储在队列中订单，防止重复提交 ，真实场景可用redis代替验证重复
     */
    Map cacheMap = new ConcurrentHashMap<>();

    /**
     * 作为订单缓冲队列,当线程池满了，则将订单存入
     **/
    Queue<Object> msgQueue = new LinkedBlockingQueue<Object>();


    final RejectedExecutionHandler handler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // todo 订单加入缓冲队列
            msgQueue.offer(((BusinessThread) r).getAcceptStr());
            System.out.println("系统任务忙，把订单交给（调度线程池）逐一处理，订单号：" + ((BusinessThread) r).getAcceptStr());
        }
    };

    final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue(WORK_QUEUE_SIZE), this.handler);


    /**
     * 将任务加入线程池
     **/
    public void addOrders(String orderId) {
        // 缓存中不存在则执行线程
        if (cacheMap.get(orderId) == null) {
            cacheMap.put(orderId, new Object());
            BusinessThread businessThread = new BusinessThread(orderId);
            threadPoolExecutor.execute(businessThread);
        }

    }

    /**
     * 线程池的定时任务--->称为（调度线程池），定时以及周期性执行任务的需求。
     */
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    /**
     * 检查（调度线程池），每秒执行一次，查看订单的缓冲队列是否有订单记录，则重新加入到线程池
     */
    final ScheduledFuture scheduledFuture = scheduler.scheduleAtFixedRate(new Runnable() {
        @Override
        public void run() {
            if (!msgQueue.isEmpty()) {
                if (threadPoolExecutor.getQueue().size() < WORK_QUEUE_SIZE) {
                    String orderId = (String) msgQueue.poll();
                    BusinessThread businessThread = new BusinessThread(orderId);
                    threadPoolExecutor.execute(businessThread);
                    System.out.println("(调度线程池)缓冲队列出现订单业务，重新添加到线程池，订单号：" + orderId);
                }
            }

        }
    }, 0, 1, TimeUnit.SECONDS);

    /**
     * 获取消息缓冲队列
     */
    public Queue<Object> getMsgQueue() {
        return msgQueue;
    }

    /**
     * 终止订单线程池+调度线程池
     */
    public void shutdown() {
        System.out.println("终止订单线程池+调度线程池：" + scheduledFuture.cancel(false));
        scheduler.shutdown();
        threadPoolExecutor.shutdown();
    }

}
