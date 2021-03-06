package com.huawei.search.mq;

import com.huawei.search.service.SearchService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @program: huaweishop
 * @description: 监听商品
 * @author: cuibeijie
 * @create: 2019-06-03 21:24
 */
@Component
public class ItemListener {
    @Autowired
    private SearchService searchService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "search.item.insert.queue", durable = "true"),
            exchange = @Exchange(
                    value = "huawei.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    public void listenInsertOrUpdate(Message message, Channel channel) throws IOException {
        System.out.println("==========message====="+message.getBody());
        System.out.println("======spuid======"+new String(message.getBody(),"utf-8"));
        Long spuId = Long.parseLong(new String(message.getBody(),"utf-8"));

        if(spuId == null){
            return;
        }
       //处理消息对消息进行新增修改
        searchService.createOrUpdateIndex(spuId);
        //手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    /**
     * 处理delete的消息
     *
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "search.item.delete.queue", durable = "true"),
            exchange = @Exchange(
                    value = "huawei.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = "item.delete"))
    public void listenDelete(Message message, Channel channel) throws IOException {
        Long spuId = Long.parseLong(new String(message.getBody(),"utf-8"));
        if(spuId == null){
            return;
        }
        // 删除索引
        searchService.deleteIndex(spuId);
        //手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
