package com.tx.smalldemo;

import com.tx.smalldemo.miaosha.service.MiaoshaService;
import jdk.nashorn.internal.codegen.CompilerConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmallDemoApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(SmallDemoApplicationTests.class);

    @Autowired
    private MiaoshaService service;

    /**
     * 用时875ms ,出现超发
     *
     * @throws Exception
     */
    @Test
    public void normalTest() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        List<Callable<String>> list = new ArrayList<>();
        Callable task;
        for (int i = 0; i < 20; i++) {
            task = () -> {
                String s = service.deductApple();
                return s;
            };
            list.add(task);
        }
        List<Future<String>> futures = executorService.invokeAll(list);
        futures.stream().forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 用时953 ，未超发
     *
     * @throws Exception
     */
    @Test
    public void SyncTest() throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        List<Callable<String>> list = new ArrayList<>();
        Callable task;
        for (int i = 0; i < 20; i++) {
            task = () -> {
                String s = service.deductAppleBySync();
                return s;
            };
            list.add(task);
        }
        List<Future<String>> futures = executorService.invokeAll(list);
        futures.stream().forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 用时217 ,未超发
     *
     * @throws Exception
     */
    @Test
    public void redisTest() throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        List<Callable<String>> list = new ArrayList<>();
        Callable task;
        for (int i = 0; i < 20; i++) {
            task = () -> {
                String s = service.deductAppleByRedis();
                return s;
            };
            list.add(task);
        }
        List<Future<String>> futures = executorService.invokeAll(list);
        futures.stream().forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 用时217 ,未超发
     *
     * @throws Exception
     */
    @Test
    public void rbmqTest() throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        List<Callable<String>> list = new ArrayList<>();
        Callable task;
        for (int i = 0; i < 20; i++) {
            task = () -> {
                String s = service.deductAppleByRedisLuaScript();
                return s;
            };
            list.add(task);
        }
        List<Future<String>> futures = executorService.invokeAll(list);
        futures.stream().forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        Thread.sleep(2100);
    }


}
