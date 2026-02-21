package com.yunjin.docking.yjai.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 元景大模型文本处理返回结果
 * </p>
 *
 * @author Carlos
 * @date 2022/3/8 12:18
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class YjAITextResultContent {


    /**
     * reqId 请求ID，对应request中的requestId
     */
    @JsonProperty("reqId")
    private String reqId;
    /**
     * serviceName 服务名称
     */
    @JsonProperty("serviceName")
    private String serviceName;
    /**
     * isCredible 回复是否可信，是否大于阈值
     */
    @JsonProperty("isCredible")
    private Boolean isCredible;
    /**
     * action 操作类型（start-握手，result-结果，error-出错，end-结束，unused-非流式）
     */
    @JsonProperty("action")
    private String action;
    /**
     * answer 回复内容
     */
    @JsonProperty("Answer")
    private Answer answer;
    /**
     * attachInfo 附加信息，相似问答对&得分，默认展示五个。具体见实例
     */
    @JsonProperty("attachInfo")
    private AttachInfo attachInfo;

    /**
     * Answer
     */
    @NoArgsConstructor
    @Data
    public static class Answer {
        /**
         * showText 回复文本
         */
        @JsonProperty("showText")
        private String showText;
        /**
         * qgroupId 所属组
         */
        @JsonProperty("qgroupId")
        private String qgroupId;
        /**
         * confidence 该showtext的置信度
         */
        @JsonProperty("confidence")
        private Double confidence;
        /**
         * anchors 附件信息
         */
        @JsonProperty("anchors")
        private List<Anchors> anchors;

        /**
         * Anchors
         */
        @NoArgsConstructor
        @Data
        public static class Anchors {
            /**
             * name 附件名称
             */
            @JsonProperty("name")
            private String name;
            /**
             * type 附件类型
             */
            @JsonProperty("type")
            private String type;
            /**
             * url 附件地址
             */
            @JsonProperty("url")
            private String url;
        }
    }


    /**
     * AttachInfo
     */
    @NoArgsConstructor
    @Data
    public static class AttachInfo {
        /**
         * simQuestions
         */
        @JsonProperty("simQuestions")
        private List<SimQuestions> simQuestions;
        /**
         * simAnswers
         */
        @JsonProperty("simAnswers")
        private List<Answer> simAnswers;

        /**
         * SimQuestions
         */
        @NoArgsConstructor
        @Data
        public static class SimQuestions {
            /**
             * question
             */
            @JsonProperty("question")
            private String question;
            /**
             * qgroupId
             */
            @JsonProperty("qgroupId")
            private String qgroupId;
            /**
             * confidence
             */
            @JsonProperty("confidence")
            private Double confidence;
        }


    }
}
