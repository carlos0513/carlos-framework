package com.carlos.docking.rzt.organization.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
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
@Data
@Accessors(chain = true)
public class RztPageResult<T> implements Serializable {

    /**
     * scim的schema，返回字符串数组固定值"urn:ietf:params:scim:api:messages:2.0:ListResponse"。
     */
    @JsonProperty("schemas")
    private List<String> schemas;
    /**
     * 总记录数
     */
    @JsonProperty("totalResults")
    private Integer totalResults;
    /**
     * 错误编码
     */
    @JsonProperty("Resources")
    private List<T> resources;
    /**
     * 错误编码
     */
    @JsonProperty("errorCode")
    private String errorCode;
    /**
     * 错误信息
     */
    @JsonProperty("errorMsg")
    private String errorMsg;
}
