package com.yunjin.docking.jct;

import cn.hutool.core.codec.Base64;
import cn.hutool.extra.spring.SpringUtil;
import com.google.common.collect.Sets;
import com.yunjin.docking.jct.api.LJAppAggrPushUserParam;
import com.yunjin.docking.jct.config.LJAppAggrConstant;
import com.yunjin.docking.jct.config.LJAppAggrProperties;
import com.yunjin.docking.jct.exception.DockingLJAppAggrException;
import com.yunjin.docking.jct.param.LJAppAggrParseTokenParam;
import com.yunjin.docking.jct.result.LJAppAggrResult;
import com.yunjin.docking.jct.result.LJAppAggrUser;
import com.yunjin.org.pojo.dto.OrgDockingMappingDTO;
import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.pojo.emuns.OrgUserDockingOperateTypeEnum;
import com.yunjin.org.pojo.enums.OrgDockingTypeEnum;
import com.yunjin.org.pojo.enums.UserGenderEnum;
import com.yunjin.org.service.RoleService;
import com.yunjin.org.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * <p>
 * 引擎相关服务
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:05
 */
@Slf4j
@RequiredArgsConstructor
public class LJAppAggrService {

    private final FeignLJAppAggr feign;

    private final LJAppAggrProperties properties;


    @PostConstruct
    public void init() {

    }

    public LJAppAggrUser getUserInfo(String token) {
        LJAppAggrResult<LJAppAggrUser> result;
        try {
            LJAppAggrParseTokenParam param = new LJAppAggrParseTokenParam();
            param.setAccessToken(token);
            param.setAppSign(properties.getAppSign());
            result = feign.getUserInfo(param);
        } catch (Exception e) {
            log.error("request LJAppAggrAuth allUser error", e);
            throw new DockingLJAppAggrException(e);
        }
        return result.getData();
    }

    public List<LJAppAggrUser> listAll() {
        LJAppAggrResult<List<LJAppAggrUser>> result = null;
        try {
            result = feign.allUser(properties.getAppSign());
        } catch (Exception e) {
            log.error("request LJAppAggrAuth allUser error", e);
            throw new DockingLJAppAggrException(e);
        }
        return result.getData();
    }

    public void receiveUser(LJAppAggrPushUserParam param) {
        // 解析用户，添加用户
        Integer integer = param.getStatus();
        OrgUserDockingOperateTypeEnum operateType;
        switch (integer) {
            case 1:
                operateType = OrgUserDockingOperateTypeEnum.ADD;
                break;
            case 2:
                operateType = OrgUserDockingOperateTypeEnum.UPDATE;
                break;
            case 3:
                operateType = OrgUserDockingOperateTypeEnum.DISABLE;
                break;
            case 4:
                operateType = OrgUserDockingOperateTypeEnum.ENABLE;
                break;
            case 5:
                operateType = OrgUserDockingOperateTypeEnum.SIGN_OFF;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + integer);
        }
        UserService userService = SpringUtil.getBean(UserService.class);
        OrgDockingMappingDTO mapping = new OrgDockingMappingDTO();
        mapping.setTargetId(param.getOpenId());
        mapping.setTargetCode(LJAppAggrConstant.LJ_USER);
        mapping.setDockingType(OrgDockingTypeEnum.USER);

        String phoneNumber = param.getPhoneNumber();
        UserDTO user = new UserDTO();
        user.setAccount(param.getUserName());
        user.setRealname(param.getNickName());
        user.setPwd(Base64.encode(phoneNumber));
        user.setConfirmPwd(Base64.encode(phoneNumber));
        user.setPhone(phoneNumber);
        user.setGender(UserGenderEnum.UNKNOWN);
        user.setCreateBy("ljc");


        RoleService roleService = SpringUtil.getBean(RoleService.class);
        // RoleDTO role = new RoleDTO();
        // role.setName("默认角色");
        // role = roleService.getOrAdd(role);
        // roleService.getOrAdd(role);
        user.setRoleIds(Sets.newHashSet(properties.getDefaultRoleId()));

        user.setDepartmentIds(Sets.newHashSet(properties.getDefaultDeptId()));
        userService.dockingUser(user, mapping, operateType);
    }
}
