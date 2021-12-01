package com.atguigu.study.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

/**
 * @author <a href="mailto:jiangyue@dtstack.com">江月 At 袋鼠云</a>.
 * @description 生产者
 * @date 2021/10/21 10:27 上午
 */
public class Producer {
    private static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.254.151");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");

        //通过工厂获取连接对象
        Connection connection = connectionFactory.newConnection();

        //通过连接对象获取信道对象
        Channel channel = connection.createChannel();

        /**
         * 在信道中声明一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化 默认消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
         * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();

            /**
             * 发送一个消息
             * 1.发送到那个交换机
             * 2.发送到那个队列
             * 3.其他的参数信息
             * 4.发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        }

        System.out.println("消息发送成功！");
    }

}
