package com.yunjin.org.apiimpl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiOrgUserMessage;
import com.yunjin.org.convert.OrgUserMessageConvert;
import com.yunjin.org.pojo.ao.OrgUserMessageDetailAO;
import com.yunjin.org.pojo.dto.OrgUserMessageDTO;
import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.enums.UserMessageType;
import com.yunjin.org.pojo.param.ApiOrgUserMessageCreateParam;
import com.yunjin.org.service.OrgUserMessageService;
import com.yunjin.org.service.UserService;
import com.yunjin.system.api.ApiSysNews;
import com.yunjin.system.pojo.ao.SysNewsDetailAO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户消息表 api接口
 * </p>
 *
 * @author yunjin
 * @date 2024-2-28 17:39:16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/user/message")
@Tag(name = "用户消息表Feign接口", hidden = true)
public class OrgUserMessageAPI implements ApiOrgUserMessage {
    private final OrgUserMessageService orgUserMessageService;
    private final UserService userService;

    @Override
    @GetMapping("getMessageOne")
    public Result<OrgUserMessageDetailAO> getMessageOne(String id) {
        OrgUserMessageDTO messageById = orgUserMessageService.getMessageById(id);
        if (messageById == null) {
            throw new ServiceException("消息不存在！");
        }
        // 查看之后需要更为状态为已读
        messageById.setStatus(UserMessageStatus.READ);
        orgUserMessageService.updateOrgUserMessage(messageById);
        OrgUserMessageDetailAO messageDetailAO = OrgUserMessageConvert.INSTANCE.toAO(messageById);
        // 系统公告单独处理
        UserMessageType messageType = messageById.getType();
        switch (messageType) {
            case MESSAGE:
                ApiSysNews apiSysNews = SpringUtil.getBean(ApiSysNews.class);
                Result<SysNewsDetailAO> apiSysNewsResult = apiSysNews.getById(messageById.getMessageId());
                if (Boolean.FALSE.equals(apiSysNewsResult.getSuccess())) {
                    throw new ServiceException(apiSysNewsResult.getMessage());
                }
                messageDetailAO.setImages(apiSysNewsResult.getData().getImages());
                BeanUtil.copyProperties(messageById, messageDetailAO);
                messageDetailAO.setCreatorName(messageDetailAO.getCreator());
                break;
            case COMPLAINTS:
            case COLLECTION:
            case CHECK:
            case APPLY:
                BeanUtil.copyProperties(messageById, messageDetailAO);
                messageDetailAO.setCreatorName(
                        userService.getUserById(messageDetailAO.getCreator(), false).getRealname());
                break;
            case PROVINCIAL:
            case CITY:
            case THIRD:
                break;
        }
        //if (messageById.getType() == UserMessageType.MESSAGE) {
        //    ApiSysNews apiSysNews = SpringUtil.getBean(ApiSysNews.class);
        //    Result<SysNewsDetailAO> apiSysNewsResult = apiSysNews.getById(messageById.getMessageId());
        //    if (Boolean.FALSE.equals(apiSysNewsResult.getSuccess())) {
        //        throw new ServiceException(apiSysNewsResult.getMessage());
        //    }
        //    messageDetailAO.setImages(apiSysNewsResult.getData().getImages());
        //    BeanUtil.copyProperties(messageById, messageDetailAO);
        //    messageDetailAO.setCreatorName(messageDetailAO.getCreator());
        //} else {
        //    BeanUtil.copyProperties(messageById, messageDetailAO);
        //    messageDetailAO.setCreatorName(
        //            userService.getUserById(messageDetailAO.getCreator(), false).getRealname());
        //}
        return Result.ok(messageDetailAO);
    }

    @Override
    @PostMapping("messagesRead")
    public void messagesRead(ParamIdSet<String> ids) {
        orgUserMessageService.messagesRead(ids);
    }

    @Override
    @PostMapping("addMessage")
    public void addMessage(ApiOrgUserMessageCreateParam param) {
        OrgUserMessageDTO dto = OrgUserMessageConvert.INSTANCE.toDTO(param);
        // 微服务传输时候 序列化出错，选择org添加时间
        dto.setSendDate(LocalDateTime.now());
        orgUserMessageService.addOrgUserMessage(dto);
    }

    @Override
    @GetMapping("smsMessage")
    public void smsMessage(String id) {
        orgUserMessageService.sendSMS(id);
    }
}
