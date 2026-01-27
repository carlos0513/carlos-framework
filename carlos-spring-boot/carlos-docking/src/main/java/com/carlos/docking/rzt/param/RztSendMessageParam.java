package com.carlos.docking.rzt.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
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
public class RztSendMessageParam {
    /** 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向该单位应用的全部成员发送 */
    @JsonProperty("touser")
    private String touser;
    /** 部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数 */
    @JsonProperty("toparty")
    private String toparty;
    /** 标签ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数 */
    @JsonProperty("totag")
    private String totag;
    /**
     * 消息类型，此时固定为：text
     */
    @JsonProperty("msgtype")
    private String msgtype;
    /**
     * 单位应用的id，整型。可在应用的设置页面查看
     */
    @JsonProperty("agentid")
    private Integer agentid;
    /**
     *
     */
    @JsonProperty("text")
    private TextMessage text;
    /**
     *
     */
    @JsonProperty("access_token")
    private String accessToken;

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class TextMessage {
        /**
         * 消息内容，最长不超过2048个字节
         */
        @JsonProperty("content")
        private String content;
    }
}
