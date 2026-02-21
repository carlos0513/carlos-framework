package com.yunjin.board.data.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.board.data.query.BoardRefluxDataDataQuery;
import com.yunjin.board.data.result.BoardRefluxDataResult;
import com.yunjin.core.response.Result;
import com.yunjin.warehouse.metric.ApiWarehouseMetric;
import com.yunjin.warehouse.metric.WarehouseMetric;
import com.yunjin.warehouse.metric.WarehouseMetricEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * <p>
 * 回流数据
 * </p>
 *
 * @author Carlos
 * @date 2025-05-20 17:12
 */
@Slf4j
public class BoardRefluxDataDataHandler extends AbstractBoardDataHandler<BoardRefluxDataDataQuery, BoardRefluxDataResult> {

    @Override
    public BoardRefluxDataResult getData(Map<String, Object> param) {
        BoardRefluxDataDataQuery query = convertQueryParams(param);
        BoardRefluxDataResult result = new BoardRefluxDataResult();

        ApiWarehouseMetric metricApi = SpringUtil.getBean(ApiWarehouseMetric.class);
        Map<WarehouseMetricEnum, Object> map = new HashMap<>(2);
        map.put(WarehouseMetricEnum.refluxData, query);
        map.put(WarehouseMetricEnum.refluxDataRank, query);

        Result<WarehouseMetric> metric = metricApi.getMetric(map);
        if (!metric.getSuccess()) {
            log.error("Api ApiWarehouseMetric.getMetric  request failed, message: {}, detail message:{}", metric.getMessage(), metric.getStack());
            result.setErrMsg(metric.getMessage());
            return result;
        }
        // 设置回流数据统计
        Optional.ofNullable(metric.getData())
                .map(WarehouseMetric::getRefluxData)
                .ifPresent(refluxData -> {
                    result.setRefluxDatasetCount(refluxData.getRefluxDatasetCount());
                    result.setRefluxDataItemCount(refluxData.getRefluxDataItemCount());
                    result.setRefluxDataVolume(refluxData.getRefluxDataVolume());
                });

        // 设置回流数据排行
        Optional.ofNullable(metric.getData())
                .map(WarehouseMetric::getRefluxDataRank)
                .ifPresent(refluxDataRank -> {
                    List<BoardRefluxDataResult.TopicRank> topicRanks = new ArrayList<>();

                    for (WarehouseMetric.TopicRank metricTopicRank : refluxDataRank.getTopicRanks()) {
                        BoardRefluxDataResult.TopicRank resultTopicRank = new BoardRefluxDataResult.TopicRank();
                        resultTopicRank.setTopicName(metricTopicRank.getTopicName());

                        List<BoardRefluxDataResult.LevelCount> levelCounts = new ArrayList<>();
                        for (WarehouseMetric.LevelCount metricLevelCount : metricTopicRank.getLevelCounts()) {
                            levelCounts.add(new BoardRefluxDataResult.LevelCount(
                                    metricLevelCount.getLevelName(),
                                    metricLevelCount.getDataCount(),
                                    metricLevelCount.getColor()
                            ));
                        }

                        resultTopicRank.setLevelCounts(levelCounts);
                        topicRanks.add(resultTopicRank);
                    }

                    result.setRefluxRank(topicRanks);

                    // 新增：设置颜色数组
                    if (refluxDataRank.getColorForRank() != null) {
                        result.setColorForRank(refluxDataRank.getColorForRank());
                    }
                });

        return result;

    }
}
