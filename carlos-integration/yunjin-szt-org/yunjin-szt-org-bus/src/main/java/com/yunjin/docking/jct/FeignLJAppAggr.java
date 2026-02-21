package com.yunjin.docking.jct;

import com.yunjin.docking.jct.param.LJAppAggrParseTokenParam;
import com.yunjin.docking.jct.result.LJAppAggrResult;
import com.yunjin.docking.jct.result.LJAppAggrUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * <p>
 *   黑龙江一体化平台接口
 * </p>
 *
 * @author Carlos
 * @date 2025-02-24 14:52
 */
public interface FeignLJAppAggr {


    /**
     * 获取全量用户信息
     *
     * @param appSign app签名信息
     * @return com.yunjin.docking.jct.result.LJAppAggrResult<java.util.List < com.yunjin.docking.jct.result.LJAppAggrUser>>
     * @author Carlos
     * @date 2025-02-25 09:27
     */
    @GetMapping("/grass/sync/user/supply/all/{appSign}")
    LJAppAggrResult<List<LJAppAggrUser>> allUser(@PathVariable("appSign") String appSign);

    /**
     * 解析token获取用户信息
     *
     * @param param   解析token参数
     * @return com.yunjin.docking.jct.result.LJAppAggrResult<com.yunjin.docking.jct.result.LJAppAggrUser>
     * @author Carlos
     * @date 2025-02-25 09:27
     */
    @PostMapping("/grass/auth/token/parse")
    LJAppAggrResult<LJAppAggrUser> getUserInfo(LJAppAggrParseTokenParam param);
}
