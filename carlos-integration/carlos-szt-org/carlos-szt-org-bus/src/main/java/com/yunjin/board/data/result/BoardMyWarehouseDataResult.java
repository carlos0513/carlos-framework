package com.yunjin.board.data.result;

import com.yunjin.warehouse.pojo.enums.WarehouseReportSourceType;
import com.yunjin.warehouse.pojo.enums.WarehouseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @Title: BoardMyWarehouseDataResult
 * @Author fxd
 * @Date 2025/12/2 13:50
 * @description: 我的数仓收藏数据Result
 */
@Data
@Accessors(chain = true)
public class BoardMyWarehouseDataResult extends BoardDataResult{

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
         * 是否自建数据
         */
        private Boolean isSelfData = false;
        /**
         * 数仓类型(2基础公用、1部门数仓、4我的数仓、3任务报表)
         */
        private WarehouseType fromType;
        /**
         * 数据来源类型
         */
        private WarehouseReportSourceType sourceType;
        /**
         * 来源id 对方数据表id
         */
        private String sourceId;
        /**
         * 制发单位id
         */
        private String createDeptId;
        /**
         * 制发单位名称
         */
        private String createDeptName;
        /**
         * 来源单位id
         */
        private String sourceDeptId;
        /**
         * 来源单位名称
         */
        private String sourceDeptName;
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
    }



}
