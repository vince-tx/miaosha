package com.tx.smalldemo.miaosha.service;

import com.tx.smalldemo.miaosha.dao.OrderDAO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tangxiao
 * @create 2019-04-30 16:11
 * @Since --
 **/
@Service
public class OrderService {

    @Autowired
    private OrderDAO orderDAO;

    /***
     * 下单：
     * 高并发条件需要不出现重复订单,库存不超出,性能
     * @param order
     * @return
     */
    public String createOrder01(Order order) {

        //*****第一种，最简单的方法，容易造成超出库存、重复订单的情况

        //判断库存，如果 number <库存 提示 库存不足
        //创建订单
        int i = orderDAO.createOrder(order);
        if (i == 0) {
            return "下单失败";
        }
        // 发送通知
        // 各种操作
        return "下单成功";
    }



    @Data
    public class Order {
        /**
         * 商品id
         */
        private Integer goodsId;
        /**
         * 购买数量
         */
        private Integer number;
        /**
         * 用户id
         */
        private Integer userId;
    }
}
