package com.carlos.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carlos.auth.entity.AuditOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AuditOperationMapper extends BaseMapper<AuditOperation> {

    @Select("SELECT * FROM audit_operation " +
            "WHERE user_id = #{userId} " +
            "AND create_time >= #{startTime} " +
            "ORDER BY create_time DESC")
    List<AuditOperation> selectUserOperations(@Param("userId") Long userId,
                                              @Param("startTime") LocalDateTime startTime);

    @Select("SELECT * FROM audit_operation " +
            "WHERE operation_type = #{operationType} " +
            "AND create_time >= #{startTime} " +
            "ORDER BY create_time DESC")
    List<AuditOperation> selectByOperationType(@Param("operationType") String operationType,
                                               @Param("startTime") LocalDateTime startTime);
}
