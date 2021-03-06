package com.atguigu.study.four;

import com.atguigu.study.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author <a href="mailto:jiangyue@dtstack.com">江月 At 袋鼠云</a>.
 * @description  发布确认 -- 单条消息确认
 *
 * 问题：由于每发送一条消息，都需要等待RabbitMQ返回确认结果，所以单条消息确认的方式效率非常低
 * 解决：引入批量确认的方式
 *
 * @date 2021/12/2 9:29 上午
 */
public class Producer03_1 {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        //开启发布确认模式
        channel.confirmSelect();

        //声明一个队列
        channel.queueDeclare(RabbitMQUtil.QUEUE_NAME, false, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //发布消息
            channel.basicPublish("", RabbitMQUtil.QUEUE_NAME, null, message.getBytes());

            boolean success = channel.waitForConfirms();
            if(success){
                System.out.println("消息发送成功：" + message);
            }else {
                System.out.println("消息发送失败：" + message);
            }
        }

    }

}
