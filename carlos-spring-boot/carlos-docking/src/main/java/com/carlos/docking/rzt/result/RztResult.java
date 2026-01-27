package com.carlos.docking.rzt.result;


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
public class RztResult implements Serializable {

    /**
     * 错误码
     */
    @JsonProperty("errcode")
    private String errcode;
    /**
     * 错误信息
     */
    @JsonProperty("errmsg")
    private String errmsg;
}
