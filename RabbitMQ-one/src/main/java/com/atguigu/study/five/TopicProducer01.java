package com.atguigu.study.five;

import com.atguigu.study.utils.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * @author <a href="mailto:jiangyue@dtstack.com">江月 At 袋鼠云</a>.
 * @description  发布、订阅  交换机
 * @date 2021/12/9 8:01 下午
 */
public class TopicProducer01 {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        //声明一个主题类型的交换机
        channel.exchangeDeclare(RabbitMQUtil.QUEUE_NAME_TOPIC, BuiltinExchangeType.TOPIC);

        for (int i = 0; i < 50; i++) {
            if(i%3 == 0){
                channel.basicPublish(RabbitMQUtil.QUEUE_NAME_TOPIC,"jishu.3", null, (i+"").getBytes());
            }
            if(i%5 == 0){
                channel.basicPublish(RabbitMQUtil.QUEUE_NAME_TOPIC,"jishu.5", null, (i+"").getBytes());
            }
            if(i%7 == 0){
                channel.basicPublish(RabbitMQUtil.QUEUE_NAME_TOPIC,"shushu.7", null, (i+"").getBytes());
            }
        }

    }

}
