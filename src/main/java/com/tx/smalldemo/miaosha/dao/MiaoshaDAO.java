package com.tx.smalldemo.miaosha.dao;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author tangxiao
 * @create 2019-03-22 13:43
 * @Since --
 **/
@Repository
public interface MiaoshaDAO {

    @Update("update apple set num = num - 1 where id = 1")
    int deductApple();

    @Select("select num from apple where id = 1")
    int getAppleNum();
}
