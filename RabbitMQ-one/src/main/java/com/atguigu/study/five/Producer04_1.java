package com.atguigu.study.five;

import com.atguigu.study.utils.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author <a href="mailto:jiangyue@dtstack.com">江月 At 袋鼠云</a>.
 * @description     交换机
 * @date 2021/12/2 10:53 上午
 */
public class Producer04_1 {

    public static void main(String[] args) throws Exception {
        //创建信道
        Channel channel = RabbitMQUtil.getChannel();

        //声明一个交换机，类型是 FANOUT
        channel.exchangeDeclare(RabbitMQUtil.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();

            //这里队列传的空串，由于Exchange的类型是 FANOUT ，也就是广播，所以绑定这个交换机的所有队列都会收到消息。
            //之前的代码中，没有指定交换机，使用的就是默认的交换机，默认的交换机类型是 DIRECT
            channel.basicPublish(RabbitMQUtil.EXCHANGE_NAME, "", null, message.getBytes());

        }
    }

}
