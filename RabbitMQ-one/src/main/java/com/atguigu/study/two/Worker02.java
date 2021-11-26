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
        Channel channel = RabbitMQUtil.getChannel();

        //默认为轮询分发消息。设置 prefetchCount 表示使用不公平分发策略，优先给空闲的消费者发送消息，
        // 并且每个消费者阻塞的消息不超过 prefetchCount 个。
        int prefetchCount = 3;
        //是否应用于当前信道
        Boolean global = true;
        channel.basicQos(prefetchCount, true);

        //接收消息回调接口
        DeliverCallback deliverCallback = (customerTag, delivery) -> {

            byte[] messageBodyByte = delivery.getBody();
            String messageBodyStr = new String(messageBodyByte);
            System.out.println("接收到消息：" + messageBodyStr);

            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            //如果出现异常，则此消息不会手动应答，这个消息会重新被加入到队列中，发送给其他的消费者
            if("101".equals(messageBodyStr)){
                int a = 1/0;
            }

            /**
             * basicAck 方法参数：
             * 1. DeliveryTag：消息的标记tag
             * 2. multiple：是否批量应答
             */
            System.out.println("消息处理完成：" + messageBodyStr);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

        };

        //取消订阅时，回调此接口
        CancelCallback cancelCallback = (customerTag) -> {
            System.out.println("消费消息：" + customerTag);
        };

        //消费者启动
        System.out.println("消费者启动完成，等待消费。。。");

        //消息是否自动应答
        Boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }

}
