package com.carlos.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carlos.message.pojo.entity.MessageReceiver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 消息接收人 Mapper 接口
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Mapper
public interface MessageReceiverMapper extends BaseMapper<MessageReceiver> {

    /**
     * 根据消息ID查询接收人列表
     *
     * @param messageId 消息ID
     * @return 接收人列表
     */
    @Select("SELECT * FROM message_receiver WHERE message_id = #{messageId}")
    List<MessageReceiver> selectByMessageId(@Param("messageId") String messageId);

    /**
     * 根据接收人ID和状态查询
     *
     * @param receiverId 接收人ID
     * @param status     状态
     * @return 接收人列表
     */
    @Select("SELECT * FROM message_receiver WHERE receiver_id = #{receiverId} AND status = #{status} ORDER BY create_time DESC")
    List<MessageReceiver> selectByReceiverAndStatus(@Param("receiverId") String receiverId, @Param("status") Integer status);

    /**
     * 更新状态
     *
     * @param id     ID
     * @param status 状态
     * @return 影响行数
     */
    @Update("UPDATE message_receiver SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 增加失败次数
     *
     * @param id         ID
     * @param failReason 失败原因
     * @return 影响行数
     */
    @Update("UPDATE message_receiver SET fail_count = fail_count + 1, fail_reason = #{failReason}, update_time = NOW() WHERE id = #{id}")
    int incrementFailCount(@Param("id") Long id, @Param("failReason") String failReason);
}
