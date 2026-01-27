package com.carlos.docking.rzt.organization.result;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class RztUserInfoResult {
    /**
     * 用户id
     */
    @JsonProperty("id")
    private String id;
    /**
     * 显示名
     */
    @JsonProperty("displayName")
    private String displayName;
    /**
     * 职务
     */
    @JsonProperty("office")
    private List<String> office;
    /**
     * 用户名，与蓉政通内的userid对应
     */
    @JsonProperty("userName")
    private String userName;
    /**
     * 用户主身份类型
     */
    @JsonProperty("subjectType")
    private String subjectType;
    /**
     * 用户状态，对应身份status字段，true:启用，false:冻结。
     */
    @JsonProperty("active")
    private Boolean active;

    /**
     * 用户邮箱
     */
    @JsonProperty("emails")
    private List<Email> emails;
    /**
     * 是否删除
     */
    @JsonProperty("deleted")
    private String deleted;
    /**
     * scim的标准，用户的元数据。
     */
    @JsonProperty("meta")
    private Meta meta;
    /**
     * 用户所属组织机构id
     */
    @JsonProperty("organization")
    private List<String> organization;
    /**
     * scim的schema，返回字符串数组固定值"urn:ietf:params:scim:api:messages:2.0:ListResponse"。
     */
    @JsonProperty("schemas")
    private List<String> schemas;
    /**
     * 主从身份辨别字段，true:主身份，false：从身份
     */
    @JsonProperty("majorSubject")
    private String majorSubject;
    /**
     * 排序
     */
    @JsonProperty("order")
    private String order;
    /**
     * 用户电话
     */
    @JsonProperty("phoneNumbers")
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
        @JsonProperty("type")
        private String type;
        /**
         * 邮箱
         */
        @JsonProperty("value")
        private String value;
        /**
         * 是否是主邮箱
         */
        @JsonProperty("primary")
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
        @JsonProperty("created")
        private String created;
        /**
         * 最后修改时间，格式为"2010-01-23T04:56:22Z"
         */
        @JsonProperty("lastModified")
        private String lastModified;
        /**
         * 版本号
         */
        @JsonProperty("version")
        private String version;
        /**
         * 资源类型，固定值"User"
         */
        @JsonProperty("resourceType")
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
        @JsonProperty("type")
        private String type;
        /**
         * 电话，格式：+86-19981285885
         */
        @JsonProperty("value")
        private String value;
        /**
         * 主电话
         */
        @JsonProperty("primary")
        private Boolean primary;
    }
}
