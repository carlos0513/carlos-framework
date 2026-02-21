package com.yunjin.board.data.result;


import com.yunjin.form.pojo.enums.FormCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 看板数据-我得数据
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:09
 */
@Data
@Accessors(chain = true)
public class BoardMyDataResult extends BoardDataResult {

    private int total;
    private int size;
    private int pages;
    private int current;
    private List<Item> records;

    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    public static class Item implements Serializable {

        /**
         * 主键
         */
        private String id;
        /**
         * 报表名称
         */
        private String name;
        /**
         * 表单类型
         */
        private String type;
        /**
         * 创建部门
         */
        private String createDept;
        /**
         * 更新频率
         */
        private String updateFrequency;
        /**
         * 状态
         */
        private String status;
        /**
         * 数据条数
         */
        private int count;
        /**
         * 日期
         */
        private LocalDate date;

        /** 是否收藏 */
        private Boolean collectd;
        
        /** 是否导出 */
        private boolean exportFlag = false;

        /** 注册状态 */
        private Integer registerStatus;

        /** 表单类型 */
        private FormCategoryEnum formCategoryEnum;
    }
}
