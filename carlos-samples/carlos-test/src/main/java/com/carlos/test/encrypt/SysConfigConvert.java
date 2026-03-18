package com.carlos.test.encrypt;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * 系统配置 转换�?
 * </p>
 *
 * @author carlos
 * @date 2022-11-3 13:47:54
 */
// @Mapper(uses = {EncryptConvert.class})
@Mapper
public interface SysConfigConvert {

    SysConfigConvert INSTANCE = Mappers.getMapper(SysConfigConvert.class);


    /**
     * 数据传输对象转数据显示对�?
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2022-11-3 13:47:54
     */
    // @Mapping(source = "configName", target = "configName", qualifiedBy = EncryptMapper.class)
    SysConfig toDo(SysConfigDTO dto);


}
