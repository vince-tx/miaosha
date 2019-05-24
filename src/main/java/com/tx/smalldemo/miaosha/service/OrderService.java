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
    // 这里使用128个锁
    private final static String[] LOCKS = new String[128];

    static {
        for (int i = 0; i < 128; i++) {
            LOCKS[i] = "lock_" + i;
        }
    }

    /***
     * 下单：
     * 高并发条件需要不出现重复订单,库存不超出,性能
     * @param order
     * @return
     */
    public String createOrder01(Order order) {

        // 业务操作
        // 写入数据库
        // 写入redis
        // 出现异常循环5次，仍然失败则进入到消息队列
        return "";
    }

    public void getOrderByUId(Integer uId) {
        /**
         //



         1.查询缓存,存在订单则直接换回
         if(orderRedisCache.isOrderListExist(uId){
         return getOrderListFromCache(uId);
         }
         // 2.缓存中没有，先上锁,用户id的hashcode和锁数量与运算
         int index = uId.hashCode() & (locks.length -1);

         try{
         orderRedisCache.lock(locks[index])
         // 上锁后再次判断缓存是否存在
         if(orderRedisCache.isOrderListExist(uId){
         return getOrderListFromCache(uId);
         }
         // 查询数据库
         list = orderDAO.getOrderListDb(uId);
         // 如果数据库中无数据，就存入缓存中null,标识这个用户真的没有数据,等新订单入库，会删除此标识，放入订单数据
         if (list == null && list.size() = 0){
         cacheCloud.zAdd(orderRedisKey.getListKey(uId),0,null);
         }else{
         cacheCloud.zAdd(orderRedisKey.getListKey(uId),list);
         }
         return list;
         }
         finally{
         orderRedisCache.unLock(locks[index]);
         }


         */


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
