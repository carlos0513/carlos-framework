package com.yunjin.docking.rzt.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MessageSendResult extends RztResult {


    private static final long serialVersionUID = 3451821858707330717L;
    /**
     * 如果接收人不存在或无权限，发送仍然执行，但会返回无效的部分（即invaliduser或invalidparty或invalidtag）
     * 未收到信息常见的原因是接收人不在应用的可见范围内。
     */
    @JsonProperty("invaliduser")
    private String invaliduser;
    /**
     * 员名称
     */
    @JsonProperty("toparty")
    private String toparty;
    /** 可以通过jobid查询消息发送的进度 */
    @JsonProperty("jobid")
    private String jobid;
}
