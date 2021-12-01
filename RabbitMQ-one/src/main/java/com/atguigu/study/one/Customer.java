package com.atguigu.study.one;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import javax.naming.CompositeName;
import java.io.IOException;

/**
 * @author <a href="mailto:jiangyue@dtstack.com">江月 At 袋鼠云</a>.
 * @description 消费者
 * @date 2021/10/22 11:19 上午
 */
public class Customer {

    private static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.254.151");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");

        //通过连接工厂，创建连接对象
        Connection connection = connectionFactory.newConnection();

        //在连接中创建一个信道
        Channel channel = connection.createChannel();

        //消费消息的回调接口
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("处理回调接口！" + consumerTag);
            String message= new String(delivery.getBody());
            System.out.println(message);

            //消费者，取消订阅该对队列
            if("cancel".equals(message)){
                System.out.println("调用了取消订阅接口！");
                channel.basicCancel(QUEUE_NAME);
            }

            //消费者，删除该队列
            if("delete".equals(message)){
                System.out.println("调用了删除订阅接口！");
                channel.queueDelete(QUEUE_NAME);
            }

        };

        //取消消息的回调接口，除了调用 channel.basicCancel 方法取消订阅，其他任何形式取消订阅，都会回调此接口
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("取消回调接口！" + consumerTag);
        };

        /**
         * 消费消息
         * 1.队列名称
         * 2.是否自动应答
         * 3.处理消息的回调接口
         * 4.取消消息的回调接口
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }

}
