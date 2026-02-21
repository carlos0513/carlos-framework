package com.yunjin.org.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunjin.org.pojo.entity.OrgUserMessage;
import com.yunjin.org.pojo.vo.OrgUserMessageVO;
import com.yunjin.org.pojo.param.OrgUserMessagePageParam;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * <p>
 * 用户消息表 查询接口
 * </p>
 *
 * @author  yunjin
 * @date    2024-2-28 17:39:16
 */
@Mapper
public interface OrgUserMessageMapper extends BaseMapper<OrgUserMessage> {


}
