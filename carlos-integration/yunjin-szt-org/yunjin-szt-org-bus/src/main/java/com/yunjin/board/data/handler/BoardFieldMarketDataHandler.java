package com.yunjin.board.data.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.board.data.query.BoardFieldMarketDataQuery;
import com.yunjin.board.data.result.BoardFieldMarketDataResult;
import com.yunjin.core.response.Result;
import com.yunjin.szt.data.metric.ApiDataMetric;
import com.yunjin.szt.data.metric.DataMetric;
import com.yunjin.szt.data.metric.DataMetricEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 1、展示展示当前登录用户（统计部门及下级部门）字段超市里面各个标准字段分类的数据。 2、展示指标：标签名称、数据集（关联的表单数量）、数据项（关联的字段数量）、数据量（关联的数据数量）
 * </p>
 *
 * @author Carlos
 * @date 2025-05-20 23:31
 */
@Slf4j
public class BoardFieldMarketDataHandler extends AbstractBoardDataHandler<BoardFieldMarketDataQuery, BoardFieldMarketDataResult> {


    @Override
    public BoardFieldMarketDataResult getData(Map<String, Object> param) {
        BoardFieldMarketDataQuery query = convertQueryParams(param);

        BoardFieldMarketDataResult result = new BoardFieldMarketDataResult();

        ApiDataMetric metricApi = SpringUtil.getBean(ApiDataMetric.class);
        Map<DataMetricEnum, Object> map = new HashMap<>(1);
        map.put(DataMetricEnum.userMetaDataOverView, query);

        Result<DataMetric> metric = metricApi.getMetric(map);
        if (!metric.getSuccess()) {
            log.error("Api ApiDataMetric.getMetric  request failed, message: {}, detail message:{}", metric.getMessage(), metric.getStack());
            result.setErrMsg(metric.getMessage());
            return result;
        }
        Optional.ofNullable(metric.getData()).map(DataMetric::getUserMetaDirOverviews).ifPresent(m -> {
            List<BoardFieldMarketDataResult.Item> items = BeanUtil.copyToList(m, BoardFieldMarketDataResult.Item.class);
            result.setItems(items);
        });
        return result;
    }
}
