package com.carlos.org.convert;

import com.carlos.org.pojo.ao.UserRoleAO;
import com.carlos.org.pojo.dto.UserRoleDTO;
import com.carlos.org.pojo.entity.UserDepartment;
import com.carlos.org.pojo.entity.UserRole;
import com.carlos.org.pojo.vo.UserRoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 用户角色 转换器
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Mapper(uses = {CommonConvert.class})
public interface UserRoleConvert {

    UserRoleConvert INSTANCE = Mappers.getMapper(UserRoleConvert.class);


    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    List<UserRoleDTO> toDTO(List<UserRole> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    UserRoleDTO toDTO(UserRole entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    UserRole toDO(UserRoleDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    UserRoleVO toVO(UserRoleDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    List<UserRoleVO> toVO(List<UserRole> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    List<UserRole> toDO(List<UserRoleDTO> dos);

    List<UserRoleVO> dto2vo(List<UserRoleDTO> userRoles);

    List<UserRoleAO> toAO(List<UserRoleDTO> userRoles);

    List<UserRoleAO> userDeptRoleToAO(List<UserDepartment> userRoles);
}
