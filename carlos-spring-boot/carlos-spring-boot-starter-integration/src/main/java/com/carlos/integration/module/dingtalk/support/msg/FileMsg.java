package com.carlos.integration.module.dingtalk.support.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件消息
 *
 * @author top auto create
 * @since 1.0, null
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileMsg extends Msg {
    private static final long serialVersionUID = 8133117921592134818L;
    /**
     * 媒体文件id。引用的媒体文件最�?0MB
     */

    private String mediaId;

}

