package com.yunjin.board.data.result;


import com.yunjin.form.metric.FormMetric;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 看板数据
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:09
 */
@Data
@Accessors(chain = true)
public class BoardRtReportOverviewDataResult extends BoardDataResult {

    /**
     * 报表总数
     */
    private int reportCount;
    /**
     * 下发报表总数
     */
    private int distributeReportCount;
    /**
     * 上级报表总数
     */
    private int parentReportCount;
    /**
     * 本级报表总数
     */
    private int currentReportCount;
    /**
     * 报表注册申请
     */
    private int reportRegistrationCount;
    /**
     * 报表审核退回
     */
    private int reportReviewCount;


    /**
     * 县 镇 村 网格报表相关信息
     * 区域报表统计信息
     */
    private RegionReport regionReport;


    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionReport implements Serializable {

        /**
         * 字段总数
         */
        private int reportItemCount;
        /**
         *县级报表数
         */
        private int countyReportCount;
        /**
         * 镇街报表数
         */
        private int streetReportCount;
        /**
         * 村社报表数
         */
        private int communityReportCount;
        /**
         * 网格报表数
         */
        private int gridReportCount;

    }


}