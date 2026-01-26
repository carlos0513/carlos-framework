package com.yunjin.docking.linkage.result;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 翻译返回结果
 * </p>
 *
 * @author Carlos
 * @date 2022/3/8 12:18
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class BigLinkAgeUserInfo {


    /**
     * loginid
     */
    private String loginid;
    /**
     * tel
     */
    private String tel;
    /**
     * username
     */
    private String username;
    /**
     * ssqx
     */
    private String ssqx;
    /**
     * ssjd
     */
    private String ssjd;
    /**
     * sssq
     */
    private String sssq;
    /**
     * sswg
     */
    private String sswg;
    /**
     * personType
     */
    private String personType;
    /**
     * orgId
     */
    private String orgId;
    /**
     * orgLevel
     */
    private Integer orgLevel;
    /**
     * orgName
     */
    private String orgName;
    /**
     * orgType
     */
    private String orgType;
    /**
     * areaCode
     */
    private String areaCode;
    /**
     * areaName
     */
    private String areaName;
    /**
     * areaNamePath
     */
    private String areaNamePath;
}
