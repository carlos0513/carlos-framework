package com.yunjin.board.data.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 看板数据-任务
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:09
 */
@Data
@Accessors(chain = true)
public class BoardTaskOverviewDataResult extends BoardDataResult {


    private List<Item> items;


    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class Item {
        private String key;

        /**
         * 任务数量
         */
        private int taskCount;
        /**
         * 延期数量
         */
        private int overdueCount;
    }
}
