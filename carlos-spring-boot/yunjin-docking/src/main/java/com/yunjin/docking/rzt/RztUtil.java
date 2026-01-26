package com.yunjin.docking.rzt;

import com.yunjin.docking.rzt.param.RztRevokeMessageParam;
import com.yunjin.docking.rzt.result.MessageSendResult;
import com.yunjin.docking.rzt.result.UserIdResult;
import com.yunjin.docking.rzt.result.UserInfoResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * <p>
 * 蓉政通工具类
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 9:39
 */
@Slf4j
public class RztUtil {

    private static RztService rztService;


    public RztUtil(RztService rztService) {
        RztUtil.rztService = rztService;
    }

    /**
     * 获取用户id
     *
     * @param code 通过成员授权获取到的code，每次成员授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期
     * @return java.lang.String
     * @author Carlos
     * @date 2023/4/7 16:03
     */
    public static String getUserId(String code) {
        UserIdResult result = rztService.getUserId(code);
        return result.getUserId();
    }

    /**
     * 获取用户id
     *
     * @param userId
     * @param phone
     * @return java.lang.String
     * @author Carlos
     * @date 2023/4/7 16:03
     */
    public static String getUsersId(String userId, String phone) {
        return rztService.getRztUsers(userId, phone);
    }

    /**
     * 缓存蓉政通用户
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2023/4/7 16:03
     */
    public static void cacheUser() {
        rztService.cacheUsers();
    }

    /**
     * 更新蓉政通用户缓存
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2023/4/7 16:03
     */
    public static void updateRztUsers(String filter, String searchType) {
        rztService.updateRztUsers(filter, searchType);
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return com.yunjin.app.rzt.api.result.UserInfoResult
     * @author Carlos
     * @date 2023/4/7 14:20
     */
    public static UserInfoResult getUserInfo(String userId) {
        UserInfoResult result = rztService.getUserInfo(userId);
        return result;
    }

    /**
     * 发送文本消息
     *
     * @param users   用户信息
     * @param party   部门信息
     * @param tags    标签信息
     * @param content 消息文本
     * @return com.yunjin.docking.rzt.result.MessageSendResult
     * @author Carlos
     * @date 2024-10-28 16:48
     */
    public static MessageSendResult sendTextMessage(Set<String> users, Set<String> party, Set<String> tags, String content) {
        return rztService.sendTextMessage(users, party, tags, content);
    }

    /**
     * 撤回消息
     *
     * @param param 参数
     * @author Carlos
     * @date 2024-11-18 22:37
     */
    public static void revokeMessage(RztRevokeMessageParam param) {
        rztService.revokeMessage(param);
    }


}
