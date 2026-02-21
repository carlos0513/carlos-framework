package com.carlos.system.resource.convert;

import com.carlos.system.config.CommonConvert;
import com.carlos.system.pojo.ao.SysResourceAO;
import com.carlos.system.pojo.param.ApiSysResourceAddParam;
import com.carlos.system.resource.pojo.dto.SysResourceDTO;
import com.carlos.system.resource.pojo.dto.SysResourceGroupDTO;
import com.carlos.system.resource.pojo.dto.SysResourceTreeDTO;
import com.carlos.system.resource.pojo.entity.SysResource;
import com.carlos.system.resource.pojo.param.SysResourceCreateParam;
import com.carlos.system.resource.pojo.param.SysResourceUpdateParam;
import com.carlos.system.resource.pojo.vo.SysResourceGroupVO;
import com.carlos.system.resource.pojo.vo.SysResourceTreeVO;
import com.carlos.system.resource.pojo.vo.SysResourceVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 系统资源 转换器
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Mapper(uses = {CommonConvert.class})
public interface SysResourceConvert {

    SysResourceConvert INSTANCE = Mappers.getMapper(SysResourceConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    SysResourceDTO toDTO(SysResourceCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    SysResourceDTO toDTO(SysResourceUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    List<SysResourceDTO> toDTO(List<SysResource> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    SysResourceDTO toDTO(SysResource entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    SysResource toDO(SysResourceDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    SysResourceVO toVO(SysResourceDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    @Mapping(target = "categoryName", source = "categoryId", qualifiedByName = "toCategoryName")
    SysResourceVO toVO(SysResource dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    List<SysResourceVO> toVO(List<SysResource> dos);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return java.util.List<com.carlos.user.pojo.vo.ResourceVO>
     * @date 2021/12/29 11:46
     */
    List<SysResourceVO> toVOList(List<SysResourceDTO> dto);

    /**
     * toTreeVO
     *
     * @param resourceTree 参数0
     * @return java.util.List<com.carlos.sys.pojo.vo.ResourceTreeVO>
     * @author yunjin
     * @date 2022/1/4 15:13
     */
    List<SysResourceTreeVO> toTreeVO(List<SysResourceTreeDTO> resourceTree);

    /**
     * 分组列表
     *
     * @param resource 参数0
     * @return java.util.List<com.carlos.sys.pojo.vo.ResourceGroupVO>
     * @author yunjin
     * @date 2022/1/13 12:46
     */
    List<SysResourceGroupVO> toGroupVO(List<SysResourceGroupDTO> resource);


    SysResourceDTO toDTO(ApiSysResourceAddParam param);

    SysResourceAO toAO(SysResourceDTO sysResource);
}
