package com.carlos.auth.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 密码匹配参数
 *
 * @author carlos
 * @date 2022/4/25 15:27
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PasswordMatchDTO {

    private String password;

    private String encodePassword;
}
