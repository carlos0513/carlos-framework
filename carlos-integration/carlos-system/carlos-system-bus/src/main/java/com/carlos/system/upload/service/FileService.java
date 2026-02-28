package com.carlos.system.upload.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.response.Result;
import com.carlos.core.util.ExecutorUtil;
import com.carlos.minio.utils.MinioUtil;
import com.carlos.minio.utils.ObjectOptUtil;
import com.carlos.system.upload.config.SystemFileProperties;
import com.carlos.system.upload.convert.UploadFileConvert;
import com.carlos.system.upload.pojo.dto.UploadFileDTO;
import com.carlos.system.upload.pojo.dto.UploadRecordDTO;
import com.carlos.system.upload.pojo.dto.UploadResultDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;


/**
 * <p>
 * 文件相关服务
 * </p>
 *
 * @author Carlos
 * @date 2021/12/16 11:38
 */
@Slf4j
@AllArgsConstructor
@Service
public class FileService {


    private final UploadRecordService uploadRecordService;

    private final SystemFileProperties systemFileProperties;

    private static final ThreadPoolExecutor UPLOAD_POOL = ExecutorUtil.get(2, 8, "upload", 100, null);

    /** 文件base64前缀 */
    private static final String BASE_PREFIX_FORMAT = "data:%s;base64,";


    /**
     * 上传文件
     *
     * @param namespace 文件保存位置
     * @param files     上传文件列表
     * @return com.carlos.system.upload.pojo.dto.UploadResultDTO
     * @author Carlos
     * @date 2024/1/5 16:59
     */
    public UploadResultDTO upload(String namespace, List<UploadFileDTO> files) {
        if (CharSequenceUtil.isBlank(namespace)) {
            namespace = MinioUtil.getDefaultBucket();
            log.warn("file namespace is null, use default bucket: {}", namespace);
        }
        // 检查文件类型
        checkExtension(files.stream().map(i -> FileUtil.extName(i.getName())).collect(Collectors.toSet()));

        final String groupId = RandomUtil.randomNumbers(10);
        final String prefix = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN) + StrUtil.SLASH + groupId;
        final List<Future<UploadFileDTO>> futures = new ArrayList<>(files.size());
        for (int i = 1; i <= files.size(); i++) {
            final UploadFileDTO file = files.get(i - 1);
            file.setGroupId(groupId);
            final int finalI = i;
            String finalNamespace = namespace;
            final Future<UploadFileDTO> res = UPLOAD_POOL.submit(() -> {
                final String originalFilename = file.getName();
                final String suffix = StrUtil.UNDERLINE + finalI + StrUtil.DOT + FileUtil.extName(originalFilename);
                final String obj = prefix + suffix;

                try {
                    ObjectOptUtil.putObject(finalNamespace, obj, IoUtil.toStream(file.getBytes()));
                } catch (Exception e) {
                    throw new ServiceException("文件上传失败！文件名称：" + originalFilename, e);
                }
                final UploadRecordDTO record = new UploadRecordDTO();
                record.setGroupId(groupId);
                record.setRepositoryName(finalNamespace);
                record.setRepositoryUrl(obj);
                record.setOriginalName(originalFilename);
                uploadRecordService.addUploadRecord(record);
                file.setId(record.getId());
                file.setUrl(getDownloadUrl(record.getId()));
                return file;
            });
            futures.add(res);
        }

        final List<UploadFileDTO> records = new ArrayList<>();
        for (final Future<UploadFileDTO> future : futures) {
            UploadFileDTO file;
            try {
                file = future.get();
            } catch (final InterruptedException | ExecutionException e) {
                log.error("File upload failed: message:{}", e.getMessage(), e);
                continue;
            }
            records.add(file);
            log.info("File save success, id={}", file.getId());
            // 文件上传完成进行后续处理
        }
        return new UploadResultDTO(groupId, records);
    }


    /**
     * 上传 @MultipartFile类型文件
     *
     * @param namespace 上传空间
     * @param files     文件列表
     * @return com.carlos.system.upload.pojo.dto.UploadResultDTO
     */
    public UploadResultDTO uploadMultipartFile(String namespace, List<MultipartFile> files) {
        List<UploadFileDTO> uploadFiles = files.stream().map(i -> {
            UploadFileDTO file = new UploadFileDTO();
            file.setName(i.getOriginalFilename());
            byte[] bytes;
            try {
                bytes = i.getBytes();
            } catch (IOException e) {
                log.error("get file error! Error message:{}", e.getMessage(), e);
                throw new ServiceException(e);
            }
            file.setBytes(bytes);
            // file.setBase64(codeBase64(bytes, FileTypeUtil.getTypeByPath(file.getName())));
            return file;
        }).collect(Collectors.toList());
        return this.upload(namespace, uploadFiles);
    }


    /**
     * 文件子节生成base64
     *
     * @param bytes    文件子节数组
     * @param mimeType 文件mimeType
     * @return java.lang.String
     */
    private String codeBase64(byte[] bytes, String mimeType) {
        if (CharSequenceUtil.isBlank(mimeType)) {
            return Base64.encode(bytes);
        }
        String format = StrUtil.format(BASE_PREFIX_FORMAT, mimeType);
        return format + Base64.encode(bytes);
    }


    public void downloadById(final String id, final HttpServletResponse response) {
        UploadFileDTO fileInfo = getFileInfo(id, true);
        byte[] bytes = fileInfo.getBytes();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        if (bytes == null) {
            JakartaServletUtil.write(response, JSONUtil.toJsonStr(Result.fail("文件读取失败！")), ContentType.JSON.getValue());
            return;
        }
        String fileName = fileInfo.getName();
        // 获取文件content-type
        String mimeType = FileUtil.getMimeType(fileName);
        try {
            JakartaServletUtil.write(response, IoUtil.toStream(bytes), mimeType, fileName);
        } catch (Exception e) {
            log.error("文件下载失败：{}", fileName, e);
        }
    }

    /**
     * 获取文件信息
     *
     * @param id 文件id
     * @return com.carlos.system.upload.pojo.dto.UploadFileDTO
     * @author Carlos
     * @date 2024/1/5 11:32
     */
    public UploadFileDTO getFileInfo(String id, boolean loadStream) {
        UploadRecordDTO dto = uploadRecordService.getInfoById(id);
        if (dto == null) {
            throw new ServiceException("文件不存在！ id=" + id);
        }
        UploadFileDTO fileinfo = new UploadFileDTO();
        fileinfo.setId(dto.getId());
        fileinfo.setGroupId(dto.getGroupId());
        fileinfo.setUrl(this.getDownloadUrl(id));
        fileinfo.setName(dto.getOriginalName());
        fileinfo.setDesc(dto.getOriginalName());
        if (loadStream) {
            InputStream stream = ObjectOptUtil.getObject(dto.getRepositoryName(), dto.getRepositoryUrl());
            if (stream != null) {
                fileinfo.setBytes(IoUtil.readBytes(stream));
            }
        }
        return fileinfo;
    }


    /**
     * 获取文件下载地址
     *
     * @param id 文件id
     * @return java.lang.String
     * @author Carlos
     * @date 2024/1/5 13:43
     */
    public String getDownloadUrl(Serializable id) {
        String baseUrl = systemFileProperties.getBaseUrl();
        String downloadPath = systemFileProperties.getDownloadPath();
        return baseUrl + downloadPath + id;
    }

    /**
     * 根据批次号获取一批次文件id
     *
     * @param groupId 批次id
     * @return java.util.List<com.carlos.system.upload.pojo.dto.UploadFileDTO>
     * @author Carlos
     * @date 2024/1/5 14:17
     */
    public List<UploadFileDTO> getFileGroup(String groupId) {
        List<UploadRecordDTO> files = uploadRecordService.getInfoByGroupId(groupId);
        return files.stream().map(i -> {
            UploadFileDTO file = UploadFileConvert.INSTANCE.toDTO(i);
            file.setUrl(getDownloadUrl(file.getId()));
            return file;
        }).collect(Collectors.toList());
    }

    /**
     * 批量获取文件信息
     *
     * @param ids 文件ids
     * @return java.util.List<com.carlos.system.upload.pojo.dto.UploadFileDTO>
     * @author Carlos
     * @date 2024/1/5 14:25
     */
    public List<UploadFileDTO> getFileInfo(Set<String> ids) {
        List<UploadRecordDTO> files = uploadRecordService.getInfoByIds(ids);
        return files.stream().map(i -> {
            UploadFileDTO file = UploadFileConvert.INSTANCE.toDTO(i);
            file.setUrl(getDownloadUrl(file.getId()));
            return file;
        }).collect(Collectors.toList());
    }

    /**
     * 检查上传文件类型
     *
     * @param file 上传文件
     * @return boolean
     * @throws
     * @author Carlos
     * @date 2024/1/5 16:58
     */
    private boolean isAllowedExtension(MultipartFile file) {
        String extension = FileUtil.extName(file.getOriginalFilename());
        Set<String> supportExt = systemFileProperties.getSupportExt();
        if (CollUtil.isEmpty(supportExt)) {
            return true;
        } else {
            return supportExt.contains(extension);
        }
    }

    /**
     * 检查上传文件类型
     *
     * @param fileName 文件名称
     * @return boolean
     * @throws
     * @author Carlos
     * @date 2024/1/5 16:58
     */
    private boolean isAllowedExtension(String fileName) {
        String extension = FileUtil.extName(fileName);
        Set<String> supportExt = systemFileProperties.getSupportExt();
        if (CollUtil.isEmpty(supportExt)) {
            return true;
        } else {
            return supportExt.contains(extension);
        }
    }

    private void checkExtension(Set<String> exts) {
        if (CollUtil.isEmpty(exts)) {
            return;
        }
        Set<String> supportExt = systemFileProperties.getSupportExt();
        if (CollUtil.isEmpty(supportExt)) {
            return;
        }
        for (String ext : exts) {
            if (!supportExt.contains(ext)) {
                throw new ServiceException("不支持" + ext + "类型文件上传！仅支持" + CollUtil.join(supportExt, StrUtil.SLASH) + "格式");
            }
        }
    }
}
