package com.carlos.msg.base.service;

import com.carlos.core.exception.ServiceException;
import com.carlos.msg.base.manager.MsgChannelConfigManager;
import com.carlos.msg.base.pojo.dto.MsgChannelConfigDTO;
import com.carlos.msg.sender.channel.MsgChannelFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 消息渠道配置 业务
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MsgChannelConfigService {

    private final MsgChannelConfigManager channelConfigManager;
    private final MsgChannelFactory channelFactory;

    public void addMsgChannelConfig(MsgChannelConfigDTO dto) {
        MsgChannelConfigDTO byCode = channelConfigManager.getByType(dto.getChannelType());
        if (byCode != null) {
            throw new ServiceException("渠道重复，请重新设置！");
        }
        boolean success = channelConfigManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            throw new ServiceException("消息渠道配置添加失败！");
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
        channelFactory.regiest(dto.getChannelType(), dto.getChannelConfig());
    }

    public void deleteMsgChannelConfig(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = channelConfigManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    public void updateMsgChannelConfig(MsgChannelConfigDTO dto) {
        // checkChannelConfig(dto);
        boolean success = channelConfigManager.modify(dto);
        if (!success) {
            // 修改失败操作
            throw new ServiceException("消息渠道配置更新失败！");
        }
        // 修改成功的后续操作
        channelFactory.reload(dto.getChannelType(), dto.getChannelConfig());
    }

    public void changeState(Set<Serializable> ids, Boolean enabled) {
        for (Serializable id : ids) {
            MsgChannelConfigDTO dto = channelConfigManager.getDtoById(id);
            boolean success = channelConfigManager.updateState(id, enabled);
            if (!success) {
                // 修改失败操作
                continue;
            }
            // 修改成功的后续操作
            channelFactory.disable(dto.getChannelType());
        }
    }
}
