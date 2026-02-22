package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.experimental.Accessors;


/**
 * <p>
 * 部门 新增参数封装
 * </p>
 *
 * @author yunjin
 */
@Accessors(chain = true)
@Schema(description = "部门新增参数")
public class DepartmentCreateParam {

    @Schema(description = "主键")
    private String id;
    @Schema(description = "上级机构")
    private String parentId;
    @NotBlank(message = "部门名称不能为空")
    @Schema(description = "部门名称")
    private String deptName;
    @Schema(description = "机构地址")
    private String address;
    @Schema(description = "机构排序")
    private int sort;
    @Schema(description = "组织机构类型")
    private String departmentType;
    @Schema(description = "管辖区域")
    private String regionCode;
    @Schema(description = "对方系统数据id")
    private String targetId;
    @Schema(description = "对方系统数据code")
    private String targetCode;
    @Schema(description = "第三方部门id")
    private String thirdDeptId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(String departmentType) {
        this.departmentType = departmentType;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public String getThirdDeptId() {
        return thirdDeptId;
    }

    public void setThirdDeptId(String thirdDeptId) {
        this.thirdDeptId = thirdDeptId;
    }
}
