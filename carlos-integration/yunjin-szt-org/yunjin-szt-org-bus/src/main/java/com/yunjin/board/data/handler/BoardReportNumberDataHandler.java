package com.yunjin.board.data.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.board.data.query.BoardReportNumberDataQuery;
import com.yunjin.board.data.result.BoardReportNumberDataResult;
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
 * 1、轮播展示当前登录用户所在市的各个区县报表数量。
 * 2、指标项：市级下发（来源层级为市级且已下发的报表数量）、区级下发（来源层级为区级且已下发的报表数量）、镇街（市或区已下发且最末填报层级为镇街）、村社区（市或区已下发且最末填报层级为村社区）
 * 3、引导语：显示已下发报表情况，可以了解各个区县已下发到镇（街道）、村（社区）的市级或区级报表。
 * </p>
 *
 * @author Carlos
 * @date 2025-05-20 23:34
 */
@Slf4j
public class BoardReportNumberDataHandler extends AbstractBoardDataHandler<BoardReportNumberDataQuery, BoardReportNumberDataResult> {


    @Override
    public BoardReportNumberDataResult getData(Map<String, Object> param) {
        BoardReportNumberDataQuery query = convertQueryParams(param);
        ApiFormMetric metricApi = SpringUtil.getBean(ApiFormMetric.class);

        Map<FormMetricEnum, Object> map = new HashMap<>(4);
        map.put(FormMetricEnum.regionReportNumber, query);
        Result<FormMetric> metric = metricApi.getMetric(map);
        BoardReportNumberDataResult result = new BoardReportNumberDataResult();
        if (!metric.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", metric.getMessage(), metric.getStack());
            result.setErrMsg(metric.getMessage());
            return result;
        }
        Optional.ofNullable(metric.getData()).map(FormMetric::getRegionReportNumbers).ifPresent(m -> {
            result.setRegions(BeanUtil.copyToList(m, BoardReportNumberDataResult.Region.class));
        });
        return result;
    }
}
