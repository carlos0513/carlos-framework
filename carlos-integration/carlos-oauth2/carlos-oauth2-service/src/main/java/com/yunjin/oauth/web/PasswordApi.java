package com.carlos.oauth.web;

import com.carlos.auth.pojo.PasswordMatchDTO;
import com.carlos.core.response.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 * 密码相关接口
 * </p>
 *
 * @author yunjin
 * @date 2021/12/27 16:05
 */
@RestController
@Api(tags = "PasswordAPI")
@RequestMapping("pwd")
@AllArgsConstructor
@ConditionalOnBean(PasswordEncoder.class)
public class PasswordApi {


    private final PasswordEncoder passwordEncoder;


    @ApiOperation("密码加密")
    @GetMapping(value = "encode")
    public Result<String> encode(@RequestParam("password") String password) {
        if (StringUtils.isBlank(password)) {
            return null;
        }
        String encode = passwordEncoder.encode(password);
        return Result.ok(encode);
    }


    @ApiOperation("密码匹配校验")
    @PostMapping(value = "match")
    public Result<Boolean> match(@RequestBody PasswordMatchDTO param) {
        boolean match = passwordEncoder.matches(param.getPassword(), param.getEncodePassword());
        return Result.ok(match);
    }

}
