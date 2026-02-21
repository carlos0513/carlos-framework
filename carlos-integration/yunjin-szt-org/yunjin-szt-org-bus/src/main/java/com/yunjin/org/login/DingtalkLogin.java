package com.yunjin.org.login;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.docking.dingtalk.DingtalkUtil;
import com.yunjin.org.pojo.dto.DepartmentDTO;
import com.yunjin.org.pojo.dto.RoleDTO;
import com.yunjin.org.service.RoleService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Description: 钉钉登录
 * @Date: 2023/7/12 16:28
 */
@Slf4j
public class DingtalkLogin implements ThridLogin<Map<String, String>> {
    @Override
    public String login(Map<String, String> param) {
        log.info("dingtalk login param:{}", param);
        String code = param.get("code");
        if (StrUtil.isBlank(code)) {
            throw new ServiceException("code不能为空");
        }
        OapiV2UserGetResponse result;
        OapiV2UserGetResponse.UserGetResponse userInfo;
        try {
            OapiV2UserGetuserinfoResponse.UserGetByCodeResponse user = DingtalkUtil.getUserId(code);
            result = DingtalkUtil.getUserInfo(user.getUserid());
            userInfo = result.getResult();
            log.info("Get dingtalk userinfo success: code:{}  userinfo:{}", code, userInfo);
        } catch (Exception e) {
            log.error("dingtalk login failed, code:{}", code, e);
            throw new ServiceException("钉钉用户信息获取失败！");
        }
        String mobile = userInfo.getMobile();
        // 判断用户是否存在，如果用户不存在，注册新用户
        // UserService userService = SpringUtil.getBean(UserService.class);
        // UserDTO user = userService.getUserByPhone(mobile);
        // if (user == null) {
        //     user = new UserDTO();
        //     // 注册新用户
        //     List<Long> deptIdList = userInfo.getDeptIdList();
        //     user.setAccount(mobile);
        //     user.setRealname(userInfo.getName());
        //     user.setPhone(mobile);
        //     user.setEmail(userInfo.getEmail());
        //     HashSet<String> deptIds = Sets.newHashSet();
        //     for (Long deptId : deptIdList) {
        //         DepartmentDTO dept = createDepartmentRecursion(deptId);
        //         if (dept != null) {
        //             deptIds.add(dept.getId());
        //         }
        //     }
        //     user.setDepartmentIds(deptIds);
        //     // 获取角色
        //     String roleId = getDefaultRoleId();
        //     user.setRoleIds(Sets.newHashSet(roleId));
        //     userService.addUser(user);
        //     log.info("钉钉用户注册成功:{}", user);
        //
        // }
        return mobile;
    }

    /**
     * 获取默认角色id
     *
     * @return java.lang.String
     * @throws
     * @author Carlos
     * @date 2024/7/2 10:16
     */
    private String getDefaultRoleId() {
        RoleService roleService = SpringUtil.getBean(RoleService.class);
        RoleDTO role = roleService.getOrAdd(new RoleDTO().setName("默认角色"));
        return role.getId();
    }

    private DepartmentDTO createDepartmentRecursion(Long deptId) {
        OapiV2DepartmentGetResponse.DeptGetResponse departmentInfo = DingtalkUtil.getDepartmentInfo(deptId);
        Long parentId = departmentInfo.getParentId();
        if (parentId == 1) {
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setDeptName(departmentInfo.getName());
            departmentDTO.setId(deptId.toString());
            departmentDTO.setParentId("0");
            return null;
        }
        DepartmentDTO departmentDTO = createDepartmentRecursion(parentId);
        return departmentDTO;
    }
}
