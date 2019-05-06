参考博客：  
线程池：https://blog.csdn.net/u011677147/article/details/80271174  
延迟队列：（1）https://www.cnblogs.com/DBGzxx/p/10090840.html   （2）https://www.jianshu.com/p/e40f55ecefda

延迟队列场景：未支付订单30分钟取消<br>
ps: 所有订单进入消息队列设置30分钟过期时间，过期会进入死信队列，消费死信队列中的订单做一些业务处理（如未支付则取消订单）