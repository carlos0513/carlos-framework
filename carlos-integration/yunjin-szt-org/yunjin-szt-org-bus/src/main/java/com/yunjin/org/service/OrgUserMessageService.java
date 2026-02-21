package com.yunjin.org.service;

import com.yunjin.core.param.ParamIdSet;
import com.yunjin.org.pojo.dto.OrgUserMessageDTO;
import com.yunjin.org.pojo.enums.UserMessageType;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户消息表 业务接口
 * </p>
 *
 * @author  yunjin
 * @date    2024-2-28 17:39:16
 */
public interface OrgUserMessageService{

    /**
     * 新增用户消息表
     *
     * @param dto 用户消息表数据
     * @author  yunjin
     * @date    2024-2-28 17:39:16
     */
    void addOrgUserMessage(OrgUserMessageDTO dto);

    boolean isRepeat(OrgUserMessageDTO dto);

    void sendSMS(String id);

    void sendThirdMessage();

    /**
     * 删除用户消息表
     *
     * @param ids 用户消息表id
     * @author  yunjin
     * @date    2024-2-28 17:39:16
     */
    void deleteOrgUserMessage(Set<String> ids);

    /**
     * 修改用户消息表信息
     *
     * @param dto 对象信息
     * @author  yunjin
     * @date    2024-2-28 17:39:16
     */
    void updateOrgUserMessage(OrgUserMessageDTO dto);

    List<OrgUserMessageDTO> listByType(UserMessageType type, String userId);

    OrgUserMessageDTO getMessageById(String id);

    void messagesRead(ParamIdSet<String> ids);

    void updateMessages();

    boolean isSmsEnabled();
}
