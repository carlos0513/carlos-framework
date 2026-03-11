package com.carlos.message.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.carlos.message.pojo.entity.MessageRecord;

import java.util.List;

/**
 * <p>
 * 消息记录 Manager 接口
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
public interface MessageRecordManager extends IService<MessageRecord> {

    /**
     * 根据消息ID查询
     *
     * @param messageId 消息ID
     * @return 记录
     */
    MessageRecord getByMessageId(String messageId);

    /**
     * 查询需要清理的历史记录ID
     *
     * @param days 天数
     * @return ID列表
     */
    List<Long> getExpiredIds(int days);

    /**
     * 更新统计信息
     *
     * @param messageId 消息ID
     * @param successCount 成功数
     * @param failCount 失败数
     * @return 是否成功
     */
    boolean updateStatistics(String messageId, int successCount, int failCount);
}
