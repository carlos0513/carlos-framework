package com.carlos.system.news.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carlos.system.news.pojo.entity.SysNews;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统-通知公告 查询接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
@Mapper
public interface SysNewsMapper extends BaseMapper<SysNews> {


}
