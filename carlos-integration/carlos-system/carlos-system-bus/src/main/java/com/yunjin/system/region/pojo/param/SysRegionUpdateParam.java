package com.carlos.system.region.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
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
@ApiModel(value = "行政区域划分修改参数", description = "行政区域划分修改参数")
public class SysRegionUpdateParam {

    @NotNull(message = "自增ID不能为空")
    @ApiModelProperty(value = "自增ID")
    private String id;
    @ApiModelProperty(value = "行政区域编码")
    private String regionCode;
    @ApiModelProperty(value = "行政区域名称")
    private String regionName;
    @ApiModelProperty(value = "上级行政区域编码")
    private String parentCode;
    @ApiModelProperty(value = "父级code集合 ,分隔")
    private String parents;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "区域类型(0：村下级，1：社区/村，2：街道/乡镇，3：区/县，4：市，5：省/直辖市)")
    private String regionType;
    @ApiModelProperty(value = "区域面积(KM2)")
    private BigDecimal regionArea;
    @ApiModelProperty(value = "区域人口")
    private Long regionPeopleNumber;
    @ApiModelProperty(value = "区域级别")
    private Integer regionLevel;
    @ApiModelProperty(value = "区域负责人")
    private String regionLeader;
    @ApiModelProperty(value = "扩展字段值（网格：1、综合网格；2、专属网格；3、镇街网格；4村社区网格；5、基础网格）（社区：1、村社区；2、镇街社区；）")
    private Long extend;
    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;
    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;
    @ApiModelProperty(value = "行政区左边界经度")
    private BigDecimal boundLeft;
    @ApiModelProperty(value = "行政区右边界经度")
    private BigDecimal boundRight;
    @ApiModelProperty(value = "行政区下边界纬度")
    private BigDecimal boundBottom;
    @ApiModelProperty(value = "行政区上边界纬度")
    private BigDecimal boundTop;
    @ApiModelProperty(value = "gis标绘信息，大于0整数代表已标绘")
    private Long gisOid;
    @ApiModelProperty(value = "排序")
    private int sort;
}
