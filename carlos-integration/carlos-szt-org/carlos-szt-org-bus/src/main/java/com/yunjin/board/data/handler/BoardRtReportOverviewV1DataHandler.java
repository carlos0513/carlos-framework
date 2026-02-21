package com.yunjin.board.data.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.board.data.query.BoardRtReportOverviewDataQuery;
import com.yunjin.board.data.result.BoardRtReportOverviewDataResult;
import com.yunjin.core.response.Result;
import com.yunjin.form.metric.ApiFormMetric;
import com.yunjin.form.metric.FormMetric;
import com.yunjin.form.metric.FormMetricEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 看板-报表数据
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 15:44
 */
@Slf4j
public class BoardRtReportOverviewV1DataHandler extends AbstractBoardDataHandler<BoardRtReportOverviewDataQuery, BoardRtReportOverviewDataResult> {
    @Override
    public BoardRtReportOverviewDataResult getData(Map<String, Object> param) {
        BoardRtReportOverviewDataQuery query = convertQueryParams(param);

        ApiFormMetric metricApi = SpringUtil.getBean(ApiFormMetric.class);
        Map<FormMetricEnum, Object> map = new HashMap<>(4);
        map.put(FormMetricEnum.reportCount, query);
        map.put(FormMetricEnum.reportItemCount, query);
        map.put(FormMetricEnum.countyReportCount, query);
        map.put(FormMetricEnum.streetReportCount, query);
        map.put(FormMetricEnum.communityReportCount, query);
        map.put(FormMetricEnum.gridReportCount, query);
        Result<FormMetric> metric = metricApi.getMetric(map);

        BoardRtReportOverviewDataResult result = new BoardRtReportOverviewDataResult();
        if (!metric.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", metric.getMessage(), metric.getStack());
            result.setErrMsg(metric.getMessage());
            return result;
        }

        Optional.ofNullable(metric.getData()).ifPresent(m -> {
            result.setReportCount(m.getReportCount());

            // 设置区域报表统计信息
            FormMetric.RegionReport formRegionReport = m.getRegionReport();
            if (formRegionReport != null) {
                BoardRtReportOverviewDataResult.RegionReport resultRegionReport =
                        new BoardRtReportOverviewDataResult.RegionReport()
                                .setReportItemCount(formRegionReport.getReportItemCount())
                                .setCountyReportCount(formRegionReport.getCountyReportCount())
                                .setStreetReportCount(formRegionReport.getStreetReportCount())
                                .setCommunityReportCount(formRegionReport.getCommunityReportCount())
                                .setGridReportCount(formRegionReport.getGridReportCount());
                result.setRegionReport(resultRegionReport);
            }
        });
        return result;
    }
}
