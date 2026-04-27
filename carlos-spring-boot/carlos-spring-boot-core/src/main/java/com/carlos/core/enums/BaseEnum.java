package com.carlos.core.enums;


/**
 * <p>
 * 枚举类型父接口 若需要枚举字典则必须实现该接口
 *
 * </p>
 *
 * @author carlos
 * @date 2020/4/10 15:41
 */
public interface BaseEnum<T> {

    /**
     * 获取枚举标识
     */
    T getCode();

    /**
     * 获取枚举描述
     */
    String getDesc();

    /**
     * 获取枚举描述信息
     *
     * @return com.carlos.core.enums.EnumVo
     * @author carlos
     * @date 2021/11/25 14:03
     */
    default EnumInfo getEnumVo() {
        EnumInfo vo = new EnumInfo();
        vo.setCode(getCode());
        vo.setDesc(getDesc());
        vo.setName(this.toString());
        return vo;
    }
}
