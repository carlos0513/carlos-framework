package com.yunjin.board.data.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 看板数据
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class BoardFieldMarketDataResult extends BoardDataResult {

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
         * 库名称
         */
        private String key;

        /**
         * 数据集
         */
        private int dataSet;


        /**
         * 数据项
         */
        private int dataItem;


        /**
         * 数据总量
         */
        private int dataVolumn;

    }

}
