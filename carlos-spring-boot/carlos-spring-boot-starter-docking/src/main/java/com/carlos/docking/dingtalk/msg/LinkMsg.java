package com.carlos.docking.dingtalk.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 链接消息
 *
 * @author top auto create
 * @since 1.0, null
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LinkMsg extends Msg {
    private static final long serialVersionUID = 2757883691527167563L;
    /**
     * 消息点击链接地址
     */

    private String messageUrl;
    /**
     * 图片地址
     */

    private String picUrl;
    /**
     * 消息标题，建议100字符以内
     */

    private String text;
    /**
     * 消息描述，建议500字符以内
     */

    private String title;
}
