package com.carlos.org.pojo.dto;

import lombok.Data;

/**
 * @author： lvbw
 * @date： 2025/12/08 15:36
 * @Description：
 */
@Data
public class CreateUserMessageDto {
    private String userName;
    private String phone;
    private String password;
    private String account;
}
