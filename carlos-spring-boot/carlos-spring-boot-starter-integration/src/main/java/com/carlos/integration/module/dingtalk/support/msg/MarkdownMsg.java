package com.carlos.integration.module.dingtalk.support.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * markdown消息
 *
 * @author top auto create
 * @since 1.0, null
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MarkdownMsg extends Msg {
    private static final long serialVersionUID = 4352594231334542361L;
    /**
     * markdown格式的消息，建议500字符以内
     */

    private String text;
    /**
     * 首屏会话透出的展示内�?
     */

    private String title;

}
	
