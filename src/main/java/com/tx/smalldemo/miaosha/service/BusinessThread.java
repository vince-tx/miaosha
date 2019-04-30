package com.tx.smalldemo.miaosha.service;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author tangxiao
 * @create 2019-04-30 16:45
 * @Since --
 **/
@Component
@Data
@Scope(value = "prototype")
public class BusinessThread implements Runnable {

    private String acceptStr;

    public BusinessThread(String acceptStr) {
        this.acceptStr = acceptStr;
    }

    @Override
    public void run() {
        //业务操作
        System.out.println("已插入订单系统，订单号" + acceptStr);
    }
}
