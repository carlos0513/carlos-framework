package com.yunjin.org.convert;

import com.yunjin.org.pojo.ao.RoleAO;
import com.yunjin.org.pojo.dto.RoleDTO;
import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.pojo.entity.Role;
import com.yunjin.org.pojo.param.RoleCreateParam;
import com.yunjin.org.pojo.param.RoleUpdateParam;
import com.yunjin.org.pojo.vo.RoleBaseVO;
import com.yunjin.org.pojo.vo.RoleDetailVO;
import com.yunjin.org.pojo.vo.RolePageVO;
import com.yunjin.org.pojo.vo.RoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 系统角色 转换器
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Mapper(uses = {CommonConvert.class})
public interface RoleConvert {

    RoleConvert INSTANCE = Mappers.getMapper(RoleConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    RoleDTO toDTO(RoleCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    RoleDTO toDTO(RoleUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    List<RoleDTO> toDTO(List<Role> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    RoleDTO toDTO(Role entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    Role toDO(RoleDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    RoleVO toVO(RoleDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    List<RoleVO> toVO(List<Role> dos);

    List<RolePageVO> toVOS(List<Role> dos);

    RoleVO toVO(Role dos);

    List<RoleVO> dto2vo(List<RoleDTO> dtos);

    List<RoleBaseVO> toBaseVO(List<RoleDTO> dtos);

    RoleDetailVO toDetailVO(RoleDTO role);

    @Mapping(target = "departmentName", source = "department")
    RoleDTO.UserInfo userDTOToInfo(UserDTO user);

    RoleAO toAO(RoleDTO dto);

    List<RoleAO> toAOS(List<RoleDTO> dto);

}
