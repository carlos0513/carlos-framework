package com.yunjin.org.convert;

import com.yunjin.org.pojo.dto.RoleResourceGroupDTO;
import com.yunjin.org.pojo.dto.RoleResourceGroupRefDTO;
import com.yunjin.org.pojo.entity.RoleResourceGroupRef;
import com.yunjin.org.pojo.param.RoleResourceGroupRefCreateParam;
import com.yunjin.org.pojo.param.RoleResourceGroupRefUpdateParam;
import com.yunjin.org.pojo.vo.RoleResourceGroupRefVO;
import com.yunjin.resource.convert.CommonConvert;
import com.yunjin.resource.pojo.dto.ResourceGroupItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 角色资源组关联表 转换器
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Mapper(uses = {CommonConvert.class})
public interface RoleResourceGroupRefConvert {

    RoleResourceGroupRefConvert INSTANCE = Mappers.getMapper(RoleResourceGroupRefConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    RoleResourceGroupRefDTO toDTO(RoleResourceGroupRefCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    RoleResourceGroupRefDTO toDTO(RoleResourceGroupRefUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    List<RoleResourceGroupRefDTO> toDTO(List<RoleResourceGroupRef> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    RoleResourceGroupRefDTO toDTO(RoleResourceGroupRef entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    RoleResourceGroupRef toDO(RoleResourceGroupRefDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    RoleResourceGroupRefVO toVO(RoleResourceGroupRefDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    List<RoleResourceGroupRefVO> toVO(List<RoleResourceGroupRef> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    RoleResourceGroupRefVO toVO(RoleResourceGroupRef entity);

    List<RoleResourceGroupDTO> toGroupDTOS(List<ResourceGroupItemDTO> itemList);
}
