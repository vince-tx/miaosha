package com.tx.smalldemo.miaosha.dao;

import com.tx.smalldemo.miaosha.service.OrderService;
import org.springframework.stereotype.Repository;

/**
 * @author tangxiao
 * @create 2019-04-30 16:17
 * @Since --
 **/
@Repository
public interface OrderDAO {
    int createOrder(OrderService.Order order);
}
