package com.carlos.system.region.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.carlos.json.jackson.annotation.UserIdField;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 行政区域划分 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-8 19:30:25
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRegionVO implements Serializable {

    private static final long serialVersionUID = 1L;
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
    // @UserIdField(type = UserIdField.SerializerType.REALNAME)
    // @ApiModelProperty(value = "创建人")
    // private String createBy;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
    @ApiModelProperty(value = "子级数目")
    private Long subNum;

    @ApiModelProperty(value = "子区域信息")
    List<SysRegionVO> children = new ArrayList<>();

}
