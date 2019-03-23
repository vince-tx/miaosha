package com.tx.smalldemo.miaosha.service;

import com.google.common.util.concurrent.RateLimiter;
import com.sun.org.apache.regexp.internal.RE;
import com.tx.smalldemo.miaosha.dao.MiaoshaDAO;
import com.tx.smalldemo.miaosha.mq.MsgSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tangxiao
 * @create 2019-03-22 13:43
 * @Since --
 **/
@Service
public class MiaoshaService {
    Logger logger = LoggerFactory.getLogger(MiaoshaService.class);

    @Autowired
    private MiaoshaDAO miaoshaDAO;

    /**
     * 简单的减库存操作 未加锁
     *
     * @return
     */
    public String deductApple() {
        if (miaoshaDAO.getAppleNum() <= 0) {
            return "失败";
        }
        int i = miaoshaDAO.deductApple();
        if (i == 0) {
            return "失败";
        }
        return "成功";
    }

    /**
     * 使用Java synchoronized 同步锁
     *
     * @return
     */
    public synchronized String deductAppleBySync() {
        if (miaoshaDAO.getAppleNum() <= 0) {
            return "失败";
        }
        int i = miaoshaDAO.deductApple();
        if (i == 0) {
            return "失败";
        }
        return "成功";
    }


    @Autowired
    private StringRedisTemplate template;

    /**
     * 使用redis做库存预减处理
     *
     * @return
     */
    public String deductAppleByRedis() {
        Long apple = template.opsForValue().decrement("apple", 1);

        if (apple < 0) {
            return "失败";
        }
        int i = miaoshaDAO.deductApple();
        if (i == 0) {
            return "失败";
        }
        return "成功";
    }

    /**
     * lua脚本
     *
     * @return
     */
    public String deductAppleByRedisLuaScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript();
        script.setResultType(Long.class);
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("miaosha.lua")));
        List key = new ArrayList<>();
        key.add("num");
        Long execute = template.execute(script, key);
        if (execute == 1) {
            miaoshaDAO.deductApple();
            return "成功";
        }
        return "失败";
        //return template.execute(script, key, "1", "20") == 1 ? "好了" : "坏了";
    }


    @Autowired
    private MsgSender sender;

    public String deductAppleByRabbitMQ() {
        Long apple = template.opsForValue().decrement("apple", 1);
        if (apple < 0) {
            return "失败";
        }
        //miaoshaDAO.deductApple();
        sender.sendMessage(apple);
        return "成功";
    }


}
