package com.carlos.minio.web;

import com.carlos.core.response.Result;
import com.carlos.minio.bean.ClientInfo;
import com.carlos.minio.config.MinioProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 项目信息
 * </p>
 *
 * @author yunjin
 * @date 2020/4/11 1:46
 */
@Slf4j
@RestController
@RequestMapping("/minio")
@Tag(name = "Minio相关接口")
@RequiredArgsConstructor
public class MinioController {

    private final MinioProperties properties;

    @GetMapping("/init")
    @Operation(summary = "minio初始化信息")
    public Result<ClientInfo> init() {
        return Result.ok(ClientInfo.builder().endpoint(properties.getEndpoint())
                .accessKey(properties.getAccessKey())
                .secretKey(properties.getSecretKey())
                .defaultBucket(properties.getBucket()).build());
    }
}
