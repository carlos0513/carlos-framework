package com.yunjin.docking.dingtalk;

import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse.UserGetByCodeResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 钉钉工具类
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 9:39
 */
@Slf4j
public class DingtalkUtil {

    private static DingtalkService service;


    public DingtalkUtil(DingtalkService service) {
        DingtalkUtil.service = service;
    }

    /**
     * getUserId
     *
     * @param code 参数0
     * @return java.lang.String
     * @author Carlos
     * @date 2023/6/30 15:27
     */
    public static UserGetByCodeResponse getUserId(String code) {
        return service.getUserId(code);
    }

    /**
     * 获取用户信息
     *
     * @param userId 参数0
     * @return com.dingtalk.api.response.OapiUserGetResponse
     * @author Carlos
     * @date 2023/6/30 15:27
     */
    public static OapiV2UserGetResponse getUserInfo(String userId) {
        return service.getUserInfo(userId);
    }


    /**
     * 获取部门详情
     *
     * @param deptId 部门id
     * @return com.dingtalk.api.response.OapiV2DepartmentGetResponse.DeptGetResponse
     * @author Carlos
     * @date 2024/7/1 17:09
     */
    public static OapiV2DepartmentGetResponse.DeptGetResponse getDepartmentInfo(Long deptId) {
        return service.getDeptInfo(deptId);
    }


    /**
     * 根据手机号获取用户
     *
     * @param mobile 手机号
     * @return com.dingtalk.api.response.OapiV2UserGetbymobileResponse.UserGetByMobileResponse
     * @throws
     * @author Carlos
     * @date 2025-03-25 15:53
     */
    public OapiV2UserGetbymobileResponse getByMobile(String mobile) {
        return service.getByMobile(mobile);
    }

    /**
     * 获取子级部门
     *
     * @param deptId 部门id
     * @return List<OapiV2DepartmentListsubResponse.DeptBaseResponse>
     * @author Carlos
     * @date 2025-02-13 16:27
     */
    public static List<OapiV2DepartmentListsubResponse.DeptBaseResponse> listDeptSub(Long deptId) {
        return service.listDeptSub(deptId);
    }

    /**
     * 获取部门用户
     *
     * @param deptId 部门id
     * @return com.dingtalk.api.response.OapiV2UserListResponse.PageResult
     * @author Carlos
     * @date 2025-02-13 16:27
     */
    public static OapiV2UserListResponse.PageResult listDeptUser(Long deptId, Long cursor, Long size) {
        return service.listUser(deptId, cursor, size);
    }

    /**
     * 创建用户
     *
     * @param request 参数0
     * @return com.dingtalk.api.response.OapiV2UserCreateResponse.UserCreateResponse
     * @author Carlos
     * @date 2025-03-01 19:50
     */
    public OapiV2UserCreateResponse createUser(OapiV2UserCreateRequest request) {
        return service.createUser(request);
    }

    /**
     * 修改用户
     *
     * @param request 参数0
     * @return com.dingtalk.api.response.OapiV2UserUpdateResponse
     * @throws
     * @author Carlos
     * @date 2025-03-01 19:52
     */
    public OapiV2UserUpdateResponse updateUser(OapiV2UserUpdateRequest request) {
        return service.updateUser(request);
    }

    /**
     * 删除用户
     *
     * @param request 删除用户参数
     * @return com.dingtalk.api.response.OapiV2UserDeleteResponse
     * @author Carlos
     * @date 2025-03-01 19:53
     */
    public OapiV2UserDeleteResponse deleteUser(OapiV2UserDeleteRequest request) {
        return service.deleteUser(request);
    }


    /**
     * 创建部门
     *
     * @param request 参数0
     * @return com.dingtalk.api.response.OapiV2DepartmentCreateResponse.DeptCreateResponse
     * @throws
     * @author Carlos
     * @date 2025-03-01 19:54
     */
    public OapiV2DepartmentCreateResponse createDepartment(OapiV2DepartmentCreateRequest request) {
        return service.createDepartment(request);
    }

    /**
     * 修改部门
     *
     * @param request 参数0
     * @return com.dingtalk.api.response.OapiV2DepartmentUpdateResponse
     * @throws
     * @author Carlos
     * @date 2025-03-01 19:55
     */
    public OapiV2DepartmentUpdateResponse updateDepartment(OapiV2DepartmentUpdateRequest request) {
        return service.updateDepartment(request);
    }

    /**
     * 删除部门
     *
     * @param request 删除部门参数
     * @return com.dingtalk.api.response.OapiV2DepartmentDeleteResponse
     * @author Carlos
     * @date 2025-03-01 19:56
     */
    public OapiV2DepartmentDeleteResponse deleteDepartment(OapiV2DepartmentDeleteRequest request) {
        return service.deleteDepartment(request);
    }

    /**
     * <p>
     * 获取钉钉accessToken
     * </p>
     *
     * @author Carlos
     * @date 2024/7/19 9:46
     */
    public static String getToken() {
        return service.getToken();
    }

    /**
     * 消息发送
     *
     * @param sendMessageRequest 请求参数
     * @return com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response
     * @author Carlos
     * @date 2025-04-14 23:29
     */
    public static OapiMessageCorpconversationAsyncsendV2Response send(SendMessageRequest sendMessageRequest) {
        return service.send(sendMessageRequest);
    }

}
