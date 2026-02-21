package com.yunjin.docking.suining.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *   法人信息
 * </p>
 *
 * @author Carlos
 * @date 2025-01-15 08:58
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class SuiningNormalUser {

    @JsonProperty("base")
    private Base base;
    @JsonProperty("real")
    private Real real;

    @NoArgsConstructor
    @Data
    public static class Base {
        @JsonProperty("username")
        private String username;
        @JsonProperty("usernameMaskShow")
        private String usernameMaskShow;
        @JsonProperty("uuid")
        private String uuid;
        @JsonProperty("authLevel")
        private String authLevel;
    }

    @NoArgsConstructor
    @Data
    public static class Real {
        @JsonProperty("realName")
        private String realName;
        @JsonProperty("realNameMaskShow")
        private String realNameMaskShow;
        @JsonProperty("certNo")
        private String certNo;
        @JsonProperty("certNoMaskShow")
        private String certNoMaskShow;
        @JsonProperty("documentType")
        private String documentType;
    }
}
