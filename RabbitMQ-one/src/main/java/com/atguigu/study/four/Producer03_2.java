package com.atguigu.study.four;

import com.atguigu.study.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;

/**
 * @author <a href="mailto:jiangyue@dtstack.com">江月 At 袋鼠云</a>.
 * @description 发布确认 -- 批量消息确认
 * @date 2021/12/2 10:06 上午
 */
public class Producer03_2 {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        //开启发布确认模式
        channel.confirmSelect();

        //声明队列
        channel.queueDeclare(RabbitMQUtil.QUEUE_NAME, false, false, false, null);

        //总消息数量
        int messageCount = 103;

        //批量确认的数量
        int batchSize = 10;

        //未确认消息的标识
        int flag = 0;

        for (int i = 1; i <= messageCount; i++) {
            channel.basicPublish("", RabbitMQUtil.QUEUE_NAME, null, (i+"").getBytes());
            System.out.println("消息发送成功：" + i);
            flag++;
            if(flag == batchSize){
                boolean success = channel.waitForConfirms();
                if(success){
                    System.out.println("消息批量确认。。。。。。。。。" + flag);
                    flag = 0;
                }
            }
        }

        //这里的逻辑是为了防止，最后一批发布的消息，不足批量确认的数量，所以需要单独处理
        if(flag>0){
            boolean success = channel.waitForConfirms();
            if(success){
                System.out.println("消息批量确认。。。。。。。。。" + flag);
                flag = 0;
            }
        }

    }

}
