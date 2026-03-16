package com.carlos.mq.consumer;

import com.carlos.mq.annotation.MqListener;
import com.carlos.mq.controller.MqController;
import com.carlos.mq.support.ConsumeMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 订单消息消费者示例
 *
 * @author Carlos
 * @date 2026/3/15
 */
@Component
@Slf4j
public class OrderMessageConsumer {

    /**
     * 监听普通订单消息
     */
    @MqListener(
        topic = "order-topic",
        group = "order-consumer-group",
        concurrency = 5
    )
    public void onOrderMessage(MqController.OrderMessage order) {
        log.info("【普通消费】收到订单消息，orderId: {}, userId: {}, productName: {}, amount: {}",
            order.getOrderId(), order.getUserId(), order.getProductName(), order.getAmount());
        // 处理订单业务逻辑
        processOrder(order);
    }

    /**
     * 监听订单创建消息（带标签过滤）
     */
    @MqListener(
        topic = "order-topic",
        tag = "CREATED",
        group = "order-created-consumer-group"
    )
    public void onOrderCreated(MqController.OrderMessage order) {
        log.info("【订单创建】收到订单创建消息，orderId: {}, userId: {}", order.getOrderId(), order.getUserId());
        // 处理订单创建逻辑，如发送通知、初始化库存等
    }

    /**
     * 监听订单支付消息（带标签过滤）
     */
    @MqListener(
        topic = "order-topic",
        tag = "PAID",
        group = "order-paid-consumer-group"
    )
    public void onOrderPaid(MqController.OrderMessage order) {
        log.info("【订单支付】收到订单支付消息，orderId: {}, amount: {}", order.getOrderId(), order.getAmount());
        // 处理订单支付逻辑，如更新订单状态、通知仓库发货等
    }

    /**
     * 监听订单发货消息（带标签过滤）
     */
    @MqListener(
        topic = "order-topic",
        tag = "SHIPPED",
        group = "order-shipped-consumer-group"
    )
    public void onOrderShipped(MqController.OrderMessage order) {
        log.info("【订单发货】收到订单发货消息，orderId: {}", order.getOrderId());
        // 处理订单发货逻辑，如发送物流通知等
    }

    /**
     * 监听延迟订单消息（用于超时处理）
     */
    @MqListener(
        topic = "order-delay-topic",
        group = "order-delay-consumer-group"
    )
    public void onOrderDelayed(MqController.OrderMessage order) {
        log.info("【延迟消费】收到延迟订单消息，orderId: {}, status: {}", order.getOrderId(), order.getStatus());
        // 处理订单超时逻辑，如自动取消未支付订单等
        handleOrderTimeout(order);
    }

    /**
     * 广播模式消费者 - 所有实例都会收到
     */
    @MqListener(
        topic = "broadcast-topic",
        group = "broadcast-consumer-group",
        consumeMode = ConsumeMode.BROADCAST
    )
    public void onBroadcastMessage(String message) {
        log.info("【广播消费】收到广播消息: {}", message);
    }

    /**
     * 监听字符串消息
     */
    @MqListener(
        topic = "string-topic",
        group = "string-consumer-group",
        concurrency = 3
    )
    public void onStringMessage(String message) {
        log.info("【字符串消费】收到消息: {}", message);
    }

    /**
     * 处理订单业务逻辑
     */
    private void processOrder(MqController.OrderMessage order) {
        try {
            // 模拟业务处理
            Thread.sleep(100);
            log.info("订单处理完成，orderId: {}", order.getOrderId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("订单处理被中断，orderId: {}", order.getOrderId());
        }
    }

    /**
     * 处理订单超时逻辑
     */
    private void handleOrderTimeout(MqController.OrderMessage order) {
        log.info("处理订单超时，orderId: {}, status: {}", order.getOrderId(), order.getStatus());
        // 实际业务中可能会查询订单状态，如果仍未支付则取消订单
    }
}
