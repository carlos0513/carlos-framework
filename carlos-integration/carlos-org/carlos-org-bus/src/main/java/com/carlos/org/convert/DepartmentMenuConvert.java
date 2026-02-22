package com.carlos.org.convert;

import com.carlos.org.pojo.dto.DepartmentMenuDTO;
import com.carlos.org.pojo.entity.DepartmentMenu;
import com.carlos.org.pojo.vo.DepartmentMenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 部门菜单表 转换器
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Mapper(uses = {CommonConvert.class})
public interface DepartmentMenuConvert {

    DepartmentMenuConvert INSTANCE = Mappers.getMapper(DepartmentMenuConvert.class);


    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    List<DepartmentMenuDTO> toDTO(List<DepartmentMenu> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    DepartmentMenuDTO toDTO(DepartmentMenu entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    List<DepartmentMenu> toDO(List<DepartmentMenuDTO> dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    DepartmentMenuVO toVO(DepartmentMenuDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    List<DepartmentMenuVO> toVO(List<DepartmentMenu> dos);
}
