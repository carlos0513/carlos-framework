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
public class SuiningManagerUser {

    @JsonProperty("base")
    private Base base;

    @NoArgsConstructor
    @Data
    public static class Base {
        @JsonProperty("socialCreditCode")
        private String socialCreditCode;
        @JsonProperty("deptName")
        private String deptName;
        @JsonProperty("deptType")
        private String deptType;
        @JsonProperty("deptPerson")
        private String deptPerson;
        @JsonProperty("deptPersonMaskShow")
        private String deptPersonMaskShow;
        @JsonProperty("deptPersonCode")
        private String deptPersonCode;
        @JsonProperty("deptPersonCodeMaskShow")
        private String deptPersonCodeMaskShow;
        @JsonProperty("deptPersonCodeType")
        private String deptPersonCodeType;
        @JsonProperty("uuid")
        private String uuid;
        @JsonProperty("username")
        private String username;
        @JsonProperty("usernameMaskShow")
        private String usernameMaskShow;
        @JsonProperty("managerName")
        private String managerName;
        @JsonProperty("managerNameMaskShow")
        private String managerNameMaskShow;
        @JsonProperty("managerCertNo")
        private String managerCertNo;
        @JsonProperty("managerCertNoMaskShow")
        private String managerCertNoMaskShow;
        @JsonProperty("managerDocumentType")
        private String managerDocumentType;
    }
}
