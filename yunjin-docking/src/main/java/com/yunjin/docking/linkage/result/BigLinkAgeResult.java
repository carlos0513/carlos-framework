package com.yunjin.docking.linkage.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

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
public class BigLinkAgeResult implements Serializable {

    /**
     * 错误码
     */
    @JsonProperty("resultCode")
    private String resultCode;
    /**
     * 错误信息
     */
    @JsonProperty("resultMsg")
    private String resultMsg;
    /**
     * 返回结果
     */
    @JsonProperty("results")
    private String results;
}
