package com.yunjin.board.data.result;


import com.yunjin.json.jackson.annotation.DepartmentIdField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 看板数据-区市县报表数
 *
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:09
 */
@Data
@Accessors(chain = true)
public class BoardReportNumberDataResult extends BoardDataResult {

    /**
     * 区域列表
     */
    private List<Region> regions;

    // TODO: Carlos 2025-05-15 该对象内容待确认


    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Region implements Serializable {

        /**
         * 区市县名称
         */
        @DepartmentIdField
        private String regionName;

        /**
         * 市
         */
        private DataItem city;

        /**
         * 区
         */
        private DataItem area;

    }


    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataItem implements Serializable {

        /**
         * 市级下发
         */
        private int cityTableNum;


        /**
         * 镇街
         */
        private int townTableNum;


        /**
         * 村社区
         */
        private int villageTableNum;
    }

}
