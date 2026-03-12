package com.carlos.message.service;

import com.carlos.core.pagination.Paging;
import com.carlos.core.response.Result;
import com.carlos.message.convert.MessageConvert;
import com.carlos.message.manager.MessageReceiverManager;
import com.carlos.message.manager.MessageRecordManager;
import com.carlos.message.pojo.dto.MessageReceiverDTO;
import com.carlos.message.pojo.dto.MessageRecordDTO;
import com.carlos.message.pojo.entity.MessageReceiver;
import com.carlos.message.pojo.entity.MessageRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 消息查询服务
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageQueryService {

    private final MessageRecordManager messageRecordManager;
    private final MessageReceiverManager messageReceiverManager;
    private final MessageConvert messageConvert;

    /**
     * 根据消息ID查询详情
     *
     * @param messageId 消息ID
     * @return 消息记录
     */
    public Result<MessageRecordDTO> getById(String messageId) {
        log.info("查询消息详情: {}", messageId);
        MessageRecord record = messageRecordManager.getByMessageId(messageId);
        if (record == null) {
            return Result.fail("消息不存在");
        }
        return Result.ok(messageConvert.toDTO(record));
    }

    /**
     * 分页查询消息记录
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    public Result<Paging<MessageRecordDTO>> page(int page, int size) {
        log.info("分页查询消息记录, page: {}, size: {}", page, size);
        // TODO: 实现分页查询
        return Result.ok(new Paging<>());
    }

    /**
     * 查询消息接收人列表
     *
     * @param messageId 消息ID
     * @return 接收人列表
     */
    public Result<List<MessageReceiverDTO>> getReceivers(String messageId) {
        log.info("查询消息接收人列表: {}", messageId);
        List<MessageReceiver> receivers = messageReceiverManager.listByMessageId(messageId);
        return Result.ok(messageConvert.toReceiverList(receivers));
    }

    /**
     * 查询用户的未读消息
     *
     * @param userId 用户ID
     * @return 未读消息列表
     */
    public Result<List<MessageRecordDTO>> getUnread(String userId) {
        log.info("查询用户未读消息: {}", userId);
        // TODO: 实现未读消息查询
        return Result.ok(List.of());
    }

    /**
     * 查询消息状态
     *
     * @param messageId 消息ID
     * @return 消息状态
     */
    public Result<Integer> queryStatus(String messageId) {
        log.info("查询消息状态: {}", messageId);
        MessageRecord record = messageRecordManager.getByMessageId(messageId);
        if (record == null) {
            return Result.fail("消息不存在");
        }
        // 根据成功/失败数计算状态
        return Result.ok(0);
    }
}
