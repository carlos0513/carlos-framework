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
public class BoardRtReportOverviewDataHandler extends AbstractBoardDataHandler<BoardRtReportOverviewDataQuery, BoardRtReportOverviewDataResult> {
    @Override
    public BoardRtReportOverviewDataResult getData(Map<String, Object> param) {
        BoardRtReportOverviewDataQuery query = convertQueryParams(param);

        ApiFormMetric metricApi = SpringUtil.getBean(ApiFormMetric.class);
        Map<FormMetricEnum, Object> map = new HashMap<>(4);
        map.put(FormMetricEnum.reportCount, query);
        map.put(FormMetricEnum.distributeReportCount, query);
        map.put(FormMetricEnum.parentReportCount, query);
        map.put(FormMetricEnum.currentReportCount, query);
        map.put(FormMetricEnum.reportRegistrationCount, query);
        map.put(FormMetricEnum.reportReviewCount, query);
        Result<FormMetric> metric = metricApi.getMetric(map);

        BoardRtReportOverviewDataResult result = new BoardRtReportOverviewDataResult();
        if (!metric.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", metric.getMessage(), metric.getStack());
            result.setErrMsg(metric.getMessage());
            return result;
        }

        Optional.ofNullable(metric.getData()).ifPresent(m -> {
            result.setReportCount(m.getReportCount());
            result.setDistributeReportCount(m.getDistributeReportCount());
            result.setParentReportCount(m.getParentReportCount());
            result.setCurrentReportCount(m.getCurrentReportCount());
            result.setReportRegistrationCount(m.getReportRegistrationCount());
            result.setReportReviewCount(m.getReportReviewCount());
        });
        return result;
    }
}
