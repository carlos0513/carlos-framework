package com.carlos.msg.base.service;

import com.carlos.core.exception.ServiceException;
import com.carlos.msg.base.manager.MsgMessageTypeManager;
import com.carlos.msg.base.pojo.dto.MsgMessageTypeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 消息类型 业务
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MsgMessageTypeService {

    private final MsgMessageTypeManager messageTypeManager;

    public void addMsgMessageType(MsgMessageTypeDTO dto) {
        checkNameAndCode(dto.getTypeCode(), dto.getTypeName());
        boolean success = messageTypeManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            throw new ServiceException("消息类型新增失败");
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    private void checkNameAndCode(String typeCode, String typeName) {
        MsgMessageTypeDTO byName = messageTypeManager.getByName(typeName);
        if (byName != null) {
            throw new ServiceException("类型名称重复，请重新设置！");
        }
        MsgMessageTypeDTO byCode = messageTypeManager.getByCode(typeCode);
        if (byCode != null) {
            throw new ServiceException("类型编码重复，请重新设置！");
        }
    }

    public void deleteMsgMessageType(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = messageTypeManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    public void updateMsgMessageType(MsgMessageTypeDTO dto) {
        checkNameAndCode(dto.getTypeCode(), dto.getTypeName());
        boolean success = messageTypeManager.modify(dto);
        if (!success) {
            // 修改失败操作
            throw new ServiceException("消息类型编辑失败");
        }
        // 修改成功的后续操作
    }

}
