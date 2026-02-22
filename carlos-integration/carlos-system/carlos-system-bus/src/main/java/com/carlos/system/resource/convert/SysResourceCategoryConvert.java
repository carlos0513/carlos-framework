package com.carlos.system.resource.convert;

import com.carlos.system.config.CommonConvert;
import com.carlos.system.pojo.param.ApiResourceCategoryAddParam;
import com.carlos.system.resource.pojo.dto.ResourceCategoryDTO;
import com.carlos.system.resource.pojo.entity.SysResourceCategory;
import com.carlos.system.resource.pojo.param.SysResourceCategoryCreateParam;
import com.carlos.system.resource.pojo.param.SysResourceCategoryUpdateParam;
import com.carlos.system.resource.pojo.vo.SysResourceCategoryRecursionVO;
import com.carlos.system.resource.pojo.vo.SysResourceCategoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 资源分类 转换器
 * </p>
 *
 * @author carlos
 * @date 2022-1-5 17:23:27
 */
@Mapper(uses = {CommonConvert.class})
public interface SysResourceCategoryConvert {

    SysResourceCategoryConvert INSTANCE = Mappers.getMapper(SysResourceCategoryConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    ResourceCategoryDTO toDTO(SysResourceCategoryCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    ResourceCategoryDTO toDTO(SysResourceCategoryUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    List<ResourceCategoryDTO> toDTO(List<SysResourceCategory> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    ResourceCategoryDTO toDTO(SysResourceCategory entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    SysResourceCategory toDO(ResourceCategoryDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    @Mapping(target = "haveChildren", source = "id", qualifiedByName = "haveChildrenCategory")
    SysResourceCategoryVO toVO(ResourceCategoryDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    List<SysResourceCategoryVO> toVO(List<SysResourceCategory> dos);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dtos 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2022-1-5 16:12:38
     */
    List<SysResourceCategoryVO> toListVO(List<ResourceCategoryDTO> dtos);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dtos 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2022-1-5 16:12:38
     */
    List<SysResourceCategoryRecursionVO> toRecursionListVO(List<ResourceCategoryDTO> dtos);

    /**
     * 三方接口参数转数据传输对象
     *
     * @param param 参数0
     * @return com.carlos.system.resource.pojo.dto.ResourceCategoryDTO
     * @author Carlos
     * @date 2023/6/28 17:31
     */
    ResourceCategoryDTO toDTO(ApiResourceCategoryAddParam param);
}
