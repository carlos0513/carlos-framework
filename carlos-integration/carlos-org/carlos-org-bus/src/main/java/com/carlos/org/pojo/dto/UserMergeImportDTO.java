package com.carlos.org.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户导入 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2023-5-27 12:52:09
 */
@Data
@Accessors(chain = true)
public class UserMergeImportDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;
    private String account;
    private String account1;
    private String realname;
    private String realname1;
    private String identify;
    private String identify1;
    private String phone;
    private String phone1;
    private String role;
    private String role1;
    private String department;
    private String regionCode;


}
