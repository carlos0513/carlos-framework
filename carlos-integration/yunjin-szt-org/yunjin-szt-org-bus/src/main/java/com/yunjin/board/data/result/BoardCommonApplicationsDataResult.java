package com.yunjin.board.data.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 看板数据-常用应用
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:09
 */
@Data
@Accessors(chain = true)
public class BoardCommonApplicationsDataResult extends BoardDataResult {


    /**
     * 应用
     */
    private List<Item> items;

    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    public static class Item implements Serializable {

        /**
         * id
         */
        private String id;
        /**
         * 路径
         */
        private String path;
        /**
         * 标题
         */
        private String title;
        /**
         * 图标
         */
        private String icon;
        /**
         * 备注
         */
        private String remark;
    }
}
