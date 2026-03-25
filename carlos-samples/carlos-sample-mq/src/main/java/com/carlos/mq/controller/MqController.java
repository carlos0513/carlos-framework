package com.carlos.mq.controller;

import com.carlos.core.response.Result;
import com.carlos.mq.core.MqTemplate;
import com.carlos.mq.support.DelayLevel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * MQ 消息发送接口
 *
 * @author Carlos
 * @date 2026/3/15
 */
@RestController
@RequestMapping("mq")
@Tag(name = "MQ消息接口", description = "消息队列发送示例接口")
@Slf4j
@RequiredArgsConstructor
public class MqController {

    private final MqTemplate mqTemplate;

    /**
     * 订单消息对象
     */
    @lombok.Data
    public static class OrderMessage implements Serializable {
        private static final long serialVersionUID = 1L;

        @Parameter(description = "订单ID")
        private String orderId;

        @Parameter(description = "用户ID")
        private String userId;

        @Parameter(description = "商品名称")
        private String productName;

        @Parameter(description = "订单金额")
        private BigDecimal amount;

        @Parameter(description = "订单状态")
        private String status;

        @Parameter(description = "创建时间")
        private LocalDateTime createTime;
    }

    /**
     * 发送普通消息
     */
    @PostMapping("send")
    @Operation(summary = "发送普通消息")
    public Result<Void> sendMessage(
        @Parameter(description = "消息主题", required = true) @RequestParam String topic,
        @Parameter(description = "消息内容", required = true) @RequestParam String message) {
        log.info("发送普通消息，topic: {}, message: {}", topic, message);
        mqTemplate.send(topic, message);
        return Result.success();
    }

    /**
     * 发送带标签的消息
     */
    @PostMapping("sendWithTag")
    @Operation(summary = "发送带标签的消息")
    public Result<Void> sendMessageWithTag(
        @Parameter(description = "消息主题", required = true) @RequestParam String topic,
        @Parameter(description = "消息标签", required = true) @RequestParam String tag,
        @Parameter(description = "消息内容", required = true) @RequestParam String message) {
        log.info("发送带标签消息，topic: {}, tag: {}, message: {}", topic, tag, message);
        mqTemplate.send(topic, tag, message);
        return Result.success();
    }

    /**
     * 发送延迟消息
     */
    @PostMapping("sendDelayed")
    @Operation(summary = "发送延迟消息")
    public Result<Void> sendDelayedMessage(
        @Parameter(description = "消息主题", required = true) @RequestParam String topic,
        @Parameter(description = "消息内容", required = true) @RequestParam String message,
        @Parameter(description = "延迟级别: SECOND_1, SECOND_5, SECOND_10, SECOND_30, MINUTE_1, MINUTE_2, MINUTE_3, MINUTE_4, MINUTE_5, MINUTE_6, MINUTE_7, MINUTE_8, MINUTE_9, MINUTE_10, MINUTE_20, MINUTE_30, HOUR_1, HOUR_2", required = true)
        @RequestParam String delayLevel) {
        log.info("发送延迟消息，topic: {}, message: {}, delayLevel: {}", topic, message, delayLevel);
        DelayLevel level = DelayLevel.valueOf(delayLevel);
        mqTemplate.sendDelayed(topic, message, level);
        return Result.success();
    }

    /**
     * 发送顺序消息
     */
    @PostMapping("sendOrdered")
    @Operation(summary = "发送顺序消息")
    public Result<Void> sendOrderedMessage(
        @Parameter(description = "消息主题", required = true) @RequestParam String topic,
        @Parameter(description = "消息内容", required = true) @RequestParam String message,
        @Parameter(description = "分片键（用于保证顺序）", required = true) @RequestParam String hashKey) {
        log.info("发送顺序消息，topic: {}, message: {}, hashKey: {}", topic, message, hashKey);
        mqTemplate.sendOrdered(topic, message, hashKey);
        return Result.success();
    }

    /**
     * 发送对象消息（订单示例）
     */
    @PostMapping("sendOrder")
    @Operation(summary = "发送订单消息")
    public Result<Void> sendOrderMessage(@RequestBody OrderMessage order) {
        log.info("发送订单消息，order: {}", order);
        order.setCreateTime(LocalDateTime.now());
        mqTemplate.send("order-topic", order);
        return Result.success();
    }

    /**
     * 发送带标签的订单消息
     */
    @PostMapping("sendOrderWithTag")
    @Operation(summary = "发送带标签的订单消息")
    public Result<Void> sendOrderWithTag(
        @Parameter(description = "订单标签: CREATED, PAID, SHIPPED, COMPLETED", required = true)
        @RequestParam String tag,
        @RequestBody OrderMessage order) {
        log.info("发送带标签订单消息，tag: {}, order: {}", tag, order);
        order.setCreateTime(LocalDateTime.now());
        mqTemplate.send("order-topic", tag, order);
        return Result.success();
    }

    /**
     * 发送延迟订单消息
     */
    @PostMapping("sendOrderDelayed")
    @Operation(summary = "发送延迟订单消息")
    public Result<Void> sendOrderDelayed(
        @RequestBody OrderMessage order,
        @Parameter(description = "延迟级别", required = true) @RequestParam DelayLevel delayLevel) {
        log.info("发送延迟订单消息，delayLevel: {}, order: {}", delayLevel, order);
        order.setCreateTime(LocalDateTime.now());
        mqTemplate.sendDelayed("order-delay-topic", order, delayLevel);
        return Result.success();
    }

    /**
     * 发送单向消息（不等待响应）
     */
    @PostMapping("sendOneWay")
    @Operation(summary = "发送单向消息")
    public Result<Void> sendOneWayMessage(
        @Parameter(description = "消息主题", required = true) @RequestParam String topic,
        @Parameter(description = "消息内容", required = true) @RequestParam String message) {
        log.info("发送单向消息，topic: {}, message: {}", topic, message);
        mqTemplate.sendOneWay(topic, message);
        return Result.success();
    }

    /**
     * 检查MQ连接状态
     */
    @GetMapping("ping")
    @Operation(summary = "检查MQ连接状态")
    public Result<Boolean> ping() {
        boolean available = mqTemplate.ping();
        log.info("MQ连接状态: {}", available);
        return Result.success(available);
    }

    /**
     * 获取当前MQ类型
     */
    @GetMapping("type")
    @Operation(summary = "获取当前MQ类型")
    public Result<String> getMqType() {
        String mqType = mqTemplate.getMqType();
        log.info("当前MQ类型: {}", mqType);
        return Result.success(mqType);
    }
}
