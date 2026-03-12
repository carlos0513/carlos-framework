package com.carlos.message.mapper;

import com.carlos.message.pojo.entity.MessageRecord;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 消息记录表 查询接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月12日 上午11:17:05
 */
@Mapper
public interface MessageRecordMapper extends MPJBaseMapper<MessageRecord> {

    @Select("SELECT * FROM message_record WHERE message_id = #{messageId} AND is_deleted = 0")
    MessageRecord selectByMessageId(@Param("messageId") String messageId);

    @Select("SELECT id FROM message_record WHERE create_time < DATE_SUB(NOW(), INTERVAL #{days} DAY)")
    List<Long> selectExpiredIds(@Param("days") int days);
}
