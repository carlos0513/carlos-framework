package com.carlos.org.apiimpl;

import com.carlos.org.api.ApiUserRole;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/user/role")
@Tag(name = "用户角色Feign接口")
@Hidden
@Slf4j
public class ApiUserRoleImpl implements ApiUserRole {


}
