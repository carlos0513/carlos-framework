package com.carlos.message.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.message.convert.MessageRecordConvert;
import com.carlos.message.manager.MessageRecordManager;
import com.carlos.message.mapper.MessageRecordMapper;
import com.carlos.message.pojo.dto.MessageRecordDTO;
import com.carlos.message.pojo.entity.MessageRecord;
import com.carlos.message.pojo.param.MessageRecordPageParam;
import com.carlos.message.pojo.vo.MessageRecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 消息记录表 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年3月12日 上午11:17:05
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MessageRecordManagerImpl extends BaseServiceImpl<MessageRecordMapper, MessageRecord> implements MessageRecordManager {

    @Override
    public boolean add(MessageRecordDTO dto) {
        MessageRecord entity = MessageRecordConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'MessageRecord' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'MessageRecord' data: id:{}", entity.getId());
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
            log.warn("Remove 'MessageRecord' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'MessageRecord' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(MessageRecordDTO dto) {
        MessageRecord entity = MessageRecordConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'MessageRecord' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'MessageRecord' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public MessageRecordDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        MessageRecord entity = getBaseMapper().selectById(id);
        return MessageRecordConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<MessageRecordVO> getPage(MessageRecordPageParam param) {
        LambdaQueryWrapper<MessageRecord> wrapper = queryWrapper();
        wrapper.select(

            MessageRecord::getId,
            MessageRecord::getMessageId,
            MessageRecord::getTemplateCode,
            MessageRecord::getTemplateParams,
            MessageRecord::getMessageType,
            MessageRecord::getMessageTitle,
            MessageRecord::getMessageContent,
            MessageRecord::getSenderId,
            MessageRecord::getSenderName,
            MessageRecord::getSenderSystem,
            MessageRecord::getFeedbackType,
            MessageRecord::getFeedbackContent,
            MessageRecord::getPriority,
            MessageRecord::getValidUntil,
            MessageRecord::getTotalCount,
            MessageRecord::getSuccessCount,
            MessageRecord::getFailCount,
            MessageRecord::getExtras,
            MessageRecord::getCreateBy,
            MessageRecord::getCreateTime,
            MessageRecord::getUpdateBy,
            MessageRecord::getUpdateTime
        );
        PageInfo<MessageRecord> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, MessageRecordConvert.INSTANCE::toVO);
    }

    @Override
    public MessageRecord getByMessageId(String messageId) {
        LambdaQueryWrapper<MessageRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MessageRecord::getMessageId, messageId);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<Long> getExpiredIds(int days) {
        LocalDateTime expireTime = LocalDateTime.now().minus(days, ChronoUnit.DAYS);
        LambdaQueryWrapper<MessageRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(MessageRecord::getId)
            .lt(MessageRecord::getCreateTime, expireTime);
        return baseMapper.selectList(wrapper).stream()
            .map(MessageRecord::getId)
            .collect(Collectors.toList());
    }

    @Override
    public boolean updateStatistics(String messageId, int successCount, int failCount) {
        MessageRecord record = getByMessageId(messageId);
        if (record == null) {
            return false;
        }
        record.setSuccessCount(successCount);
        record.setFailCount(failCount);
        return updateById(record);
    }
}
