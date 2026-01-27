package com.carlos.docking.rzt.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserInfoResult extends RztResult {


    /**
     * 成员UserID。对应管理端的帐号
     */
    @JsonProperty("userid")
    private String userid;

    /**
     * 成员UserIDs。成员可能存在于多个区县下
     */
    @JsonProperty("userids")
    private List<String> userids;
    /**
     * 员名称
     */
    @JsonProperty("name")
    private String name;
    /**
     * 成员所属部门id列表。排序在第一个的部门id为主岗部门
     */
    @JsonProperty("department")
    private List<Integer> department;
    /**
     * 部门内的排序值，默认为0。数量必须和department一致，数值越大排序越前面。有效的值范围是[0, 2^32)。在姓名A-Z排序模式下，为置顶成员的排序值，不为0则默认置顶；在自由排序模式下，为所有成员排序值
     */
    @JsonProperty("order")
    private List<Integer> order;
    /**
     * 职位信息
     */
    @JsonProperty("position")
    private String position;
    /**
     * 跟随部门，支持多部门不同职位信息
     */
    @JsonProperty("positions")
    private List<String> positions;
    /**
     * 手机号码
     */
    @JsonProperty("mobile")
    private String mobile;
    /**
     * 加密手机号
     */
    @JsonProperty("mobileEncrypt")
    private String mobileEncrypt;
    /**
     * 手机号码隐藏状态。1表示隐藏，0表示不隐藏
     */
    @JsonProperty("hide_mobile")
    private Integer hideMobile;
    /**
     * 性别。0表示未定义，1表示男性，2表示女性
     */
    @JsonProperty("gender")
    private String gender;
    /**
     * 邮箱
     */
    @JsonProperty("email")
    private String email;
    /**
     * 表示在所在的部门内是否为上级。1表示为上级，0表示非上级。
     */
    @JsonProperty("is_leader_in_dept")
    private List<Integer> isLeaderInDept;
    /**
     * 头像url。获取到的用户头像如包含http://wwlocal.qq.com的域名，则需要在下载头像时，将该域名替换成单位正在使用的域名或地址 。可以通过在url中设置avatar_addr参数获取头像的可用链接。
     */
    @JsonProperty("avatar")
    private String avatar;
    /**
     * 座机
     */
    @JsonProperty("telephone")
    private String telephone;
    /**
     * 别名。长度不超过64个字。当别名作为登录字段时，只能由字母、数字及 .-@ 组成， .-@ 不能放在开头，且数据不能重复
     */
    @JsonProperty("english_name")
    private String englishName;
    /**
     * 扩展属性
     */
    @JsonProperty("extattr")
    private Extattr extattr;
    /**
     * 激活状态:激活状态: 1=已激活，2=已禁用，4=未激活
     */
    @JsonProperty("status")
    private Integer status;
    /**
     * 成员启用状态。1表示启用的成员，0表示被禁用
     */
    @JsonProperty("enable")
    private Integer enable;
    /**
     * 成员二维码图片地址
     */
    @JsonProperty("qr_code")
    private String qrCode;
    /**
     * 手机区号
     */
    @JsonProperty("country_code")
    private String countryCode;
    /**
     * 成员对外属性
     */
    @JsonProperty("external_profile")
    private ExternalProfile externalProfile;

    /**
     * ExtattrDTO
     */
    @NoArgsConstructor
    @Data
    public static class Extattr implements Serializable {

        /**
         * attrs
         */
        @JsonProperty("attrs")
        private List<Attrs> attrs;

        /**
         * AttrsDTO
         */
        @NoArgsConstructor
        @Data
        public static class Attrs implements Serializable {

            /**
             * type
             */
            @JsonProperty("type")
            private Integer type;
            /**
             * name
             */
            @JsonProperty("name")
            private String name;
            /**
             * value
             */
            @JsonProperty("value")
            private String value;
            /**
             * web
             */
            @JsonProperty("web")
            private Web web;

            /**
             * WebDTO
             */
            @NoArgsConstructor
            @Data
            public static class Web implements Serializable {

                /**
                 * url
                 */
                @JsonProperty("url")
                private String url;
                /**
                 * title
                 */
                @JsonProperty("title")
                private String title;
            }
        }
    }

    /**
     * ExternalProfileDTO
     */
    @NoArgsConstructor
    @Data
    public static class ExternalProfile implements Serializable {

        /**
         * 对外简称
         */
        @JsonProperty("external_corp_name")
        private String externalCorpName;
    }
}
