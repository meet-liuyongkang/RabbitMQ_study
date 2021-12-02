package com.atguigu.study.four;

import com.atguigu.study.utils.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author <a href="mailto:jiangyue@dtstack.com">江月 At 袋鼠云</a>.
 * @description     交换机 -- 消费者
 * @date 2021/12/2 2:09 下午
 */
public class Customer04_1 {

    public static void main(String[] args) throws Exception {
        //获取信道,
        Channel channel = RabbitMQUtil.getChannel();

        //声明一个交换机，类型是 FANOUT
        channel.exchangeDeclare(RabbitMQUtil.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        //创建一个随机队列，队列名称由RabbitMQ随机产生，当断开连接时，自动删除该队列
        String queueName = channel.queueDeclare().getQueue();

        //将队列与交换机绑定
        channel.queueBind(queueName, RabbitMQUtil.EXCHANGE_NAME, "");

        //接收消息-回调接口
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(Customer04_1.class.getName() + " - 消费者控制台：" + message);
        };

        //取消订阅-回调接口
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(Customer04_1.class.getName() + " - 消费者取消了订阅");
        };

        channel.basicConsume(queueName,true, deliverCallback, cancelCallback);
    }

}
