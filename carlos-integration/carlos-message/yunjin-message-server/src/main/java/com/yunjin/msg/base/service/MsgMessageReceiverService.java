package com.carlos.msg.base.service;

import com.carlos.msg.base.manager.MsgMessageReceiverManager;
import com.carlos.msg.base.pojo.dto.MsgMessageReceiverDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 消息接受者 业务
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MsgMessageReceiverService {

    private final MsgMessageReceiverManager messageReceiverManager;

    public void addMsgMessageReceiver(MsgMessageReceiverDTO dto) {
        boolean success = messageReceiverManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    public void deleteMsgMessageReceiver(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = messageReceiverManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    public void updateMsgMessageReceiver(MsgMessageReceiverDTO dto) {
        boolean success = messageReceiverManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }

}
