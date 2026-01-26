package com.yunjin.docking.dingtalk.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VoiceMsg extends Msg {
    private static final long serialVersionUID = 8116843149852161251L;
    /**
     * 正整数，小于60，表示音频时长
     */

    private String duration;
    /**
     * 媒体文件id。2MB，播放长度不超过60s，AMR格式
     */

    private String mediaId;

}