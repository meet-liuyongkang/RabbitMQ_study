package com.atguigu.study.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author <a href="mailto:jiangyue@dtstack.com">江月 At 袋鼠云</a>.
 * @description
 * @date 2021/11/16 4:45 下午
 */
public class Worker02 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        //接收消息回调接口
        DeliverCallback deliverCallback = (customerTag, delivery) -> {
            byte[] messageBodyByte = delivery.getBody();
            String messageBodyStr = new String(messageBodyByte);
            System.out.println("接收到消息：" + messageBodyStr);
        };

        CancelCallback cancelCallback = (customerTag) -> {
            System.out.println("消费消息：" + customerTag);
        };

        //消费者启动
        System.out.println("消费者启动完成，等待消费。。。");

        Channel channel = RabbitMQUtil.getChannel();
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }

}
