package com.yunjin.org.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunjin.org.pojo.entity.Label;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 标签 查询接口
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-23 12:31:52
 */
@Mapper
public interface LabelMapper extends BaseMapper<Label> {

    List<Label> listByIdsIncludeDeleted(@Param(value = "ids")List<String> ids);

    Label getByIdIncludeDeleted(@Param(value = "id") String id);
}
