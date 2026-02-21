package com.yunjin.board.data.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.board.data.query.BoardUserOverviewDataQuery;
import com.yunjin.board.data.result.BoardUserOverviewDataResult;
import com.yunjin.core.response.Result;
import com.yunjin.org.metric.ApiOrgMetric;
import com.yunjin.org.metric.OrgMetric;
import com.yunjin.org.metric.OrgMetricEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
public class BoardUserOverviewDataHandler extends AbstractBoardDataHandler<BoardUserOverviewDataQuery, BoardUserOverviewDataResult> {
    @Override
    public BoardUserOverviewDataResult getData(Map<String, Object> param) {
        BoardUserOverviewDataQuery query = convertQueryParams(param);

        ApiOrgMetric metricApi = SpringUtil.getBean(ApiOrgMetric.class);

        Map<OrgMetricEnum, Object> map = new HashMap<>(4);
        map.put(OrgMetricEnum.activeCount, query);
        map.put(OrgMetricEnum.disableCount, query);
        map.put(OrgMetricEnum.pcActiveCount, query);
        map.put(OrgMetricEnum.mobileActiveCount, query);
        map.put(OrgMetricEnum.registerCount, query);
        map.put(OrgMetricEnum.registerCountInOneYear, query);


        Result<OrgMetric> metric = metricApi.getMetric(map);

        BoardUserOverviewDataResult result = new BoardUserOverviewDataResult();
        if (!metric.getSuccess()) {
            log.error("Api ApiOrgMetric.getMetric  request failed, message: {}, detail message:{}", metric.getMessage(), metric.getStack());
            result.setErrMsg(metric.getMessage());
            return result;
        }

        Optional.ofNullable(metric.getData()).ifPresent(m -> {
            result.setActiveCount(m.getActiveCount());
            result.setDisableCount(m.getDisableCount());
            result.setPcActiveCount(m.getPcActiveCount());
            result.setMobileActiveCount(m.getMobileActiveCount());
            result.setRegisterCount(m.getRegisterCount());
            result.setActiveCountV2(m.getPcActiveCount() + m.getMobileActiveCount());
            result.setRegisterCountInOneYear(m.getRegisterCountInOneYear());

        });
        return result;
    }
}
