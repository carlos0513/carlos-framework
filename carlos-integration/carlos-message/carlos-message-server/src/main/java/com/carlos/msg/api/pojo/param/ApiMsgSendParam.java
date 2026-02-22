package com.carlos.msg.api.pojo.param;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * 消息发送参数
 * </p>
 *
 * @author Carlos
 * @date 2025-03-25 10:30
 */
@Data
@Accessors(chain = true)
public class ApiMsgSendParam implements Serializable {

    /**
     * 模板编码，系统发放的模板编码
     */
    private String templateCode;

    /**
     * 模板参数,需确保参数的顺序
     */
    private LinkedHashMap<String, Object> data;
    /**
     * 发送者信息
     */
    private Sender sender;
    /**
     * 发送渠道及接受者信息
     */
    private List<SendChannel> channels;
    /**
     * 系统标识
     */
    private String systemTag;

    /**
     * 消息操作信息
     */
    private Feedback feedback;


    @Data
    @Accessors(chain = true)
    public static class Sender implements Serializable {
        /**
         * 发送者id
         */
        private String senderId;
        /**
         * 发送者姓名
         */
        private String senderName;
        /**
         * 发送者部门名称
         */
        private String senderDept;

    }


    @Data
    @Accessors(chain = true)
    public static class Receiver implements Serializable {
        /**
         * 接受者id
         */
        private String id;
        /**
         * 接受者名称
         */
        private String name;
    }


    @Data
    @Accessors(chain = true)
    public static class SendChannel implements Serializable {

        /**
         * 渠道编码
         */
        private String channelCode;

        /**
         * 渠道接收者信息
         */
        private List<Receiver> receivers;
    }


    @Data
    @Accessors(chain = true)
    public static class Feedback implements Serializable {

        /**
         * 操作反馈类型(无, 详情, 站内跳转, 外链)
         */
        private String feedbackType;
        /**
         * 操作反馈内容
         */
        private String feedbackContent;

        private String param;

    }
}
