package com.carlos.org.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.pojo.dto.UserDTO;
import com.carlos.org.pojo.dto.UserExportDTO;
import com.carlos.org.pojo.entity.User;
import com.carlos.org.pojo.param.UserPageParam;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统用户 查询接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Mapper
public interface UserMapper extends MPJBaseMapper<User> {


    PageInfo<User> pageAsAuthLimit(Page page,@Param("param") UserPageParam param);

    List<UserExportDTO> listAsAuthLimit(@Param("param") UserPageParam param);

    List<UserDTO> listByDeptIds(@Param("deptIds") Set<String> deptIds);
}
