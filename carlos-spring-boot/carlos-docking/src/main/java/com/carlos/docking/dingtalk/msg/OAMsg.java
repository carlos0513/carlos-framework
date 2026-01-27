package com.carlos.docking.dingtalk.msg;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OA消息
 *
 * @author top auto create
 * @since 1.0, null
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OAMsg extends Msg {
    private static final long serialVersionUID = 2599774517599455299L;
    /**
     * 消息体
     */

    private OapiMessageCorpconversationAsyncsendV2Request.Body body;
    /**
     * 消息头部内容
     */

    private OapiMessageCorpconversationAsyncsendV2Request.Head head;
    /**
     * 消息点击链接地址，当发送消息为小程序时支持小程序跳转链接
     */

    private String messageUrl;
    /**
     * PC端点击消息时跳转到的地址
     */

    private String pcMessageUrl;
    /**
     * 状态栏
     */

    private OapiMessageCorpconversationAsyncsendV2Request.StatusBar statusBar;
}