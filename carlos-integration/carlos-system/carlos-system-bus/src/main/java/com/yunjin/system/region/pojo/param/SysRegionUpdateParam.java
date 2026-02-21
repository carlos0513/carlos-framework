package com.carlos.system.region.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 行政区域划分 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-8 19:30:25
 */
@Data
@Accessors(chain = true)
@Schema(value = "行政区域划分修改参数", description = "行政区域划分修改参数")
public class SysRegionUpdateParam {

    @NotNull(message = "自增ID不能为空")
    @Schema(value = "自增ID")
    private String id;
    @Schema(value = "行政区域编码")
    private String regionCode;
    @Schema(value = "行政区域名称")
    private String regionName;
    @Schema(value = "上级行政区域编码")
    private String parentCode;
    @Schema(value = "父级code集合 ,分隔")
    private String parents;
    @Schema(value = "备注")
    private String remark;
    @Schema(value = "区域类型(0：村下级，1：社区/村，2：街道/乡镇，3：区/县，4：市，5：省/直辖市)")
    private String regionType;
    @Schema(value = "区域面积(KM2)")
    private BigDecimal regionArea;
    @Schema(value = "区域人口")
    private Long regionPeopleNumber;
    @Schema(value = "区域级别")
    private Integer regionLevel;
    @Schema(value = "区域负责人")
    private String regionLeader;
    @Schema(value = "扩展字段值（网格：1、综合网格；2、专属网格；3、镇街网格；4村社区网格；5、基础网格）（社区：1、村社区；2、镇街社区；）")
    private Long extend;
    @Schema(value = "经度")
    private BigDecimal longitude;
    @Schema(value = "纬度")
    private BigDecimal latitude;
    @Schema(value = "行政区左边界经度")
    private BigDecimal boundLeft;
    @Schema(value = "行政区右边界经度")
    private BigDecimal boundRight;
    @Schema(value = "行政区下边界纬度")
    private BigDecimal boundBottom;
    @Schema(value = "行政区上边界纬度")
    private BigDecimal boundTop;
    @Schema(value = "gis标绘信息，大于0整数代表已标绘")
    private Long gisOid;
    @Schema(value = "排序")
    private int sort;
}
