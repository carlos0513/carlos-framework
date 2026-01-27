package com.carlos.docking.linkage;

/**
 * <p>
 * 郫都区大联动接口
 * </p>
 *
 * @author Carlos
 * @date 2023/5/6 11:26
 */
public interface FeignBigLinkAge {


    /**
     * 获取用户详情信息
     *
     * @param appcode appcode
     * @param userid  用户id
     * @return com.carlos.app.rzt.api.result.UserInfoResult
     * @author Carlos
     * @date 2023/4/7 16:44
     */
    String getUserInfo(String appcode, String userid);


}
