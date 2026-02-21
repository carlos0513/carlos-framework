package com.yunjin.board.data.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.board.data.query.BoardTaskRunningOverviewV2DataQuery;
import com.yunjin.board.data.result.BoardTaskRunningOverviewDataResult;
import com.yunjin.board.data.result.BoardTaskRunningOverviewV2DataResult;
import com.yunjin.core.response.Result;
import com.yunjin.metric.ApiTaskMetric;
import com.yunjin.metric.TaskMetric;
import com.yunjin.metric.TaskMetricEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户的任务运行情况
 * </p>
 *
 * @author fanxd
 * @date 2025年11月20日09:09:38
 */
@Slf4j
public class BoardTaskRunningOverviewV2DataHandler extends AbstractBoardDataHandler<BoardTaskRunningOverviewV2DataQuery,
        BoardTaskRunningOverviewV2DataResult> {

    @Override
    public BoardTaskRunningOverviewV2DataResult getData(Map<String, Object> param) {
        BoardTaskRunningOverviewV2DataQuery query = convertQueryParams(param);
        BoardTaskRunningOverviewV2DataResult result = new BoardTaskRunningOverviewV2DataResult();
        ApiTaskMetric metricApi = SpringUtil.getBean(ApiTaskMetric.class);
        // 构建需要获取的指标枚举
        Map<TaskMetricEnum, Object> map = new HashMap<>(12);
        //市级任务运行情况
        map.put(TaskMetricEnum.cityDispatchedTaskTotal, query);
        //县级任务运行情况
        map.put(TaskMetricEnum.countyDispatchedTaskTotal, query);


        Result<TaskMetric> metric = metricApi.getMetric(map);

        if(! metric.getSuccess()) {
            log.error("Api ApiTaskMetric.getMetric request failed, message: {}, detail message:{}",
                    metric.getMessage(), metric.getStack());
            result.setErrMsg(metric.getMessage());
            return result;
        }
        TaskMetric taskMetric = metric.getData();
        BoardTaskRunningOverviewV2DataResult.ItemTaskName cityTask = new BoardTaskRunningOverviewV2DataResult.ItemTaskName(
                "市级任务",
                new BoardTaskRunningOverviewV2DataResult.Item(TaskMetricEnum.cityDispatchedTaskTotal, "派发任务总量", taskMetric.getCityDispatchedTaskTotal()),
                new BoardTaskRunningOverviewV2DataResult.Item(TaskMetricEnum.cityCollectedDataTotal, "收集数据总量", taskMetric.getCityCollectedDataTotal()),
                new BoardTaskRunningOverviewV2DataResult.Item(TaskMetricEnum.cityUnderCounty, "县级", taskMetric.getCityUnderCounty()),
                new BoardTaskRunningOverviewV2DataResult.Item(TaskMetricEnum.cityUnderTown, "街镇", taskMetric.getCityUnderTown()),
                new BoardTaskRunningOverviewV2DataResult.Item(TaskMetricEnum.cityUnderVillage, "村社", taskMetric.getCityUnderVillage()),
                new BoardTaskRunningOverviewV2DataResult.Item(TaskMetricEnum.cityUnderGrid, "网格", taskMetric.getCityUnderGrid())
        );
        BoardTaskRunningOverviewV2DataResult.ItemTaskName countyTask = new BoardTaskRunningOverviewV2DataResult.ItemTaskName(
                "县级任务",
                new BoardTaskRunningOverviewV2DataResult.Item(TaskMetricEnum.countyDispatchedTaskTotal, "派发任务总量", taskMetric.getCountyDispatchedTaskTotal()),
                new BoardTaskRunningOverviewV2DataResult.Item(TaskMetricEnum.countyCollectedDataTotal, "收集数据总量", taskMetric.getCountyCollectedDataTotal()),
                new BoardTaskRunningOverviewV2DataResult.Item(TaskMetricEnum.countyUnderOwn, "县级", taskMetric.getCountyUnderOwn()),
                new BoardTaskRunningOverviewV2DataResult.Item(TaskMetricEnum.countyUnderTown, "街镇", taskMetric.getCountyUnderTown()),
                new BoardTaskRunningOverviewV2DataResult.Item(TaskMetricEnum.countyUnderVillage, "村社", taskMetric.getCountyUnderVillage()),
                new BoardTaskRunningOverviewV2DataResult.Item(TaskMetricEnum.countyUnderGrid, "网格", taskMetric.getCountyUnderGrid())
        );
        result.setRegions(Arrays.asList(cityTask, countyTask));
        return result;
    }


}
