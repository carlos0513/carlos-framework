package com.carlos.auth.audit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carlos.auth.audit.pojo.entity.SecurityAlert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SecurityAlertMapper extends BaseMapper<SecurityAlert> {

    @Select("SELECT * FROM security_alert " +
            "WHERE user_id = #{userId} " +
            "ORDER BY create_time DESC")
    List<SecurityAlert> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM security_alert " +
            "WHERE handled = 0 " +
            "ORDER BY create_time DESC")
    List<SecurityAlert> selectUnhandled();
}