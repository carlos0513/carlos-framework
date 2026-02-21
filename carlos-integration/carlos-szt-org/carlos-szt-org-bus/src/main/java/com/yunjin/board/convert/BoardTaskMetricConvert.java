package com.yunjin.board.convert;

import cn.hutool.core.collection.CollUtil;
import com.yunjin.board.data.result.BoardCustomTaskDataResult;
import com.yunjin.metric.TaskMetric;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: BoardTaskMetricConvert
 * @Author fxd
 * @Date 2025/12/4 15:46
 * @description: BoardCustomTaskDataHandler 转换器
 */
@Mapper(uses = {CommonConvert.class})
public interface BoardTaskMetricConvert {

    BoardTaskMetricConvert INSTANCE = Mappers.getMapper(BoardTaskMetricConvert.class);

    /**
     * TaskMetric.TaskList 转 BoardCustomTaskDataResult.Task
     */
    @Mapping(target = "executeLive", ignore = true)
    BoardCustomTaskDataResult.Item toTaskItem(TaskMetric.TaskItem source);

    // 转换TaskList
    default BoardCustomTaskDataResult.Task toTask(TaskMetric.TaskList source) {
        if (source == null) {
            return null;
        }
        BoardCustomTaskDataResult.Task target = new BoardCustomTaskDataResult.Task();
        target.setCount(source.getCount());

        if (CollUtil.isNotEmpty(source.getItems())) {
            List<BoardCustomTaskDataResult.Item> items = source.getItems().stream()
                    .map(this::toTaskItemWithExecuteLive)
                    .collect(Collectors.toList());
            target.setItems(items);
        }
        return target;
    }


    default BoardCustomTaskDataResult.Item toTaskItemWithExecuteLive(TaskMetric.TaskItem source) {
        BoardCustomTaskDataResult.Item target = toTaskItem(source);
        afterConvertTaskItem(source, target);
        return target;
    }

    default void afterConvertTaskItem(TaskMetric.TaskItem source,
                                      BoardCustomTaskDataResult.Item target) {
        if (source != null && source.getExecuteLive() != null) {
            BoardCustomTaskDataResult.TaskExecuteLive executeLive =
                    new BoardCustomTaskDataResult.TaskExecuteLive();
            executeLive.setStatus(source.getExecuteLive().getStatus());
            executeLive.setTimeDiff(source.getExecuteLive().getTimeDiff());
            executeLive.setCompleteTime(source.getExecuteLive().getCompleteTime());
            target.setExecuteLive(executeLive);
        }
    }

    // List转换方法
    default List<BoardCustomTaskDataResult.Item> toTaskItemList(List<TaskMetric.TaskItem> source) {
        if (CollUtil.isEmpty(source)) {
            return new ArrayList<>();
        }
        return source.stream()
                .map(this::toTaskItemWithExecuteLive)
                .collect(Collectors.toList());
    }

}
