package com.carlos.message.service.impl;

import com.carlos.core.pojo.response.Paging;
import com.carlos.core.response.Result;
import com.carlos.message.pojo.dto.MessageRecordDTO;
import com.carlos.message.pojo.dto.MessageReceiverDTO;
import com.carlos.message.pojo.param.MessagePageParam;
import com.carlos.message.service.MessageQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 消息查询服务实现
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Slf4j
@Service
public class MessageQueryServiceImpl implements MessageQueryService {

    @Override
    public Result<MessageRecordDTO> getById(String messageId) {
        log.info("查询消息详情: {}", messageId);
        // TODO: 实现查询逻辑
        return Result.fail("功能待实现");
    }

    @Override
    public Result<Paging<MessageRecordDTO>> page(MessagePageParam param) {
        log.info("分页查询消息记录: {}", param);
        // TODO: 实现分页查询逻辑
        return Result.fail("功能待实现");
    }

    @Override
    public Result<List<MessageReceiverDTO>> getReceivers(String messageId) {
        log.info("查询消息接收人列表: {}", messageId);
        // TODO: 实现查询逻辑
        return Result.fail("功能待实现");
    }

    @Override
    public Result<List<MessageRecordDTO>> getUnread(String userId) {
        log.info("查询用户未读消息: {}", userId);
        // TODO: 实现查询逻辑
        return Result.fail("功能待实现");
    }

    @Override
    public Result<Integer> queryStatus(String messageId) {
        log.info("查询消息状态: {}", messageId);
        // TODO: 实现查询逻辑
        return Result.ok(0);
    }
}
