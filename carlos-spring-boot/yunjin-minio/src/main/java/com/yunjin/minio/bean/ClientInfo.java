package com.yunjin.minio.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * minio客户端信息
 * </p>
 *
 * @author yunjin
 * @date 2021/11/10 15:12
 */
@Data
@Builder
@Schema(description = "Minio客户端信息")
public class ClientInfo {

    @Schema(description = "连接地址")
    private String endpoint;

    @Schema(description = "账号")
    private String accessKey;

    @Schema(description = "密钥")
    private String secretKey;

    @Schema(description = "默认桶名")
    private String defaultBucket;


}
