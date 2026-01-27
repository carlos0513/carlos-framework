package com.carlos.docking.rzt.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * <p>
 *   蓉政通发送文本消息参数
 * </p>
 *
 * @author Carlos
 * @date 2024-10-28 16:23
 */
@NoArgsConstructor
@Data
public class RztScimIamGetUserParam {
    /** 开始下标 。默认:1（该字段所表示的是，从第几条数据开始返回。当填写1时，即从第一条数据开始返回） */
    @JsonProperty("startIndex")
    private Integer startIndex;

    /** 每一页查询记录数。默认:10。最大值：200，且必须填写整数（该字段所表示的为每一次调用返回多少条数据。当填写为20，即一页返回20条数据） */
    @JsonProperty("count")
    private Integer count;

    /** 查询条件，仅支持用户名、手机号、邮箱、组织机构、更新时间单一条件查询，不支持复杂的逻辑条件查询。操作符仅支持：eq,gt、co、and。
     filter条件符合scim语法：
     1.用户名查询：filter=userName eq "aa"
     2.邮箱查询：filter=emails eq "aa@example.com"
     3.手机号查询：filter=phoneNumbers eq "13123456789"
     4.更新时间查询：filter=meta.lastModified gt "2022-05-13T04:42:34Z"
     5.组织机构查询：filter=org eq "uuid"
     6.职务查询：filter=office eq "主任"
     7.模糊查询：filter=office co "主任"
     8.组合查询（仅支持两种条件组合）：filter=org eq "uuid" and office co "主任"
     */
    @JsonProperty("filter")
    private String filter;

    /** 1.searchType=NORMAL，现有逻辑即不返回被删除的数据（也为默认值）；
     2.searchType=ALL，返回近期所有更新的数据（包括被软删除的数据）；
     3.searchType=DEL_ONLY，仅返回删除的数据 */
    @JsonProperty("searchType")
    private String searchType;

}
