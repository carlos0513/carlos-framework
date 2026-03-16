package com.carlos.sample.json.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 示例用户类 - 演示各种 JSON 序列化特性
 * </p>
 *
 * @author carlos
 * @date 2026/3/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 用户ID - Long 类型，演示精度保持（自动转为 String）
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称 - 演示 @JsonProperty 别名
     */
    @JsonProperty("display_name")
    private String nickname;

    /**
     * 密码 - 演示 @JsonIgnore 不序列化
     */
    @JsonIgnore
    private String password;

    /**
     * 邮箱 - 可能为 null，演示 null 值处理
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 年龄 - 基本类型
     */
    private Integer age;

    /**
     * 余额 - BigDecimal 类型
     */
    private BigDecimal balance;

    /**
     * 状态 - 0-禁用 1-启用
     */
    private Integer status;

    /**
     * 角色列表
     */
    private List<String> roles;

    /**
     * 标签 - 可能为空列表，演示空集合处理
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> tags;

    /**
     * 创建时间 - Date 类型，演示日期格式化
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间 - LocalDateTime 类型
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 生日 - LocalDate 类型
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    /**
     * 简介 - 可能为 null
     */
    private String bio;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 是否激活
     */
    private Boolean active;

    /**
     * 积分 - Long 类型，演示精度保持
     */
    private Long points;
}
