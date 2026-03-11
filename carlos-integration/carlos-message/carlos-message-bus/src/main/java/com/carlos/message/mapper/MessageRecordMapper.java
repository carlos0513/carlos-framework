package com.carlos.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carlos.message.pojo.entity.MessageRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 消息记录 Mapper 接口
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Mapper
public interface MessageRecordMapper extends BaseMapper<MessageRecord> {

    /**
     * 根据消息ID查询
     *
     * @param messageId 消息ID
     * @return 记录
     */
    @Select("SELECT * FROM message_record WHERE message_id = #{messageId} AND is_deleted = 0")
    MessageRecord selectByMessageId(@Param("messageId") String messageId);

    /**
     * 查询需要清理的历史记录
     *
     * @param days 天数
     * @return 记录ID列表
     */
    @Select("SELECT id FROM message_record WHERE create_time < DATE_SUB(NOW(), INTERVAL #{days} DAY)")
    List<Long> selectExpiredIds(@Param("days") int days);
}
