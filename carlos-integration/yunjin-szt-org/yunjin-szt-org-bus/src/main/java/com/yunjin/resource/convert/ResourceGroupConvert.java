package com.yunjin.resource.convert;

import com.yunjin.org.pojo.ao.ResourceGroupAO;
import com.yunjin.resource.pojo.dto.ResourceGroupDTO;
import com.yunjin.resource.pojo.entity.ResourceGroup;
import com.yunjin.resource.pojo.param.ResourceGroupCreateParam;
import com.yunjin.resource.pojo.param.ResourceGroupUpdateParam;
import com.yunjin.resource.pojo.vo.ResourceGroupVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 资源组 转换器
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Mapper(uses = {CommonConvert.class})
public interface ResourceGroupConvert {

    ResourceGroupConvert INSTANCE = Mappers.getMapper(ResourceGroupConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    ResourceGroupDTO toDTO(ResourceGroupCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    ResourceGroupDTO toDTO(ResourceGroupUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    List<ResourceGroupDTO> toDTO(List<ResourceGroup> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    ResourceGroupDTO toDTO(ResourceGroup entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    ResourceGroup toDO(ResourceGroupDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    ResourceGroupVO toVO(ResourceGroupDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    List<ResourceGroupVO> toVO(List<ResourceGroup> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    ResourceGroupVO toVO(ResourceGroup entity);

    List<ResourceGroupVO> toVOS(List<ResourceGroupDTO> listAll);

    ResourceGroupAO toAO(ResourceGroupDTO group);
    List<ResourceGroupAO> toAOS(List<ResourceGroupDTO> groups);
}
