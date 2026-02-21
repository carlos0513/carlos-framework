package com.yunjin.board.data.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
public class ApplicationCaseDataResult extends BoardDataResult {
    private List<Item> items;

    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    public static class Item implements Serializable {
        private static final long serialVersionUID = 1L;
        @Schema(value = "主键ID")
        private String id;
        @Schema(value = "场景名称")
        private String name;
        @Schema(value = "排序")
        private Integer sort;
        @Schema(value = "场景LOGO")
        private String logo;
        @Schema(value = "场景LOGOID")
        private String logoId;
        @Schema(value = "说明")
        private String description;
        @Schema(value = "可见范围，0：非全员可见，1：全员可见")
        private String visible;
        @Schema(value = "创建者编号")
        private String createBy;
        @Schema(value = "创建时间")
        private LocalDateTime createTime;
        @Schema(value = "更新者编号")
        private String updateBy;
        @Schema(value = "更新时间")
        private LocalDateTime updateTime;
        @Schema(value = "可见部门IDS")
        private Set<String> deptIds;
        @Schema(value = "可见角色IDS")
        private Set<String> roleIds;
        @Schema(value = "可见人员IDS")
        private Set<String> userIds;
        //        @Schema(value = "场景下资源信息")
//        private List<GridScenarioResourceVO> resources;
        @Schema(value = "场景分类")
        private String classification;
        @Schema(value = "采数报表数量")
        private Long reportNum;
        @Schema(value = "取数报表数量")
        private Long formNum;
        @Schema(value = "分析报表数量")
        private Long biNum;
    }
}
