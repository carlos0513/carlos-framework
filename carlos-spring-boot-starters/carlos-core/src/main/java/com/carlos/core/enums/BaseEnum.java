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
public interface BaseEnum {

    /**
     * 获取枚举标识
     */
    // TODO: carlos 2020/9/24 优化方案，支持code多种数据类型，泛型的配置
    Integer getCode();

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
    default Enum getEnumVo() {
        Enum vo = new Enum();
        vo.setCode(getCode());
        vo.setDesc(getDesc());
        vo.setName(this.toString());
        return vo;
    }
}
