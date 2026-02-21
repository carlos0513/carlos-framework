package com.yunjin.org.controller;


import com.yunjin.org.convert.UserConvert;
import com.yunjin.org.login.pojo.AppUserSessionVO;
import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.service.UserService;
import com.yunjin.system.enums.MenuType;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 移动端登录
 * </p>
 *
 * @author Carlos
 * @date 2023/4/12 20:13
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "移动端登录接口")
@RequestMapping("/org/app")
@Slf4j
public class AppLoginController {

    private final UserService userService;


    @GetMapping("user")
    @Operation(summary = "获取当前登录用户信息", notes = "用户信息包括用户菜单, 基本信息")
    public AppUserSessionVO loginInfo() {
        UserDTO dto = this.userService.getCurrentUser(MenuType.MOBILE);
        return UserConvert.INSTANCE.toAppLoginVO(dto);

    }

}
