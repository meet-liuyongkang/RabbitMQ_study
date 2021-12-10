package com.atguigu.study.four;

import com.atguigu.study.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author <a href="mailto:jiangyue@dtstack.com">江月 At 袋鼠云</a>.
 * @description  发布确认 -- 异步确认
 *
 * 店长良心推荐
 * 优点：异步确认发布的方式，由于是异步确认，效率最高，并且对每条消息都进行了编号，这样出现了问题，可以快速定位到具体的消息。
 * 缺点：代码逻辑比较复杂
 *
 * @date 2021/12/2 10:29 上午
 */
public class Producer03_3 {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        //使用一个线程安全的map，记录下所有发送消息的记录，当记录确认成功后删除，当记录确认失败后将消息重新发送
        ConcurrentSkipListMap<Long, String> concurrentSkipListMap = new ConcurrentSkipListMap();

        //开启发布确认模式
        channel.confirmSelect();

        ConfirmCallback ackConfirmCallback = (long deliveryTag, boolean multiple) -> {
            //批量发送的处理方式
            if(multiple){
                //获取小于当前批次最后一个消息编号的所有消息，并清除
                ConcurrentNavigableMap<Long, String> longStringConcurrentNavigableMap = concurrentSkipListMap.headMap(deliveryTag, true);
                longStringConcurrentNavigableMap.clear();
            } else {
                //不是批量确认的话，直接处理单条消息即可
                concurrentSkipListMap.remove(deliveryTag);
            }
            System.out.println("成功监听器：" + deliveryTag);
        };

        ConfirmCallback nackConfirmCallback = (long deliveryTag, boolean multiple) -> {
            //这里可以再定义一个线程安全的List，将失败的消息收集起来，再重新发送
            System.out.println("失败监听器：" + deliveryTag + "   消息内容：" + concurrentSkipListMap.get(deliveryTag));
            concurrentSkipListMap.remove(deliveryTag);
        };

        // 在信道中添加两个监听器
        channel.addConfirmListener(ackConfirmCallback, nackConfirmCallback);

        //声明队列
        channel.queueDeclare(RabbitMQUtil.QUEUE_NAME, false, false, false, null);

        for (int i = 0; i < 10; i++) {
            String message = "消息" + i;

            //即将发送消息的编号
            long nextPublishSeqNo = channel.getNextPublishSeqNo();
            //记录所有发送的消息
            concurrentSkipListMap.put(nextPublishSeqNo, message);

            channel.basicPublish("", RabbitMQUtil.QUEUE_NAME, null, message.getBytes());
        }

    }

}
