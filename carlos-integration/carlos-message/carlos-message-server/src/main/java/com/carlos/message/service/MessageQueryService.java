package com.carlos.message.service;

import com.carlos.core.pojo.response.Paging;
import com.carlos.core.response.Result;
import com.carlos.message.pojo.dto.MessageRecordDTO;
import com.carlos.message.pojo.dto.MessageReceiverDTO;

import java.util.List;

/**
 * <p>
 * 消息查询服务
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
public interface MessageQueryService {

    /**
     * 根据消息ID查询详情
     *
     * @param messageId 消息ID
     * @return 消息记录
     */
    Result<MessageRecordDTO> getById(String messageId);

    /**
     * 分页查询消息记录
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    Result<Paging<MessageRecordDTO>> page(int page, int size);

    /**
     * 查询消息接收人列表
     *
     * @param messageId 消息ID
     * @return 接收人列表
     */
    Result<List<MessageReceiverDTO>> getReceivers(String messageId);

    /**
     * 查询用户的未读消息
     *
     * @param userId 用户ID
     * @return 未读消息列表
     */
    Result<List<MessageRecordDTO>> getUnread(String userId);

    /**
     * 查询消息状态
     *
     * @param messageId 消息ID
     * @return 消息状态
     */
    Result<Integer> queryStatus(String messageId);
}
