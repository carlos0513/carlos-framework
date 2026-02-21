package com.yunjin.board.data.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.board.data.query.BoardMyDataDataQuery;
import com.yunjin.board.data.result.BoardMyDataResult;
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
 * 我的数据
 * </p>
 *
 * @author Carlos
 * @date 2025-05-20 22:28
 */
@Slf4j
public class BoardMyDataDataHandler extends AbstractBoardDataHandler<BoardMyDataDataQuery, BoardMyDataResult> {

    @Override
    public BoardMyDataResult getData(Map<String, Object> param) {
        BoardMyDataDataQuery query = convertQueryParams(param);
        ApiFormMetric metricApi = SpringUtil.getBean(ApiFormMetric.class);
        Map<FormMetricEnum, Object> map = new HashMap<>(4);
        map.put(FormMetricEnum.myData, query);
        Result<FormMetric> metric = metricApi.getMetric(map);

        BoardMyDataResult result = new BoardMyDataResult();
        if (!metric.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", metric.getMessage(), metric.getStack());
            result.setErrMsg(metric.getMessage());
            return result;
        }

        Optional.ofNullable(metric.getData()).map(FormMetric::getUserCollectTable).ifPresent(m -> {
            BeanUtil.copyProperties(m, result);
        });
        return result;

    }
}
