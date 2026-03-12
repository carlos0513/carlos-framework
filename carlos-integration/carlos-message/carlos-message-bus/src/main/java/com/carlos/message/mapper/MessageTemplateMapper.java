package com.carlos.message.mapper;

import com.carlos.message.pojo.entity.MessageTemplate;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 消息模板 查询接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月12日 上午11:17:05
 */
@Mapper
public interface MessageTemplateMapper extends MPJBaseMapper<MessageTemplate> {

    @Select("SELECT * FROM message_template WHERE template_code = #{templateCode} AND is_deleted = 0")
    MessageTemplate selectByCode(@Param("templateCode") String templateCode);
}
