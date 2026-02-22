package com.carlos.org.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * ai服务
 * </p>
 *
 * @author carlos
 * @date 2024-3-23 12:31:52
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/ai")
@Tag(name = "ai服务")
public class AiController {


    @PostMapping("yj/text")
    @Operation(summary = "元景AI")
    public void yjai(String text) {
        // YjAIUtil.text(UserUtil.getId(), text);
    }

}
