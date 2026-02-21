package com.yunjin.docking.tftd.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户列表
 * </p>
 *
 * @author Carlos
 * @date 2022/3/8 12:18
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TfDeptInfoResult {

    /**
     * deptId
     */
    @JsonProperty("deptId")
    private String deptId;
    /**
     * name
     */
    @JsonProperty("name")
    private String name;
    /**
     * createTime
     */
    @JsonProperty("createTime")
    private String createTime;
    /**
     * delFlag
     */
    @JsonProperty("delFlag")
    private String delFlag;
    /**
     * deptCode
     */
    @JsonProperty("deptCode")
    private String deptCode;
    /**
     * deptLevel
     */
    @JsonProperty("deptLevel")
    private String deptLevel;
    /**
     * deptNamePath
     */
    @JsonProperty("deptNamePath")
    private String deptNamePath;
    /**
     * deptPidPath
     */
    @JsonProperty("deptPidPath")
    private String deptPidPath;
    /**
     * deptRegion
     */
    @JsonProperty("deptRegion")
    private String deptRegion;
    /**
     * deptType
     */
    @JsonProperty("deptType")
    private String deptType;
    /**
     * deptTypeDesc
     */
    @JsonProperty("deptTypeDesc")
    private String deptTypeDesc;
    /**
     * parentId
     */
    @JsonProperty("parentId")
    private String parentId;
    /**
     * regionCode
     */
    @JsonProperty("regionCode")
    private String regionCode;
    /**
     * regionId
     */
    @JsonProperty("regionId")
    private String regionId;
    /**
     * remark
     */
    @JsonProperty("remark")
    private String remark;
    /**
     * sortOrder
     */
    @JsonProperty("sortOrder")
    private Integer sortOrder;
    /**
     * updateTime
     */
    @JsonProperty("updateTime")
    private String updateTime;
    /**
     * childrens
     */
    @JsonProperty("childrens")
    private List<TfDeptInfoResult> childrens;
}
