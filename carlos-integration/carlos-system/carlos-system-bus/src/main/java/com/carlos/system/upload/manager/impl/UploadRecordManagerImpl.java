package com.carlos.system.upload.manager.impl;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.system.upload.convert.UploadRecordConvert;
import com.carlos.system.upload.manager.UploadRecordManager;
import com.carlos.system.upload.mapper.UploadRecordMapper;
import com.carlos.system.upload.pojo.dto.UploadRecordDTO;
import com.carlos.system.upload.pojo.entity.UploadRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 文件上传记录 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2022-2-7 15:22:31
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class UploadRecordManagerImpl extends BaseServiceImpl<UploadRecordMapper, UploadRecord> implements UploadRecordManager {

    @Override
    public boolean add(UploadRecordDTO dto) {
        UploadRecord entity = UploadRecordConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'UploadRecord' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        log.debug("Insert 'UploadRecord' data: id:{}", entity.getId());
        return true;
    }

    @Override
    public boolean delete(Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        boolean success = removeById(id);
        if (!success) {
            log.warn("Remove 'UploadRecord' data fail, id:{}", id);
            return false;
        }
        log.debug("Remove 'UploadRecord' data by id:{}", id);
        return true;
    }

    @Override
    public boolean modify(UploadRecordDTO dto) {
        UploadRecord entity = UploadRecordConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'UploadRecord' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        log.debug("Update 'UploadRecord' data by id:{}", dto.getId());
        return true;
    }

    @Override
    public UploadRecordDTO getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            log.warn("id is null");
            return null;
        }
        UploadRecord entity = getBaseMapper().selectById(id);
        return UploadRecordConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public List<UploadRecordDTO> getDtoByGroupId(String groupId) {
        if (StringUtils.isBlank(groupId)) {
            log.warn("groupId is null");
            return null;
        }
        List<UploadRecord> uploadRecords = lambdaQuery().eq(UploadRecord::getGroupId, groupId).list();
        return UploadRecordConvert.INSTANCE.toDTO(uploadRecords);
    }

    @Override
    public List<UploadRecordDTO> getDtoByIds(Set<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.warn("ids is null");
            return null;
        }
        List<UploadRecord> uploadRecords = lambdaQuery().in(UploadRecord::getId, ids).list();
        return UploadRecordConvert.INSTANCE.toDTO(uploadRecords);
    }
}
