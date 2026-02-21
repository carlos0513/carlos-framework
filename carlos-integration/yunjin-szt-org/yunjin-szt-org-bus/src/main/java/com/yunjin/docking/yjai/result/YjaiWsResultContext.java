package com.yunjin.docking.yjai.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * ws result context
 * </p>
 *
 * @author Carlos
 * @date 2024/4/19 10:37
 */
@NoArgsConstructor
@Data
public class YjaiWsResultContext {

    /**
     * reqId
     */
    @JsonProperty("reqId")
    private String reqId;
    /**
     * serviceName
     */
    @JsonProperty("serviceName")
    private String serviceName;
    /**
     * isCredible
     */
    @JsonProperty("isCredible")
    private Boolean isCredible;
    /**
     * action
     */
    @JsonProperty("action")
    private String action;
    /**
     * answer
     */
    @JsonProperty("answer")
    private Answer answer;
    /**
     * attachInfo
     */
    @JsonProperty("attachInfo")
    private Object attachInfo;

    /**
     * Answer
     */
    @NoArgsConstructor
    @Data
    public static class Answer {
        /**
         * showText
         */
        @JsonProperty("showText")
        private String showText;
        /**
         * qgroupId
         */
        @JsonProperty("qgroupId")
        private String qgroupId;
        /**
         * confidence
         */
        @JsonProperty("confidence")
        private Integer confidence;
        /**
         * anchors
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
             * name
             */
            @JsonProperty("name")
            private String name;
            /**
             * type
             */
            @JsonProperty("type")
            private String type;
            /**
             * url
             */
            @JsonProperty("url")
            private String url;
            /**
             * pageContent
             */
            @JsonProperty("pageContent")
            private String pageContent;
        }
    }

}
