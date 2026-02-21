package com.yunjin.docking.yjai.param;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunjin.core.param.Param;
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
public class YjAITextParam implements Param {

    /**
     * 客户端id
     */
    @JsonProperty("clientId")
    private String clientId;

    /**
     * 通道id
     */
    @JsonProperty("channelId")
    private String channelId;

    /**
     * 请求id
     */
    @JsonProperty("requestId")
    private String requestId;

    /**
     * 查询内容
     */
    @JsonProperty("query")
    private String query;
}
