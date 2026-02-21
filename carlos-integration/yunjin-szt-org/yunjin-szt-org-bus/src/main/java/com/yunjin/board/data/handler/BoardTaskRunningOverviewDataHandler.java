package com.yunjin.board.data.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.board.data.query.BoardTaskRunningOverviewDataQuery;
import com.yunjin.board.data.result.BoardTaskRunningOverviewDataResult;
import com.yunjin.core.response.Result;
import com.yunjin.metric.ApiTaskMetric;
import com.yunjin.metric.TaskMetric;
import com.yunjin.metric.TaskMetricEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 用户的任务运行情况
 * </p>
 *
 * @author fanxd
 * @date 2025年11月20日09:09:38
 */
@Slf4j
public class BoardTaskRunningOverviewDataHandler extends AbstractBoardDataHandler<BoardTaskRunningOverviewDataQuery,
        BoardTaskRunningOverviewDataResult> {

    @Override
    public BoardTaskRunningOverviewDataResult getData(Map<String, Object> param) {
        BoardTaskRunningOverviewDataQuery query = convertQueryParams(param);
        BoardTaskRunningOverviewDataResult result = new BoardTaskRunningOverviewDataResult();

        ApiTaskMetric metricApi = SpringUtil.getBean(ApiTaskMetric.class);

        // 构建需要获取的指标枚举
        Map<TaskMetricEnum, Object> map = new HashMap<>(5);
        map.put(TaskMetricEnum.dispatchedTaskTotal, query);
        map.put(TaskMetricEnum.collectedDataTotal, query);
        map.put(TaskMetricEnum.last30DaysTask, query);

        Result<TaskMetric> metric = metricApi.getMetric(map);

        if (!metric.getSuccess()) {
            log.error("Api ApiTaskMetric.getMetric request failed, message: {}, detail message:{}",
                    metric.getMessage(), metric.getStack());
            result.setErrMsg(metric.getMessage());
            return result;
        }

        Optional.ofNullable(metric.getData()).ifPresent(m -> {
            // 派发任务总量
            Optional.ofNullable(m.getDispatchedTaskTotal()).ifPresent(count -> {
                result.setDispatchedTaskTotal(createMetricItem("派发任务总量", count, "所有已派发的任务总数"));
            });

            // 收集数据总量
            Optional.ofNullable(m.getCollectedDataTotal()).ifPresent(count -> {
                result.setCollectedDataTotal(createMetricItem("收集数据总量", count, "通过任务收集的数据总条数"));
            });

            // 近30日任务运行情况
            Optional.ofNullable(m.getLast30DaysTaskMetrics()).ifPresent(last30DaysMetrics -> {
                BoardTaskRunningOverviewDataResult.Last30DaysTaskMetrics resultLast30DaysMetrics =
                        new BoardTaskRunningOverviewDataResult.Last30DaysTaskMetrics();

                Optional.ofNullable(last30DaysMetrics.getTotalTasks()).ifPresent(count -> {
                    resultLast30DaysMetrics.setTotalTasks(createMetricItem("近30日任务", count, "近30天内创建的任务数量"));
                });

                Optional.ofNullable(last30DaysMetrics.getOngoingTasks()).ifPresent(count -> {
                    resultLast30DaysMetrics.setOngoingTasks(createMetricItem("近30日进行中任务", count, "近30天内仍在进行中的任务数量"));
                });

                Optional.ofNullable(last30DaysMetrics.getOverdueTasks()).ifPresent(count -> {
                    resultLast30DaysMetrics.setOverdueTasks(createMetricItem("近30日超期任务", count, "近30天内已超期的任务数量"));
                });

                result.setLast30DaysTaskMetrics(resultLast30DaysMetrics);
            });
        });

        return result;
    }

    /**
     * 创建MetricItem对象，处理Long类型数据
     */
    private BoardTaskRunningOverviewDataResult.MetricItem createMetricItem(String key, Long count, String description) {
        return new BoardTaskRunningOverviewDataResult.MetricItem(key, count, description);
    }


}
