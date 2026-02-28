package com.carlos.org.apiimpl;


import com.carlos.org.api.ApiOrgUserRole;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户角色 api接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/user/role")
@Tag(name = "用户角色Feign接口")
public class ApiOrgUserRoleImpl implements ApiOrgUserRole {


}
