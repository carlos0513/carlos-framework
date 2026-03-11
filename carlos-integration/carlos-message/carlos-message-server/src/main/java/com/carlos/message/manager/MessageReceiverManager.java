package com.carlos.message.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.carlos.message.pojo.entity.MessageReceiver;

import java.util.List;

/**
 * <p>
 * 消息接收人 Manager 接口
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
public interface MessageReceiverManager extends IService<MessageReceiver> {

    /**
     * 根据消息ID查询接收人列表
     *
     * @param messageId 消息ID
     * @return 接收人列表
     */
    List<MessageReceiver> listByMessageId(String messageId);

    /**
     * 根据接收人ID和状态查询
     *
     * @param receiverId 接收人ID
     * @param status     状态
     * @return 接收人列表
     */
    List<MessageReceiver> listByReceiverAndStatus(String receiverId, Integer status);

    /**
     * 更新状态
     *
     * @param id     ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 增加失败次数
     *
     * @param id         ID
     * @param failReason 失败原因
     * @return 是否成功
     */
    boolean incrementFailCount(Long id, String failReason);
}
