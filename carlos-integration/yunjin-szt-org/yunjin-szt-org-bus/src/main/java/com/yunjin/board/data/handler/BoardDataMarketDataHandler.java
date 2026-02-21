package com.yunjin.board.data.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.board.data.query.BoardDataMarketDataQuery;
import com.yunjin.board.data.result.BoardDataMarketDataResult;
import com.yunjin.core.response.Result;
import com.yunjin.warehouse.metric.ApiWarehouseMetric;
import com.yunjin.warehouse.metric.WarehouseMetric;
import com.yunjin.warehouse.metric.WarehouseMetricEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * <p>
 * 数据超市 1、轮播展示当前登录用户部门及下级部门内数据超市按数据量或浏览量排序的Top10报表。 2、每个报表卡片展示指标：数据量、所属部门、浏览次数、更新时间。
 * 3、引导语：显示数据超市情况，可以了解数据超市数据量或浏览量较高的报表情况。
 * </p>
 *
 * @author Carlos
 * @date 2025-05-16 13:21
 */
@Slf4j
public class BoardDataMarketDataHandler extends AbstractBoardDataHandler<BoardDataMarketDataQuery, BoardDataMarketDataResult> {
    @Override
    public BoardDataMarketDataResult getData(Map<String, Object> param) {

        BoardDataMarketDataQuery query = convertQueryParams(param);
        BoardDataMarketDataResult result = new BoardDataMarketDataResult();


        ApiWarehouseMetric metricApi = SpringUtil.getBean(ApiWarehouseMetric.class);

        Map<WarehouseMetricEnum, Object> map = new HashMap<>(1);
        map.put(WarehouseMetricEnum.superMarketTop10, query);
        Result<WarehouseMetric> metric = metricApi.getMetric(map);
        if (!metric.getSuccess()) {
            log.error("Api ApiWarehouseMetric.getMetric  request failed, message: {}, detail message:{}", metric.getMessage(), metric.getStack());
            result.setErrMsg(metric.getMessage());
            return result;
        }

        Optional.ofNullable(metric.getData()).map(WarehouseMetric::getSuperMarketTop10).ifPresent(m -> {
            List<BoardDataMarketDataResult.Item> items = BeanUtil.copyToList(m, BoardDataMarketDataResult.Item.class);
            result.setItems(items);
        });
        return result;
    }
}
