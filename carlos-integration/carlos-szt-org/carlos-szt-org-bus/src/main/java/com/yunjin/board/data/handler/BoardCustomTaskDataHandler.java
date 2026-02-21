package com.yunjin.board.data.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.board.convert.BoardTaskMetricConvert;
import com.yunjin.board.data.query.BoardCustomTaskDataQuery;
import com.yunjin.board.data.result.BoardCustomTaskDataResult;
import com.yunjin.core.response.Result;
import com.yunjin.metric.ApiTaskMetric;
import com.yunjin.metric.TaskMetric;
import com.yunjin.metric.TaskMetricEnum;
import com.yunjin.org.UserUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户的待办任务和下派任务
 * </p>
 *
 * @author Carlos
 * @date 2025-05-16 09:04
 */
@Slf4j
public class BoardCustomTaskDataHandler extends AbstractBoardDataHandler<BoardCustomTaskDataQuery, BoardCustomTaskDataResult> {
    @Override
    public BoardCustomTaskDataResult getData(Map<String, Object> param) {
        log.info("BoardCustomTaskDataHandler getData param: {}", param);
        BoardCustomTaskDataQuery query = convertQueryParams(param);
        BoardCustomTaskDataResult result = new BoardCustomTaskDataResult();
        ApiTaskMetric metricApi = SpringUtil.getBean(ApiTaskMetric.class);
        Map<TaskMetricEnum, Object> map = new HashMap<>(2);
        map.put(TaskMetricEnum.undealTask, query);
        map.put(TaskMetricEnum.sendTask, query);
        map.put(TaskMetricEnum.approveTask, query);
        map.put(TaskMetricEnum.applyTask, query);
        Result<TaskMetric> metric = metricApi.getMetric(map);

        if (!metric.getSuccess()) {
            log.error("Api ApiTaskMetric.getMetric  request failed, message: {}, detail message:{}", metric.getMessage(), metric.getStack());
            result.setErrMsg(metric.getMessage());
            return result;
        }

        log.info("返回结果: {}", metric.getData());
        String userId = UserUtil.getId();
        Optional.ofNullable(metric.getData()).ifPresent(m -> {
            // undealTask - 使用转换器并过滤
            Optional.ofNullable(m.getUndealTasks()).ifPresent(i -> {
                BoardCustomTaskDataResult.Task task = BoardTaskMetricConvert.INSTANCE.toTask(i);
                // 过滤掉创建者为当前用户的任务
                if (CollUtil.isNotEmpty(task.getItems()) && StrUtil.isNotBlank(userId)) {
                    List<BoardCustomTaskDataResult.Item> filteredItems = task.getItems().stream()
                            .filter(item -> !userId.equals(item.getCreatorId()))
                            .collect(Collectors.toList());
                    task.setItems(filteredItems);
                    task.setCount(filteredItems.size());
                }
                result.setUndeal(task);
            });

            // sendTask
            Optional.ofNullable(m.getSendTasks()).ifPresent(i -> {
                result.setSendTask(BoardTaskMetricConvert.INSTANCE.toTask(i));
            });

            // approveTask
            Optional.ofNullable(m.getApproveTasks()).ifPresent(i -> {
                result.setApproveTask(BoardTaskMetricConvert.INSTANCE.toTask(i));
            });

            // applyTask
            Optional.ofNullable(m.getApplyTasks()).ifPresent(i -> {
                result.setApplyTask(BoardTaskMetricConvert.INSTANCE.toTask(i));
            });
        });
        return result;
    }

}
