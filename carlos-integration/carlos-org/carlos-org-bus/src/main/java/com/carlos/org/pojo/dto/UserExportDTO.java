package com.carlos.org.pojo.dto;

import com.carlos.org.pojo.enums.UserStateEnum;
import lombok.Data;

/**
 * @author： lvbw
 * @date： 2025/10/23 16:05
 * @Description：
 */
@Data
public class UserExportDTO {
    private Long id;
    private String account;
    private String realname;
    private String identify;
    private String phone;
    private String address;
    private String gender;
    private String email;
    private String description;
    private UserStateEnum stateEnum;
    private Long createBy;
    private String createTime;
    private Long updateBy;
    private String updateTime;
    private Integer sort;
    private String deptName;
    private String regionName;
    private String roleNames;
}
