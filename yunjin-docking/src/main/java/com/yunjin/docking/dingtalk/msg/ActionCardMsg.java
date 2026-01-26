package com.yunjin.docking.dingtalk.msg;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 卡片消息
 *
 * @author top auto create
 * @since 1.0, null
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActionCardMsg extends Msg {
    private static final long serialVersionUID = 2194296449645234598L;
    /**
     * 使用独立跳转ActionCard样式时的按钮列表；必须与btn_orientation同时设置
     */

    private List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonList;
    /**
     * 使用独立跳转ActionCard样式时的按钮排列方式，竖直排列(0)，横向排列(1)；必须与btn_json_list同时设置
     */

    private String btnOrientation;
    /**
     * 消息内容，支持markdown，语法参考标准markdown语法。建议1000个字符以内
     */

    private String markdown;
    /**
     * 使用整体跳转ActionCard样式时的标题，必须与single_url同时设置，最长20个字符
     */

    private String singleTitle;
    /**
     * 消息点击链接地址，当发送消息为小程序时支持小程序跳转链接，最长500个字符
     */

    private String singleUrl;
    /**
     * 透出到会话列表和通知的文案，最长64个字符
     */

    private String title;

}