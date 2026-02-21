package com.yunjin.org.convert;

import com.yunjin.org.pojo.dto.DepartmentRoleDTO;
import com.yunjin.org.pojo.entity.DepartmentRole;
import com.yunjin.org.pojo.vo.DepartmentRoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 部门角色 转换器
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Mapper(uses = {CommonConvert.class})
public interface DepartmentRoleConvert {

    DepartmentRoleConvert INSTANCE = Mappers.getMapper(DepartmentRoleConvert.class);


    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    List<DepartmentRoleDTO> toDTO(List<DepartmentRole> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    DepartmentRoleDTO toDTO(DepartmentRole entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    DepartmentRole toDO(DepartmentRoleDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    DepartmentRoleVO toVO(DepartmentRoleDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    List<DepartmentRoleVO> toVO(List<DepartmentRole> dos);
}
