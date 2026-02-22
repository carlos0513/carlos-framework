package com.carlos.msg.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carlos.msg.base.pojo.entity.MsgMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 消息 查询接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Mapper
public interface MsgMessageMapper extends BaseMapper<MsgMessage> {


}
