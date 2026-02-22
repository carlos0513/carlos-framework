package com.carlos.org.convert;

import com.carlos.core.base.UserInfo;
import com.carlos.org.pojo.ao.UserDeptRoleAO;
import com.carlos.org.pojo.ao.UserDetailAO;
import com.carlos.org.pojo.ao.UserLoginAO;
import com.carlos.org.pojo.dto.UserDTO;
import com.carlos.org.pojo.dto.UserDeptRoleDTO;
import com.carlos.org.pojo.entity.User;
import com.carlos.org.pojo.excel.UserExcel;
import com.carlos.org.pojo.param.*;
import com.carlos.org.pojo.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 系统用户 转换器
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Mapper(uses = {CommonConvert.class})
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    UserDTO toDTO(UserCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    UserDTO toDTO(UserUpdateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    UserDTO toDTO(UserResetPwdParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    List<UserDTO> toDTO(List<User> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    @Mapping(target = "admin", source = "isAdmin")
    UserDTO toDTO(User entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    @Mapping(target = "isAdmin", source = "admin")
    User toDO(UserDTO dto);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param param 数据传输对象
     * @return 数据传输对象列表
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    User toDto(UserChangePwdParam param);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    UserSessionVO toVO(UserDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    List<UserSessionVO> toVO(List<User> dos);

    @Mapping(target = "admin", source = "isAdmin")
    UserSessionVO toVO(User entity);

    /**
     * 登录用户信息转化
     *
     * @param dto 参数0
     * @return com.carlos.core.base.UserInfo
     * @author Carlos
     * @date 2022/3/7 16:04
     */
    @Mapping(target = "name", source = "account")
    //@Mapping(target = "nickName", source = "nickname")
    @Mapping(target = "realName", source = "realname")
    UserInfo toUser(UserDTO dto);


    /**
     * @Title: dtoToexcel
     * @Description: 导出数据对象转化
     * @Date: 2023/2/20 14:41
     * @Parameters: [userDTOS]
     */
    List<UserExcel> dtoToexcel(List<UserDTO> userDTOS);


    @Mapping(source = "gender", target = "gender", qualifiedByName = "toGender")
    UserDTO toDTO(UserExcel userExcel);

    @Mapping(target = "role", source = "roleNames")
    UserPageVO toPage(User item);

    UserBaseInfoVO toBaseVO(UserDTO user);

    List<UserDeptRoleVO> toListVO2(List<UserDeptRoleAO> list);

    UserDTO toDTO(UserForgetPwdParam param);

    List<UserListVO> toListVO(List<UserDTO> dtos);

    UserDetailVO toDetailVO(UserDTO userById);

    @Mapping(target = "department", source = "departmentInfo")
    @Mapping(target = "regionCode", source = "departmentInfo.regionCode")
    UserLoginAO dtoToAo(UserDTO user);

    List<UserInfo> dtoToAos(List<UserDTO> users);

    List<UserInfo> toUserInfo(List<UserDTO> userDTOS);

    UserDetailAO dtoToDetailAo(UserDTO user);

    List<UserDetailAO> dtoToAo(List<UserDTO> users);

    List<UserDeptRoleAO> userDeptRole(List<UserDeptRoleDTO> users);
}
