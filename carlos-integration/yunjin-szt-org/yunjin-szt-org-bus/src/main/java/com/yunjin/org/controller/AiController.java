package com.yunjin.org.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.org.config.AuthorConstant;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * ai服务
 * </p>
 *
 * @author yunjin
 * @date 2024-3-23 12:31:52
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/ai")
@Tag(name = "ai服务")
public class AiController {

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("yj/text")
    @Operation(summary = "元景AI")
    public void yjai(String text) {
        // YjAIUtil.text(UserUtil.getId(), text);
    }

}
