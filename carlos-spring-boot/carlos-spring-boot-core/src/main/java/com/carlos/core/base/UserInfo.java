package com.carlos.core.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 字典字段枚举
 * </p>
 *
 * @author carlos
 * @date 2020/4/12 2:58
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {

    /**
     * 用户id
     */
    private Serializable id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 真实姓名
     */
    private String phone;

    /**
     * 真实姓名
     */
    private String email;
}
