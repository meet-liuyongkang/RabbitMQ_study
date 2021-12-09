package com.atguigu.study.five;

import com.atguigu.study.utils.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * @author <a href="mailto:jiangyue@dtstack.com">江月 At 袋鼠云</a>.
 * @description  主题类型交换机
 * @date 2021/12/9 8:01 下午
 */
public class TopicCustomer02 {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        channel.exchangeDeclare(RabbitMQUtil.QUEUE_NAME_TOPIC, BuiltinExchangeType.TOPIC);

        String queueName = channel.queueDeclare().getQueue();
//        String queueName = "queueTest01";
        channel.queueBind(queueName, RabbitMQUtil.QUEUE_NAME_TOPIC, "shushu.*");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            byte[] body = message.getBody();
            System.out.println("2号消费了消息：" + new String(body));
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {
                System.out.println("2号消费者关闭了连接。。。");
            }
        };

        channel.basicConsume(queueName, deliverCallback, cancelCallback);
    }

}
