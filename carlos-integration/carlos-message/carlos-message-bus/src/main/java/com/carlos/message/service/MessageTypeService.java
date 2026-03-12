package com.carlos.message.service;

import com.carlos.message.manager.MessageTypeManager;
import com.carlos.message.pojo.dto.MessageTypeDTO;
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
 * @date 2026年3月12日 上午11:17:06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageTypeService {

    private final MessageTypeManager typeManager;

    /**
     * 新增消息类型
     *
     * @param dto 消息类型数据
     * @author Carlos
     * @date 2026年3月12日 上午11:17:06
     */
    public void addMessageType(MessageTypeDTO dto) {
        boolean success = typeManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'MessageType' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除消息类型
     *
     * @param ids 消息类型id
     * @author Carlos
     * @date 2026年3月12日 上午11:17:06
     */
    public void deleteMessageType(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = typeManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改消息类型信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2026年3月12日 上午11:17:06
     */
    public void updateMessageType(MessageTypeDTO dto) {
        boolean success = typeManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'MessageType' data: id:{}", dto.getId());
    }

}
