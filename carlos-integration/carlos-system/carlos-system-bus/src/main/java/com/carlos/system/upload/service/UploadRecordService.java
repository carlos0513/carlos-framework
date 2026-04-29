package com.carlos.system.upload.service;


import com.carlos.system.enums.SystemErrorCode;
import com.carlos.system.upload.manager.UploadRecordManager;
import com.carlos.system.upload.pojo.dto.UploadRecordDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 文件上传记录 业务接口实现类
 * </p>
 *
 * @author Carlos
 * @date 2022-2-7 15:22:31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UploadRecordService {

    private final UploadRecordManager recordManager;


    public void addUploadRecord(final UploadRecordDTO dto) {
        final boolean success = recordManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        final Serializable id = dto.getId();
        // 保存完成的后续业务
    }


    public void deleteUploadRecord(final Set<Serializable> ids) {
        for (final Serializable id : ids) {
            final boolean success = recordManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }


    public void updateUploadRecord(final UploadRecordDTO dto) {
        final boolean success = recordManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }


    public UploadRecordDTO getInfoById(final String id) {
        final UploadRecordDTO dto = recordManager.getDtoById(id);
        if (dto == null) {
            throw SystemErrorCode.SYS_FILE_NOT_FOUND.exception("文件不存在！id=" + id);
        }
        return dto;
    }


    public List<UploadRecordDTO> getInfoByGroupId(final String groupId) {

        return recordManager.getDtoByGroupId(groupId);
    }


    public List<UploadRecordDTO> getInfoByIds(final Set<String> ids) {
        return recordManager.getDtoByIds(ids);
    }
}
