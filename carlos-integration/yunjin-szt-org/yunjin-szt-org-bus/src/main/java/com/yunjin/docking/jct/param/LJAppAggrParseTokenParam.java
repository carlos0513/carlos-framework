package com.yunjin.docking.jct.param;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *   用户信息解析接口参数
 * </p>
 *
 * @author Carlos
 * @date 2025-02-27 08:52
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class LJAppAggrParseTokenParam implements Serializable {


    /** 应用id */
    @JsonProperty("appSign")
    private String appSign;

    /** token值 */
    @JsonProperty("accessToken")
    private String accessToken;
}
