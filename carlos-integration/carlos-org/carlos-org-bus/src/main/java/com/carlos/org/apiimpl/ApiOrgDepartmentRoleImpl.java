package com.carlos.org.apiimpl;


import com.carlos.org.api.ApiOrgDepartmentRole;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 部门角色 api接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/department/role")
@Tag(name = "部门角色Feign接口")
public class ApiOrgDepartmentRoleImpl implements ApiOrgDepartmentRole {


}
