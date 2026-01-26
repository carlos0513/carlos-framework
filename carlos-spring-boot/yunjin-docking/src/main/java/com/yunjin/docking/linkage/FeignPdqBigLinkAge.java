package com.yunjin.docking.linkage;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 郫都区大联动接口
 * </p>
 *
 * @author Carlos
 * @date 2023/5/6 11:26
 */
public interface FeignPdqBigLinkAge extends FeignBigLinkAge {


    /**
     * 获取用户详情信息
     *
     * @param appcode appcode
     * @param userid  用户id
     * @return com.yunjin.app.rzt.api.result.UserInfoResult
     * @author Carlos
     * @date 2023/4/7 16:44
     */
    @Override
    @PostMapping("/reportPassInterfaceController/queryUserInfo.do")
    String getUserInfo(@RequestParam("appcode") String appcode, @RequestParam("userid") String userid);


}
