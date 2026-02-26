package com.carlos.auth.web;

import com.carlos.auth.pojo.PasswordMatchDTO;
import com.carlos.core.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 密码相关接口
 * </p>
 *
 * @author carlos
 * @date 2021/12/27 16:05
 */
@RestController
@Tag(name = "PasswordAPI")
@RequestMapping("pwd")
@AllArgsConstructor
@ConditionalOnBean(PasswordEncoder.class)
public class PasswordApi {

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "密码加密")
    @GetMapping(value = "encode")
    public Result<String> encode(@RequestParam("password") String password) {
        if (StringUtils.isBlank(password)) {
            return null;
        }
        String encode = passwordEncoder.encode(password);
        return Result.ok(encode);
    }

    @Operation(summary = "密码匹配校验")
    @PostMapping(value = "match")
    public Result<Boolean> match(@RequestBody PasswordMatchDTO param) {
        boolean match = passwordEncoder.matches(param.getPassword(), param.getEncodePassword());
        return Result.ok(match);
    }
}
