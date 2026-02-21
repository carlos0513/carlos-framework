package com.yunjin.board.data.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 看板数据-数据超市
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class BoardDataMarketDataResult extends BoardDataResult {
    /**
     * 用户id
     */
    private List<Item> items;


    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item implements Serializable {

        /**
         * 数据名称
         */
        private String name;

        /**
         * 所属部门
         */
        private String deptName;

        /**
         * 更新时间
         */
        private LocalDateTime updateTime;

        /**
         * 数据总量
         */
        private int dataCount;

        /**
         * 浏览总量
         */
        private int viewCount;
        /**
         * 数据总量
         */
        private int sort;

    }
}
