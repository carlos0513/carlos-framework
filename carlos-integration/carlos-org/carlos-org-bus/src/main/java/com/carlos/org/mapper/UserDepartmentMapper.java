package com.carlos.org.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.pojo.dto.UserDepartmentDTO;
import com.carlos.org.pojo.entity.UserDepartment;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户部门 查询接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Mapper
public interface UserDepartmentMapper extends MPJBaseMapper<UserDepartment> {

    PageInfo<UserDepartmentDTO> listByDepartmentIdPage(@Param("page") Page<Object> page, @Param("id") String id,
                                                       @Param("keyword") String keyword);

    List<UserDepartmentDTO> listByDepartmentIdList(@Param("ids") List<String> ids);
    List<UserDepartmentDTO> isYxUser(@Param("userId") String userId);

    List<UserDepartmentDTO> isYxDept(@Param("deptId") String deptId);
}
