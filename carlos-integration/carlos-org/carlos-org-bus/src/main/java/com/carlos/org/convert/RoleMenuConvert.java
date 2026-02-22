package com.carlos.org.convert;

import com.carlos.org.pojo.dto.RoleMenuDTO;
import com.carlos.org.pojo.entity.RoleMenu;
import com.carlos.org.pojo.vo.RoleMenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 角色菜单 转换器
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Mapper(uses = {CommonConvert.class})
public interface RoleMenuConvert {

    RoleMenuConvert INSTANCE = Mappers.getMapper(RoleMenuConvert.class);


    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    List<RoleMenuDTO> toDTO(List<RoleMenu> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    RoleMenuDTO toDTO(RoleMenu entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    RoleMenu toDO(RoleMenuDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    RoleMenuVO toVO(RoleMenuDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    List<RoleMenuVO> toVO(List<RoleMenu> dos);
}
