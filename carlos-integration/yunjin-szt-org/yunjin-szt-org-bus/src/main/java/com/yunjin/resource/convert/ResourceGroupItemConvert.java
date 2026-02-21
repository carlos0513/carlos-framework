package com.yunjin.resource.convert;

import com.yunjin.resource.pojo.dto.ResourceGroupItemDTO;
import com.yunjin.resource.pojo.entity.ResourceGroupItem;
import com.yunjin.resource.pojo.param.ResourceGroupItemCreateParam;
import com.yunjin.resource.pojo.param.ResourceGroupItemUpdateParam;
import com.yunjin.resource.pojo.vo.ResourceGroupItemVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 资源组详情项 转换器
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Mapper(uses = {CommonConvert.class})
public interface ResourceGroupItemConvert {

    ResourceGroupItemConvert INSTANCE = Mappers.getMapper(ResourceGroupItemConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    ResourceGroupItemDTO toDTO(ResourceGroupItemCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    ResourceGroupItemDTO toDTO(ResourceGroupItemUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    List<ResourceGroupItemDTO> toDTO(List<ResourceGroupItem> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    ResourceGroupItemDTO toDTO(ResourceGroupItem entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    ResourceGroupItem toDO(ResourceGroupItemDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    ResourceGroupItemVO toVO(ResourceGroupItemDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    List<ResourceGroupItemVO> toVO(List<ResourceGroupItem> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    ResourceGroupItemVO toVO(ResourceGroupItem entity);
}
