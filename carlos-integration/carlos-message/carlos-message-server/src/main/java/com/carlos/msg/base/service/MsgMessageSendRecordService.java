package com.carlos.msg.base.service;

import com.carlos.msg.base.manager.MsgMessageSendRecordManager;
import com.carlos.msg.base.pojo.dto.MsgMessageSendRecordDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 消息发送记录 业务
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MsgMessageSendRecordService {

    private final MsgMessageSendRecordManager messageSendRecordManager;

    public void addMsgMessageSendRecord(MsgMessageSendRecordDTO dto) {
        boolean success = messageSendRecordManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    public void deleteMsgMessageSendRecord(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = messageSendRecordManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    public void updateMsgMessageSendRecord(MsgMessageSendRecordDTO dto) {
        boolean success = messageSendRecordManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }

}
