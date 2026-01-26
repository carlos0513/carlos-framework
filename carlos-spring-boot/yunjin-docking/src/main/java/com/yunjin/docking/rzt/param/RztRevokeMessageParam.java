package com.yunjin.docking.rzt.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


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
public class RztRevokeMessageParam {

    @JsonProperty("jobid")
    private String jobid;
    @JsonProperty("revokelist")
    private List<RevokeItem> revokelist;

    @NoArgsConstructor
    @Data
    public static class RevokeItem {
        @JsonProperty("userid")
        private String userid;
        @JsonProperty("msgid")
        private List<String> msgid;
    }
}
