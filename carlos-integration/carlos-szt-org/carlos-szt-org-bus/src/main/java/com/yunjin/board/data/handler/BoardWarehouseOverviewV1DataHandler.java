package com.yunjin.board.data.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.board.data.query.BoardWarehouseOverviewDataQuery;
import com.yunjin.board.data.result.BoardWarehouseOverviewDataResult;
import com.yunjin.core.response.Result;
import com.yunjin.warehouse.metric.ApiWarehouseMetric;
import com.yunjin.warehouse.metric.WarehouseMetric;
import com.yunjin.warehouse.metric.WarehouseMetricEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Title: BoardWarehouseOverviewDataHandlerV1
 * @Author fxd
 * @Date 2025/11/27 17:16
 * @description: V1版本
 */
@Slf4j
public class BoardWarehouseOverviewV1DataHandler extends AbstractBoardDataHandler<BoardWarehouseOverviewDataQuery, BoardWarehouseOverviewDataResult> {

    @Override
    public BoardWarehouseOverviewDataResult getData(Map<String, Object> param) {
        BoardWarehouseOverviewDataQuery query = convertQueryParams(param);
        BoardWarehouseOverviewDataResult result = new BoardWarehouseOverviewDataResult();

        ApiWarehouseMetric metricApi = SpringUtil.getBean(ApiWarehouseMetric.class);

        Map<WarehouseMetricEnum, Object> map = new HashMap<>(4);
        map.put(WarehouseMetricEnum.warehouseBasicData, query);
        Result<WarehouseMetric> metric = metricApi.getMetric(map);

        if (!metric.getSuccess()) {
            log.error("Api ApiWarehouseMetric.getMetric  request failed, message: {}, detail message:{}", metric.getMessage(), metric.getStack());
            result.setErrMsg(metric.getMessage());
            return result;
        }

        Optional.ofNullable(metric.getData()).ifPresent(m -> {
            BeanUtil.copyProperties(m, result);
        });
        return result;
    }
}
