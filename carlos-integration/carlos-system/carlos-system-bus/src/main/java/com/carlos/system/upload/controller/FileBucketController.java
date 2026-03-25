package com.carlos.system.upload.controller;


import com.carlos.oss.utils.OssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 文件上传相关接口
 * </p>
 *
 * @author Carlos
 * @date 2021-12-8 16:13:43
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/file/bucket")
@Tag(name = "文件bucket操作")
public class FileBucketController {


    @PostMapping("create")
    @Operation(summary = "创建bucket")
    public void create(@RequestBody BucketArray param) {
        List<Bucket> buckets = param.getBuckets();
        for (Bucket bucket : buckets) {
            OssUtil.createBucket(bucket.getName());
            switch (bucket.getPolicy()) {
                case PRIVATE:
                    OssUtil.setBucketPrivate(bucket.getName());
                    break;
                case PUBLIC:
                    OssUtil.setBucketPublic(bucket.getName());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + bucket.getPolicy());
            }
        }
    }


    @PostMapping("policy")
    @Operation(summary = "设置Policy")
    public void setPrivate(@RequestBody BucketArray param) {
        List<Bucket> buckets = param.getBuckets();
        for (Bucket bucket : buckets) {
            switch (bucket.getPolicy()) {
                case PRIVATE:
                    OssUtil.setBucketPrivate(bucket.getName());
                    break;
                case PUBLIC:
                    OssUtil.setBucketPublic(bucket.getName());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + bucket.getPolicy());
            }
        }
    }

    @Data
    public static class BucketArray {

        private List<Bucket> buckets;
    }


    @Data
    public static class Bucket {

        private String name;
        private BucketPolicy policy;
    }


    public static enum BucketPolicy {
        PRIVATE,
        PUBLIC,
    }
}
