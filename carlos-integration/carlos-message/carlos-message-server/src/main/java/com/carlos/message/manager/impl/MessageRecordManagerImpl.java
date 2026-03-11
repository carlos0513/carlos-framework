package com.carlos.message.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carlos.message.manager.MessageRecordManager;
import com.carlos.message.mapper.MessageRecordMapper;
import com.carlos.message.pojo.entity.MessageRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 消息记录 Manager 实现
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Slf4j
@Component
public class MessageRecordManagerImpl extends ServiceImpl<MessageRecordMapper, MessageRecord> implements MessageRecordManager {

    @Override
    public MessageRecord getByMessageId(String messageId) {
        return baseMapper.selectByMessageId(messageId);
    }

    @Override
    public List<Long> getExpiredIds(int days) {
        return baseMapper.selectExpiredIds(days);
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
