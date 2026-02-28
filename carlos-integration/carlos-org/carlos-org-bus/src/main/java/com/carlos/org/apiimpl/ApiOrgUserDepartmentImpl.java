package com.carlos.org.apiimpl;


import com.carlos.org.api.ApiOrgUserDepartment;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户部门 api接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/user/department")
@Tag(name = "用户部门Feign接口")
public class ApiOrgUserDepartmentImpl implements ApiOrgUserDepartment {


}
