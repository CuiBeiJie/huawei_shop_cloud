package com.huawei.page.mq;

import com.huawei.page.service.FileService;
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
    private FileService fileService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "page.item.insert.queue", durable = "true"),
            exchange = @Exchange(
                    value = "huawei.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    public void listenInsertOrUpdate(Message message, Channel channel) throws IOException {
        Long spuId = Long.parseLong(new String(message.getBody(),"utf-8"));
        if(spuId == null){
            return;
        }
       //处理消息,新增静态页
        fileService.createHtml(spuId);
        //手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    /**
     * 处理delete的消息
     *
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "page.item.delete.queue", durable = "true"),
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
        // 删除静态页
         fileService.deleteHtml(spuId);
        //手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
