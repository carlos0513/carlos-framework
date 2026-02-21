package com.carlos.msg.base.service;

import com.carlos.msg.base.manager.MsgMessageManager;
import com.carlos.msg.base.manager.MsgMessageTemplateManager;
import com.carlos.msg.base.pojo.dto.MsgMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 消息 业务
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MsgMessageService {

    private final MsgMessageManager messageManager;

    private final MsgMessageTemplateManager templateManager;

    public void addMsgMessage(MsgMessageDTO dto) {
        boolean success = messageManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    public void deleteMsgMessage(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = messageManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    public void updateMsgMessage(MsgMessageDTO dto) {
        boolean success = messageManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }

}
