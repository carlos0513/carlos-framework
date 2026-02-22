package com.carlos.system.upload.apiimpl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.carlos.core.response.Result;
import com.carlos.system.api.ApiFile;
import com.carlos.system.pojo.ao.FileInfoAO;
import com.carlos.system.pojo.ao.UploadResultAO;
import com.carlos.system.pojo.param.ApiFileUploadParam;
import com.carlos.system.pojo.param.ApiFileUploadParam.UploadFile;
import com.carlos.system.upload.convert.UploadFileConvert;
import com.carlos.system.upload.convert.UploadRecordConvert;
import com.carlos.system.upload.pojo.dto.UploadFileDTO;
import com.carlos.system.upload.pojo.dto.UploadResultDTO;
import com.carlos.system.upload.service.FileService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 动态信息 api接口
 * </p>
 *
 * @author Carlos
 * @date 2021-12-15 17:40:44
 */
@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("api/sys/file")
@Tag(name = "文件接口")
public class ApiFileImpl implements ApiFile {


    private final FileService fileService;


    @Override
    @Operation(summary = "获取文件信息")
    @GetMapping("info/{id}")
    public Result<FileInfoAO> getFile(@PathVariable("id") String id) {
        if (id == null) {
            return null;
        }
        UploadFileDTO dto = null;
        try {
            dto = fileService.getFileInfo(id, false);
        } catch (Exception e) {
            Result.fail(e.getMessage());
        }
        return Result.ok(UploadFileConvert.INSTANCE.toAO(dto));
    }

    @Override
    @GetMapping("stream/info/{id}")
    @Operation(summary = "获取文件流")
    public Result<FileInfoAO> getFileStreamInfo(@PathVariable("id") String id) {
        if (StrUtil.isBlank(id)) {
            return Result.fail("文件id不能为空");
        }
        UploadFileDTO fileInfo = fileService.getFileInfo(id, true);
        return Result.ok(UploadFileConvert.INSTANCE.toAO(fileInfo));
    }


    @Override
    @Operation(summary = "获取一组文件信息")
    @GetMapping("group/{groupId}")
    public Result<List<FileInfoAO>> getGroupFile(@PathVariable("groupId") String groupId) {
        if (groupId == null) {
            return null;
        }
        List<UploadFileDTO> files = fileService.getFileGroup(groupId);
        return Result.ok(UploadRecordConvert.INSTANCE.uploadFile2AO(files));
    }

    @Override
    @Operation(summary = "批量获取文件信息")
    @GetMapping("info/ids")
    public Result<List<FileInfoAO>> getFiles(@RequestParam("ids") Set<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Result.ok(Collections.emptyList());
        }
        List<UploadFileDTO> files = fileService.getFileInfo(ids);
        return Result.ok(UploadRecordConvert.INSTANCE.uploadFile2AO(files));
    }

    @Override
    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public Result<UploadResultAO> upload(@RequestBody @Validated ApiFileUploadParam params) {
        List<UploadFile> files = params.getFiles();
        List<UploadFileDTO> dtos = files.stream().map(f -> {
            UploadFileDTO uploadFileDTO = new UploadFileDTO();
            uploadFileDTO.setName(f.getName());
            uploadFileDTO.setBytes(f.getBytes());
            return uploadFileDTO;
        }).collect(Collectors.toList());
        UploadResultDTO result = fileService.upload(params.getNamespace(), dtos);
        return Result.ok(UploadFileConvert.INSTANCE.toAO(result));
    }
}
