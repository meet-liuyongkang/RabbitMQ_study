package com.atguigu.study.five;

import com.atguigu.study.utils.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * @author <a href="mailto:jiangyue@dtstack.com">江月 At 袋鼠云</a>.
 * @description     交换机 - 消费者2
 * @date 2021/12/2 2:42 下午
 */
public class Customer04_2 {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        //声明一个交换机， 类型为 FANOUT
        channel.exchangeDeclare(RabbitMQUtil.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        //声明一个随机队列，队列名称由RabbitMQ随机生成，当断开连接后，自动销毁该队列
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, RabbitMQUtil.EXCHANGE_NAME, "");

        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println(Customer04_2.class.getName() + " - 消费者磁盘：" + message);
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(Customer04_2.class.getName() + " - 消费者取消了订阅");
        };

        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }

}
