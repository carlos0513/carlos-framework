package com.carlos.system.upload.controller;


import cn.hutool.core.collection.CollUtil;
import com.carlos.core.exception.RestException;
import com.carlos.core.response.Result;
import com.carlos.system.upload.convert.UploadFileConvert;
import com.carlos.system.upload.pojo.dto.UploadFileDTO;
import com.carlos.system.upload.pojo.dto.UploadResultDTO;
import com.carlos.system.upload.pojo.param.SingleUploadParam;
import com.carlos.system.upload.pojo.vo.FileInfoVO;
import com.carlos.system.upload.pojo.vo.UploadResultVO;
import com.carlos.system.upload.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
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
@RequestMapping("/sys/file")
@Api(tags = "文件服务")
public class FileController {

    private final FileService fileService;

    @PostMapping("upload")
    @ApiOperation(value = "上传文件")
    public Result<UploadResultVO> upload(@RequestPart @RequestParam("files") MultipartFile[] files, @RequestParam("namespace") String namespace) {
        UploadResultDTO dto = fileService.uploadMultipartFile(namespace, Arrays.asList(files));
        UploadResultVO vo = UploadFileConvert.INSTANCE.toResultVO(dto);
        return Result.ok(vo);
    }

    @PostMapping("singleUpload")
    @ApiOperation(value = "单文件上传")
    public Result<FileInfoVO> singleUpload(SingleUploadParam param) {
        UploadResultDTO result = fileService.uploadMultipartFile(param.getNamespace(), Collections.singletonList(param.getFile()));
        List<UploadFileDTO> files = result.getFiles();
        if (CollUtil.isEmpty(files)) {
            throw new RestException("文件上传失败！");
        }
        UploadFileDTO record = files.stream().findFirst().get();
        return Result.ok(UploadFileConvert.INSTANCE.toFileVO(record));
    }

    @PostMapping("singleUploadForAnonymous")
    @ApiOperation(value = "匿名单文件上传")
    public Result<FileInfoVO> singleUploadforAnonymous(SingleUploadParam param) {
        UploadResultDTO result = fileService.uploadMultipartFile(param.getNamespace(), Collections.singletonList(param.getFile()));
        List<UploadFileDTO> files = result.getFiles();
        if (CollUtil.isEmpty(files)) {
            throw new RestException("文件上传失败！");
        }
        UploadFileDTO record = files.stream().findFirst().get();
        return Result.ok(UploadFileConvert.INSTANCE.toFileVO(record));
    }

    @ApiOperation(value = "获取二进制文件流", notes = "下载文件或者播放文件")
    @GetMapping("load")
    public void download(String id, HttpServletResponse response) {
        if (id == null) {
            return;
        }
        fileService.downloadById(id, response);
    }

    @ApiOperation(value = "查询文件详情信息", notes = "查询文件信息获取地址")
    @GetMapping("{id}")
    public FileInfoVO getFile(@PathVariable("id") String id) {
        if (id == null) {
            return null;
        }
        UploadFileDTO dto = fileService.getFileInfo(id, false);
        return UploadFileConvert.INSTANCE.toFileVO(dto);
    }

}
