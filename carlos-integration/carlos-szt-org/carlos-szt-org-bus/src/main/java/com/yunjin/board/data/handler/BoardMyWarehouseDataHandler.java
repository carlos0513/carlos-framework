package com.yunjin.board.data.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.board.data.query.BoardMyDataDataQuery;
import com.yunjin.board.data.result.BoardMyDataResult;
import com.yunjin.board.data.result.BoardMyWarehouseDataResult;
import com.yunjin.core.response.Result;
import com.yunjin.form.metric.ApiFormMetric;
import com.yunjin.form.metric.FormMetric;
import com.yunjin.form.metric.FormMetricEnum;
import com.yunjin.warehouse.metric.ApiWarehouseMetric;
import com.yunjin.warehouse.metric.WarehouseMetric;
import com.yunjin.warehouse.metric.WarehouseMetricEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * <p>
 * 我的数据
 * </p>
 *
 * @author Carlos
 * @date 2025-05-20 22:28
 */
@Slf4j
public class BoardMyWarehouseDataHandler extends AbstractBoardDataHandler<BoardMyDataDataQuery, BoardMyWarehouseDataResult> {

    @Override
    public BoardMyWarehouseDataResult getData(Map<String, Object> param) {
        BoardMyDataDataQuery query = convertQueryParams(param);
        ApiWarehouseMetric metricApi = SpringUtil.getBean(ApiWarehouseMetric.class);
        Map<WarehouseMetricEnum, Object> map = new HashMap<>(4);
        map.put(WarehouseMetricEnum.myWarehouseData, query);
        Result<WarehouseMetric> metric = metricApi.getMetric(map);

        BoardMyWarehouseDataResult result = new BoardMyWarehouseDataResult();
        if (!metric.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", metric.getMessage(), metric.getStack());
            result.setErrMsg(metric.getMessage());
            return result;
        }

        if (metric.getData() != null && metric.getData().getUserCollectRefluxTable() != null) {
            WarehouseMetric.UserCollectRefluxTable tableData = metric.getData().getUserCollectRefluxTable();

            // 设置分页信息
            result.setTotal(tableData.getTotal());
            result.setSize(tableData.getSize());
            result.setPages(tableData.getPages());
            result.setCurrent(tableData.getCurrent());

            // 设置records
            if (CollUtil.isNotEmpty(tableData.getRecords())) {
                List<BoardMyWarehouseDataResult.Item> resultItems = new ArrayList<>();

                for (WarehouseMetric.TableItem tableItem : tableData.getRecords()) {
                    BoardMyWarehouseDataResult.Item resultItem = new BoardMyWarehouseDataResult.Item();

                    // 直接设置每个字段，确保类型正确
                    resultItem.setId(tableItem.getId());
                    resultItem.setName(tableItem.getName());
                    resultItem.setFromType(tableItem.getFromType());
                    resultItem.setSourceType(tableItem.getSourceType());
                    resultItem.setSourceId(tableItem.getSourceId());
                    resultItem.setCreateDeptId(tableItem.getCreateDeptId());
                    resultItem.setSourceDeptId(tableItem.getSourceDeptId());
                    resultItem.setCreateDeptName(tableItem.getCreateDeptName());
                    resultItem.setSourceDeptName(tableItem.getSourceDeptName());
                    resultItem.setStatus(tableItem.getStatus());
                    resultItem.setCount(tableItem.getCount());
                    resultItem.setDate(tableItem.getDate());
                    if (tableItem.getIsSelfData() != null) {
                        resultItem.setIsSelfData(tableItem.getIsSelfData());
                    }

                    resultItems.add(resultItem);
                }

                result.setRecords(resultItems);
            }
        }
        return result;

    }
}
