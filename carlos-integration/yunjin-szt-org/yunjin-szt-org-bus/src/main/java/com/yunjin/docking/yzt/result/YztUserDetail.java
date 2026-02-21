package com.yunjin.docking.yzt.result;

import lombok.Data;

import java.util.List;

/**
 * 云政通用户详情
 */
@Data
public class YztUserDetail {

    private Integer errcode;            // 返回码

    private String errmsg;              // 对返回码的文本描述内容

    private String userid;              // 成员UserID

    private String name;                // 成员名称

    private String mobile;              // 手机号码

    private Integer hide_mobile;                // 手机号码隐藏状态。1表示隐藏，0表示不隐藏

    private List<Integer> department;           // 成员所属部门id列表。排序在第一个的部门id为主岗部门

    private List<Integer> order;                // 部门内的排序值，默认为0

    private String position;                    // 职位信息

    private List<String> positions;             // 跟随部门，支持多部门不同职位信息

    private String gender;                      // 性别。0表示未定义，1表示男性，2表示女性

    private String email;                       // 邮箱

    private String biz_mail;                    // 单位邮箱

    private List<String> biz_mail_alias;        // 邮箱别名

    private List<Integer> is_leader_in_dept;    // 表示在所在的部门内是否为上级。1表示为上级，0表示非上级。

    private List<String> direct_leader;         // 直属上级，数组，当前只支持1个直属上级，填 userid

    private String avatar;                      // 头像url。

    private String telephone;                   // 座机

    private Object extattr;                     // 扩展属性

    private Integer status;                     // 激活状态:激活状态: 1=已激活，2=已禁用，4=未激活

    private Integer enable;                     // 成员启用状态。1表示启用的成员，0表示被禁用

    private String qr_code;                     // 成员二维码图片地址

    private String country_code;                // 手机区号

    private Object external_profile;            // 成员对外属性

}
