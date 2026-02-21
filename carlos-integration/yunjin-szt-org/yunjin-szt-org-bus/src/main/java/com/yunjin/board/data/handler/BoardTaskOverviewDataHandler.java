package com.yunjin.board.data.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.board.data.query.BoardTaskOverviewDataQuery;
import com.yunjin.board.data.result.BoardTaskOverviewDataResult;
import com.yunjin.core.response.Result;
import com.yunjin.metric.ApiTaskMetric;
import com.yunjin.metric.TaskMetric;
import com.yunjin.metric.TaskMetricEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


@Slf4j
public class BoardTaskOverviewDataHandler extends AbstractBoardDataHandler<BoardTaskOverviewDataQuery, BoardTaskOverviewDataResult> {

    @Override
    public BoardTaskOverviewDataResult getData(Map<String, Object> param) {
        BoardTaskOverviewDataQuery query = convertQueryParams(param);
        BoardTaskOverviewDataResult result = new BoardTaskOverviewDataResult();

        ApiTaskMetric metricApi = SpringUtil.getBean(ApiTaskMetric.class);

        Map<TaskMetricEnum, Object> map = new HashMap<>(3);
        map.put(TaskMetricEnum.allTask, query);
        Result<TaskMetric> metric = metricApi.getMetric(map);

        if (!metric.getSuccess()) {
            log.error("Api ApiTaskMetric.getMetric  request failed, message: {}, detail message:{}", metric.getMessage(), metric.getStack());
            result.setErrMsg(metric.getMessage());
            return result;
        }

        Optional.ofNullable(metric.getData()).ifPresent(m -> {
            List<BoardTaskOverviewDataResult.Item> list = new ArrayList<>();
            Optional.of(m.getAllTask()).ifPresent(i -> {
                list.add(new BoardTaskOverviewDataResult.Item("全量任务", i.getTaskCount(), i.getOverdueCount()));
            });
            Optional.of(m.getParentTask()).ifPresent(i -> {
                list.add(new BoardTaskOverviewDataResult.Item("上级任务", i.getTaskCount(), i.getOverdueCount()));
            });
            Optional.of(m.getCurrentTask()).ifPresent(i -> {
                list.add(new BoardTaskOverviewDataResult.Item("本级任务", i.getTaskCount(), i.getOverdueCount()));
            });
            result.setItems(list);
        });
        return result;
    }
}
