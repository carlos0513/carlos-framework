package com.yunjin.board.data.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 看板数据
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:09
 */
@Data
@Accessors(chain = true)
public class BoardDocumentInfoDataResult extends BoardDataResult {

    private List<Item> items;

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item implements Serializable {

        /**
         * id
         */
        private String id;
        /**
         * 标题
         */
        private String title;
        /**
         * 文件名
         */
        private String fileName;
        /**
         * 登录时间
         */
        private LocalDateTime updateTime;
        /**
         * 部门名称
         */
        private String url;
    }
}
