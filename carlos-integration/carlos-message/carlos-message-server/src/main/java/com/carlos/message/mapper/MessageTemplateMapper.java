package com.carlos.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carlos.message.pojo.entity.MessageTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 消息模板 Mapper 接口
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Mapper
public interface MessageTemplateMapper extends BaseMapper<MessageTemplate> {

    @Select("SELECT * FROM message_template WHERE template_code = #{templateCode} AND is_deleted = 0")
    MessageTemplate selectByCode(@Param("templateCode") String templateCode);
}
