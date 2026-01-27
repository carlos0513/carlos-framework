package com.carlos.docking.rzt.organization;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *   蓉政通用户信息
 * </p>
 *
 * @author Carlos
 * @date 2024-12-13 15:48
 */
@Data
@Accessors(chain = true)
public class RztUserInfo {
    /**
     * 用户id
     */
    private String id;
    /**
     * 显示名
     */
    private String displayName;
    /**
     * 职务
     */
    private List<String> office;
    /**
     * 用户名，与蓉政通内的userid对应
     */
    private String userName;
    /**
     * 用户主身份类型
     */
    private String subjectType;
    /**
     * 用户状态，对应身份status字段，true:启用，false:冻结。
     */
    private Boolean active;

    /**
     * 用户邮箱
     */
    private List<Email> emails;
    /**
     * 用户邮箱
     */
    private String deleted;
    /**
     * scim的标准，用户的元数据。
     */
    private Meta meta;
    /**
     * 用户所属组织机构id
     */
    private List<String> organization;
    /**
     * scim的schema，返回字符串数组固定值"urn:ietf:params:scim:api:messages:2.0:ListResponse"。
     */
    private List<String> schemas;
    /**
     * 主从身份辨别字段，true:主身份，false：从身份
     */
    private String majorSubject;
    /**
     * 排序
     */
    private String order;
    /**
     * 用户电话
     */
    private List<Phone> phoneNumbers;


    /**
     * Email
     */
    @NoArgsConstructor
    @Data
    public static class Email implements Serializable {

        /**
         * 类型
         */
        private String type;
        /**
         * 邮箱
         */
        private String value;
        /**
         * 是否是主邮箱
         */
        private Boolean primary;
    }

    /**
     * Meta
     */
    @NoArgsConstructor
    @Data
    public static class Meta implements Serializable {

        /**
         * 创建时间，格式为"2010-01-23T04:56:22Z"
         */
        private String created;
        /**
         * 最后修改时间，格式为"2010-01-23T04:56:22Z"
         */
        private String lastModified;
        /**
         * 版本号
         */
        private String version;
        /**
         * 资源类型，固定值"User"
         */
        private String resourceType;
    }

    /**
     * Meta
     */
    @NoArgsConstructor
    @Data
    public static class Phone implements Serializable {

        /**
         * 类型
         */
        private String type;
        /**
         * 电话，格式：+86-19981285885
         */
        private String value;
        /**
         * 主电话
         */
        private Boolean primary;
    }
}
