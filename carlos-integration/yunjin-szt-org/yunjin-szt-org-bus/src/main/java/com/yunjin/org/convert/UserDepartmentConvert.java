package com.yunjin.org.convert;

import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.pojo.dto.UserDepartmentDTO;
import com.yunjin.org.pojo.entity.UserDepartment;
import com.yunjin.org.pojo.param.CurDeptExecutorPageParam;
import com.yunjin.org.pojo.param.CurSubExecutorPageParam;
import com.yunjin.org.pojo.vo.UserDepartmentVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 用户部门 转换器
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Mapper(uses = {CommonConvert.class})
public interface UserDepartmentConvert {

    UserDepartmentConvert INSTANCE = Mappers.getMapper(UserDepartmentConvert.class);


    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    List<UserDepartmentDTO> toDTO(List<UserDepartment> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    UserDepartmentDTO toDTO(UserDepartment entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */

    UserDepartment toDO(UserDepartmentDTO dto);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    List<UserDepartment> toDO(List<UserDepartmentDTO> dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    @Mapping(target = "admin", source = "isAdmin")
    UserDepartmentVO toVO(UserDepartmentDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    List<UserDepartmentVO> toVO(List<UserDepartment> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    @Mapping(target = "admin", source = "isAdmin")
    UserDepartmentVO toVO(UserDepartment dos);

    /***
     *
     * @param dto
     * @return 用户部门关联显示对象
     * @author yunjin
     * @date 2022-11-14 12:21:46
     */
    UserDepartmentDTO userDtoToDepartmentDto(UserDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dtos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    List<UserDepartmentVO> dtoToVO(List<UserDepartmentDTO> dtos);

    CurSubExecutorPageParam toSubParam(CurDeptExecutorPageParam param);
}
