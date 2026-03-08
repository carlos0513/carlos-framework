package com.carlos.auth.audit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carlos.auth.audit.pojo.entity.AuditLogin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AuditLoginMapper extends BaseMapper<AuditLogin> {

    @Select("SELECT * FROM audit_login_#{month} " +
        "WHERE user_id = #{userId} " +
        "ORDER BY create_time DESC")
    List<AuditLogin> selectUserLoginHistory(@Param("userId") Long userId,
                                            @Param("month") String month);

    @Insert({
        "INSERT INTO ${tableName} (user_id, username, event_type, ip_address, ",
        "location, user_agent, status, error_message, login_time, session_id, create_time) ",
        "VALUES (#{audit.userId}, #{audit.username}, #{audit.eventType}, #{audit.ipAddress}, ",
        "#{audit.location}, #{audit.userAgent}, #{audit.status}, #{audit.errorMessage}, ",
        "#{audit.loginTime}, #{audit.sessionId}, #{audit.createTime})"
    })
    int insertDynamic(@Param("audit") AuditLogin audit, @Param("tableName") String tableName);

    @Select("SELECT COUNT(*) FROM ${tableName} WHERE ip_address = #{ip} AND login_time >= #{startTime}")
    int countByIpInTimeRange(@Param("tableName") String tableName,
                             @Param("ip") String ip,
                             @Param("startTime") LocalDateTime startTime);

    @Select({
        "CREATE TABLE IF NOT EXISTS ${tableName} ( ",
        "  id BIGINT NOT NULL COMMENT '日志ID',",
        "  user_id BIGINT COMMENT '用户ID',",
        "  username VARCHAR(50) COMMENT '用户名',",
        "  client_id VARCHAR(100) COMMENT '客户端ID',",
        "  event_type VARCHAR(50) COMMENT '事件类型',",
        "  ip_address VARCHAR(45) COMMENT 'IP地址',",
        "  location VARCHAR(100) COMMENT '地理位置',",
        "  user_agent VARCHAR(500) COMMENT 'User-Agent',",
        "  status VARCHAR(20) COMMENT '状态',",
        "  error_message TEXT COMMENT '错误消息',",
        "  login_time DATETIME COMMENT '登录时间',",
        "  session_id VARCHAR(100) COMMENT '会话ID',",
        "  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',",
        "  PRIMARY KEY (id),",
        "  KEY idx_user_id (user_id),",
        "  KEY idx_ip_address (ip_address),",
        "  KEY idx_status (status),",
        "  KEY idx_create_time (create_time)",
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录审计日志'"
    })
    void createPartitionTable(@Param("tableName") String tableName);
}
