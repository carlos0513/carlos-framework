package com.yunjin.org.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunjin.core.base.UserInfo;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.core.response.Result;
import com.yunjin.org.UserUtil;
import com.yunjin.org.convert.OrgUserMessageConvert;
import com.yunjin.org.manager.OrgUserMessageManager;
import com.yunjin.org.manager.UserManager;
import com.yunjin.org.pojo.dto.OrgUserMessageDTO;
import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.pojo.entity.OrgUserMessage;
import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.enums.UserMessageType;
import com.yunjin.org.service.OrgUserMessageService;
import com.yunjin.org.service.SmsMessageService;
import com.yunjin.org.service.UserService;
import com.yunjin.system.api.ApiSysNews;
import com.yunjin.system.api.ApiSystemConfig;
import com.yunjin.system.pojo.ao.SysConfigAO;
import com.yunjin.system.pojo.ao.SysNewsAO;
import com.yunjin.system.pojo.ao.SysNewsDetailAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户消息表 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2024-2-28 17:39:16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgUserMessageServiceImpl implements OrgUserMessageService {
    private final UserService userService;

    private final OrgUserMessageManager userMessageManager;

    private final SmsMessageService smsMessageService;

    private final ApiSystemConfig apiSystemConfig;

    private final String SMS_STATUS_SWITCH = "sms_status_switch";

    @Override
    @Transactional
    public void addOrgUserMessage(OrgUserMessageDTO dto) {
        log.info("---addOrgUserMessage---");
        log.info("title --> {}", dto.getTitle());
        log.info("content --> {}", dto.getContent());
        log.info("receiver --> {}", dto.getUserId());
        log.info("Creator --> {}", dto.getCreator());
        log.info("messageType --> {}", dto.getTitle());
        log.info("messageId --> {}", dto.getMessageId());
        log.info("deptCode --> {}", dto.getDeptCode());
        log.info("smsMessageEnum --> {}", dto.getSmsMessageEnum());
        // 去掉重复校验（因为同一个业务可能有退回再提交，而且前一条消息并没有查看，就会导致消息没有发送）
//        if (isRepeat(dto)) {
//            return;
//        }
        boolean success = userMessageManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        if (dto.getType() != UserMessageType.MESSAGE && isSmsEnabled()) {
            // 这里需要发短信 或者对接第三方消息
            sendSMS(dto.getId());
            sendThirdMessage();
        }
    }

    @Override
    public boolean isRepeat(OrgUserMessageDTO dto) {
        QueryWrapper<OrgUserMessage> eq = new QueryWrapper<OrgUserMessage>()
                .eq("user_id", dto.getUserId())
                .eq("message_id", dto.getMessageId())
                .eq("type", dto.getType())
                .eq("status", dto.getStatus())
                .eq("title", dto.getTitle());
        return userMessageManager.count(eq) > 0;
    }

    @Override
    public void sendSMS(String id) {
        OrgUserMessage byId = userMessageManager.getById(id);
        if (byId.getSmsMessageEnum() != null) {
            String phone = userService.getPhoneByUserId(byId.getUserId());
            if (StrUtil.isBlank(phone)) {
                return;
            }
            String templateCode = byId.getSmsMessageEnum().getTemplateCode();
            LinkedHashMap<String, String> map = generateMessageMap(byId);
            smsMessageService.sendSms(phone, templateCode, map);
        }
    }

    @Override
    public void sendThirdMessage() {
        log.info("准备给第三方发消息");
    }

    private LinkedHashMap<String, String> generateMessageMap(OrgUserMessage byId) {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        String content, taskName, feedback, datetime, state, applyName;
        switch (byId.getSmsMessageEnum()) {
            case FEEDBACK_RESULT:
                content = byId.getContent().split("【")[1].split("】")[0];
                // 意见可能为空
                if (byId.getContent().split("【")[2].equals("】")) {
                    feedback = "已完成处理";
                } else {
                    feedback = byId.getContent().split("【")[2].split("】")[0];
                }
                result.put("content", content);
                result.put("feedback", feedback);
                break;
            case FEEDBACK_COMMIT:
                content = byId.getContent().split("【")[1].split("】")[0];
                result.put("content", content);
                break;
            case TASK_CORRECTION:
                taskName = byId.getContent().split("【")[1].split("】")[0];
                datetime = byId.getContent().split("【")[2].split("】")[0];
                // 意见可能为空
                if (byId.getContent().split("【")[3].equals("】")) {
                    feedback = "已完成处理";
                } else {
                    feedback = byId.getContent().split("【")[3].split("】")[0];
                }
                result.put("taskName", taskName);
                result.put("datetime", datetime);
                result.put("feedback", feedback);
                break;
            case TASK_TODO:
                taskName = byId.getContent().split("【")[1].split("】")[0];
                taskName = "【" + taskName + "】";
                state = byId.getContent().split("【")[1].split("】")[1];
                result.put("taskName", taskName);
                result.put("state", state);
                break;
            case APPLY_STATE:
                applyName = byId.getContent().split("【")[1].split("】")[0];
                state = byId.getContent().split("【")[1].split("】")[1];
                result.put("applyName", applyName);
                result.put("state", state);
                break;
            case TASK_RESULT:
                taskName = byId.getContent().split("【")[1].split("】")[0];
                // 审核意见可能为空
                result.put("taskName", taskName);
                if (byId.getContent().split("【")[2].equals("】")) {
                    content = "已完成处理";
                } else {
                    content = byId.getContent().split("【")[2].split("】")[0];
                }
                result.put("content", content);
                break;
            case TASK_OPERATE:
                taskName = byId.getContent().split("【")[1].split("】")[0];
                result.put("taskName", taskName);
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public void deleteOrgUserMessage(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = userMessageManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    @Override
    public void updateOrgUserMessage(OrgUserMessageDTO dto) {
        boolean success = userMessageManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }

    @Override
    public void updateMessages() {
        String userId = UserUtil.getId();
        ApiSysNews apiSysNews = SpringUtil.getBean(ApiSysNews.class);
        Result<List<SysNewsAO>> apiSysNewsResult = apiSysNews.list();
        if (Boolean.FALSE.equals(apiSysNewsResult.getSuccess())) {
            log.error("Api request failed, message: {}, detail message:{}", apiSysNewsResult.getMessage(), apiSysNewsResult.getStack());
            throw new ServiceException(apiSysNewsResult.getMessage());
        }
        Set<String> noticeIds = listByType(UserMessageType.MESSAGE, userId).stream().map(OrgUserMessageDTO::getMessageId).collect(Collectors.toSet());
        Set<String> allNoticeIds = apiSysNewsResult.getData().stream().map(
                SysNewsAO::getId).collect(Collectors.toSet());
//        Set<String> difference = apiSysNewsResult.getData().stream().filter(sysNewsAO ->
//                sysNewsAO.getType().getCode().equals(UserMessageType.MESSAGE.getCode())
//                        && userId.equals(sysNewsAO.getSource())).map(SysNewsAO::getId).collect(Collectors.toSet());
        allNoticeIds.removeAll(noticeIds);
        List<SysNewsAO> needToAddSysNews = apiSysNewsResult.getData().stream().filter(sysNewsAO -> allNoticeIds.contains(sysNewsAO.getId())).collect(Collectors.toList());
        needToAddSysNews.forEach(sysNewsAO -> {
            OrgUserMessageDTO orgUserMessageDTO = new OrgUserMessageDTO();
            orgUserMessageDTO.setMessageId(sysNewsAO.getId());
            orgUserMessageDTO.setTitle(sysNewsAO.getTitle());
            orgUserMessageDTO.setContent(sysNewsAO.getContent());
            orgUserMessageDTO.setStatus(UserMessageStatus.UNREAD); // 新增时候都是未读
            orgUserMessageDTO.setSendDate(sysNewsAO.getSendDate());
            orgUserMessageDTO.setUserId(userId);
            UserInfo userInfo = userService.getUserInfo(sysNewsAO.getCreateBy());
            if (userInfo != null) {
                orgUserMessageDTO.setCreator(userInfo.getRealName());
            }
            orgUserMessageDTO.setType(UserMessageType.MESSAGE);
            this.addOrgUserMessage(orgUserMessageDTO);
        });
    }

    @Override
    public boolean isSmsEnabled() {
        Result<SysConfigAO> result = null;
        try {
            result = apiSystemConfig.getByCode(SMS_STATUS_SWITCH);
        } catch (Exception e) {
            log.info("Api request failed, message: {}, detail message:{}", e.getMessage(), e.getStackTrace());
            return false;
        }
        if (!result.getSuccess()) {
            return false;
        }
        return result.getData().getConfigValue().equals("true");
    }


    @Override
    public List<OrgUserMessageDTO> listByType(UserMessageType type, String userId) {
        List<OrgUserMessage> orgUserMessages = userMessageManager.listByType(type, userId);
        return OrgUserMessageConvert.INSTANCE.toDTO(orgUserMessages);
    }

    @Override
    public OrgUserMessageDTO getMessageById(String id) {
        OrgUserMessageDTO message = userMessageManager.getDtoById(id);
        if (message.getStatus() != UserMessageStatus.READ) {
            userMessageManager.updateState(message.getId(), UserMessageStatus.READ);
        }
        // 系统公告单独处理
        if (message.getType() == UserMessageType.MESSAGE) {
            ApiSysNews apiSysNews = SpringUtil.getBean(ApiSysNews.class);
            Result<SysNewsDetailAO> apiSysNewsResult = apiSysNews.getById(message.getMessageId());
            if (Boolean.FALSE.equals(apiSysNewsResult.getSuccess())) {
                throw new ServiceException(apiSysNewsResult.getMessage());
            }
            message.setImages(apiSysNewsResult.getData().getImages());
        }
        String creator = message.getCreator();
        if (ObjUtil.isNotNull(creator)) {
            UserManager userManager = SpringUtil.getBean(UserManager.class);
            UserDTO user = userManager.getDtoById(creator);
            if (user != null) {
                message.setCreator(user.getRealname());
            }
        }
        return message;
    }

    @Override
    public void messagesRead(ParamIdSet<String> ids) {
        List<OrgUserMessage> orgUserMessages = userMessageManager.listByIds(Arrays.asList(ids.getIds().toArray(new String[0])));
        orgUserMessages.forEach(t -> t.setStatus(UserMessageStatus.READ));
        userMessageManager.saveOrUpdateBatch(orgUserMessages);
    }
}
