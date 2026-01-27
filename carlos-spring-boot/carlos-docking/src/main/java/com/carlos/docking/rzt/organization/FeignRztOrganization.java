package com.carlos.docking.rzt.organization;

import com.carlos.docking.rzt.organization.config.RztFeignOrganizationConfig;
import com.carlos.docking.rzt.organization.result.RztUserInfoResult;
import com.carlos.docking.rzt.organization.result.RztUserPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 数据接入记录 feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2022-1-17 13:46:52
 */
@FeignClient(value = "rzt-organization-bbt", path = "${yunjin.docking.rzt.organization.path}", url = "${yunjin.docking.rzt.organization.host}", contextId = "rzt-organization-bbt", configuration = RztFeignOrganizationConfig.class)
public interface FeignRztOrganization {

    /**
     * 撤回消息
     *
     * @param startIndex startIndex
     * @param count count
     * @param filter filter
     * @param searchType searchType
     * @return com.carlos.docking.rzt.result.RztResult
     * @author Carlos
     * @date 2024-11-18 22:27
     */
    @GetMapping("/Users")
    RztUserPageResult<RztUserInfoResult> getUsers(@RequestHeader MultiValueMap<String, String> headers, @RequestParam("startIndex") int startIndex, @RequestParam("count") int count, @RequestParam("filter") String filter, @RequestParam("searchType") String searchType);


}
