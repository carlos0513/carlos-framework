package com.carlos.msg.base.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.msg.base.convert.MsgMessageConvert;
import com.carlos.msg.base.manager.MsgMessageManager;
import com.carlos.msg.base.mapper.MsgMessageMapper;
import com.carlos.msg.base.pojo.dto.MsgMessageDTO;
import com.carlos.msg.base.pojo.entity.MsgMessage;
import com.carlos.msg.base.pojo.param.MsgMessagePageParam;
import com.carlos.msg.base.pojo.vo.MsgMessageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 消息 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MsgMessageManagerImpl extends BaseServiceImpl<MsgMessageMapper, MsgMessage> implements MsgMessageManager {

    @Override
    public boolean add(MsgMessageDTO dto) {
        MsgMessage entity = MsgMessageConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'MsgMessage' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'MsgMessage' data: id:{}", entity.getId());
        }
        return true;
    }

    @Override
    public boolean delete(Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        boolean success = removeById(id);
        if (!success) {
            log.warn("Remove 'MsgMessage' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'MsgMessage' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(MsgMessageDTO dto) {
        MsgMessage entity = MsgMessageConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'MsgMessage' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'MsgMessage' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public MsgMessageDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        MsgMessage entity = getBaseMapper().selectById(id);
        return MsgMessageConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<MsgMessageVO> getPage(MsgMessagePageParam param) {
        LambdaQueryWrapper<MsgMessage> wrapper = queryWrapper();
        wrapper.select(
            MsgMessage::getId,
            MsgMessage::getTemplateId,
            MsgMessage::getSender,
            MsgMessage::getMessageType,
            MsgMessage::getMessageTitle,
            MsgMessage::getMessageContent,
            MsgMessage::getMessageRemark,
            MsgMessage::getSourceBusiness,
            MsgMessage::getSendUserId,
            MsgMessage::getSendUserName,
            MsgMessage::getFeedbackType,
            MsgMessage::getFeedbackContent,
            MsgMessage::getPriority,
            MsgMessage::getPushChannel,
            MsgMessage::getCreateBy,
            MsgMessage::getCreateTime
        );
        PageInfo<MsgMessage> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, MsgMessageConvert.INSTANCE::toVO);
    }

}
