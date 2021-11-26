package com.atguigu.study.two;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.Serializable;
import java.util.Scanner;

/**
 * @author <a href="mailto:jiangyue@dtstack.com">江月 At 袋鼠云</a>.
 * @description
 * @date 2021/11/16 5:30 下午
 */
public class Task01 {

    private static final String QUEUE_NAME = "hello";
    private static final String SERIALIZABLE_QUEUE = "serializable queue";

    public static void mainTest(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        //是否持久化队列
        Boolean durable = false;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        durable = true;
        channel.queueDeclare(SERIALIZABLE_QUEUE, durable, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            /**
             * 参数说明：
             * 1. 交换机名称
             * 2. 队列名称
             * 3. 消息的其他配置信息（MessageProperties.PERSISTENT_TEXT_PLAIN  表示持久化消息）
             * 4. 消息内容
             */
            channel.basicPublish("", SERIALIZABLE_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("消息发送完成：" + message);
        }
    }


    //循环发送消息，每秒发送一条
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        //默认为轮询分发消息。设置 prefetchCount 表示使用不公平分发策略，优先给空闲的消费者发送消息，
        // 并且每个消费者阻塞的消息不超过 prefetchCount 个。
        int prefetchCount = 3;
        //是否应用于当前信道
        Boolean global = true;
        channel.basicQos(prefetchCount, true);

        //是否持久化队列
        Boolean durable = false;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        for (int i = 0; i < 30; i++) {
            /**
             * 参数说明：
             * 1. 交换机名称
             * 2. 队列名称
             * 3. 消息的其他配置信息（MessageProperties.PERSISTENT_TEXT_PLAIN  表示持久化消息）
             * 4. 消息内容
             */
            channel.basicPublish("", QUEUE_NAME, null, (i+"").getBytes());
            System.out.println("消息发送完成：" + i);

            Thread.sleep(1 * 1000);
        }

    }

}
