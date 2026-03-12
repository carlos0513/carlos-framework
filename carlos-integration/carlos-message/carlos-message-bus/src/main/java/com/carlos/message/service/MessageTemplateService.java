package com.carlos.message.service;

import com.carlos.message.manager.MessageTemplateManager;
import com.carlos.message.pojo.dto.MessageTemplateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 消息模板 业务
 * </p>
 *
 * @author Carlos
 * @date 2026年3月12日 上午11:17:06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageTemplateService {

    private final MessageTemplateManager templateManager;

    /**
     * 新增消息模板
     *
     * @param dto 消息模板数据
     * @author Carlos
     * @date 2026年3月12日 上午11:17:06
     */
    public void addMessageTemplate(MessageTemplateDTO dto) {
        boolean success = templateManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'MessageTemplate' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除消息模板
     *
     * @param ids 消息模板id
     * @author Carlos
     * @date 2026年3月12日 上午11:17:06
     */
    public void deleteMessageTemplate(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = templateManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改消息模板信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2026年3月12日 上午11:17:06
     */
    public void updateMessageTemplate(MessageTemplateDTO dto) {
        boolean success = templateManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'MessageTemplate' data: id:{}", dto.getId());
    }

}
