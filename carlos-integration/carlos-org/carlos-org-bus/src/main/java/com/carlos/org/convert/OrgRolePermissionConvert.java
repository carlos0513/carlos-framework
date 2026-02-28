package com.carlos.org.convert;

import com.carlos.org.pojo.dto.OrgRolePermissionDTO;
import com.carlos.org.pojo.entity.OrgRolePermission;
import com.carlos.org.pojo.param.OrgRolePermissionCreateParam;
import com.carlos.org.pojo.param.OrgRolePermissionUpdateParam;
import com.carlos.org.pojo.vo.OrgRolePermissionVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 角色权限 转换器
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Mapper(uses = {CommonConvert.class})
public interface OrgRolePermissionConvert {

    OrgRolePermissionConvert INSTANCE = Mappers.getMapper(OrgRolePermissionConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgRolePermissionDTO toDTO(OrgRolePermissionCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgRolePermissionDTO toDTO(OrgRolePermissionUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    List<OrgRolePermissionDTO> toDTO(List<OrgRolePermission> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgRolePermissionDTO toDTO(OrgRolePermission entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgRolePermission toDO(OrgRolePermissionDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgRolePermissionVO toVO(OrgRolePermissionDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    List<OrgRolePermissionVO> toVO(List<OrgRolePermission> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgRolePermissionVO toVO(OrgRolePermission entity);
}
