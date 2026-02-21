package com.yunjin.org.pojo.enums;

import com.yunjin.core.enums.AppEnum;
import com.yunjin.core.enums.BaseEnum;
import com.yunjin.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 机构类型
 *
 * @author shenyong
 * @e-mail sheny60@chinaunicom.cn
 * @date 2024/10/15 11:06
 **/
@AppEnum(code = "DepartmentTypeEnum")
@Getter
@AllArgsConstructor
public enum DepartmentTypeEnum implements BaseEnum {
    COUNTY_01(0, "01-区-02", "02", "区"),
    COUNTY_02(1, "02-区-02", "02", "区政府部门"),
    COUNTY_21(2, "21-区-02", "02", "区政府处室"),
    COUNTY_29(3, "29-区-02", "02", "区企事业单位"),
    STREET_03(4, "03-镇（街道）-03", "03", "镇（街道）"),
    STREET_31(5, "31-镇（街道）-03", "03", "镇（街道）科室"),
    COMMUNITY_04(6, "04-村（社区）-04", "04", "村（社区）"),
    GRID_05(7, "05-网格-05", "05", "网格"),
    PROVINCE_01(8, "01-省-00", "00", "省"),
    PROVINCE_02(9, "02-省-00", "00", "省级部门"),
    CITY_01(10, "01-市-01", "01", "市"),
    CITY_02(11, "02-市-01", "01", "市级部门"),
    MICRO_GRID_01(12, "06-微网格-06", "06", "微网格"),
    ;

    private final Integer code;
    private final String deptType;
    private final String deptLevelCode;
    private final String desc;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static DepartmentTypeEnum codeOf(final String deptType) {
        final DepartmentTypeEnum[] values = DepartmentTypeEnum.values();
        for (final DepartmentTypeEnum value : values) {
            if (value.getDeptType().equals(deptType)) {
                return value;
            }
        }
        throw new ServiceException("机构类型不合法");
    }
}
