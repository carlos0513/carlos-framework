package com.carlos.org.convert;

import com.carlos.org.api.pojo.ao.OrgUserAO;
import com.carlos.org.pojo.dto.OrgUserDTO;
import com.carlos.org.pojo.dto.OrgUserDeptDTO;
import com.carlos.org.pojo.dto.OrgUserPositionDTO;
import com.carlos.org.pojo.dto.OrgUserRoleDTO;
import com.carlos.org.pojo.entity.OrgUser;
import com.carlos.org.pojo.param.OrgUserCreateParam;
import com.carlos.org.pojo.param.OrgUserUpdateParam;
import com.carlos.org.pojo.vo.OrgUserDeptVO;
import com.carlos.org.pojo.vo.OrgUserPositionVO;
import com.carlos.org.pojo.vo.OrgUserRoleVO;
import com.carlos.org.pojo.vo.OrgUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 系统用户 转换器
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Mapper(uses = {CommonConvert.class})
public interface OrgUserConvert {

    OrgUserConvert INSTANCE = Mappers.getMapper(OrgUserConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgUserDTO toDTO(OrgUserCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgUserDTO toDTO(OrgUserUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    List<OrgUserDTO> toDTO(List<OrgUser> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgUserDTO toDTO(OrgUser entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgUser toDO(OrgUserDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgUserVO toVO(OrgUserDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    List<OrgUserVO> toVO(List<OrgUser> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgUserVO toVO(OrgUser entity);

    /**
     * 用户部门DTO转VO
     */
    OrgUserDeptVO toDeptVO(OrgUserDeptDTO dto);

    /**
     * 用户部门DTO列表转VO列表
     */
    List<OrgUserDeptVO> toDeptVOList(List<OrgUserDeptDTO> dtoList);

    /**
     * 用户角色DTO转VO
     */
    OrgUserRoleVO toRoleVO(OrgUserRoleDTO dto);

    /**
     * 用户角色DTO列表转VO列表
     */
    List<OrgUserRoleVO> toRoleVOList(List<OrgUserRoleDTO> dtoList);

    /**
     * 用户岗位DTO转VO
     */
    OrgUserPositionVO toPositionVO(OrgUserPositionDTO dto);

    /**
     * 用户岗位DTO列表转VO列表
     */
    List<OrgUserPositionVO> toPositionVOList(List<OrgUserPositionDTO> dtoList);

    OrgUserAO toAO(OrgUserDTO user);
}
