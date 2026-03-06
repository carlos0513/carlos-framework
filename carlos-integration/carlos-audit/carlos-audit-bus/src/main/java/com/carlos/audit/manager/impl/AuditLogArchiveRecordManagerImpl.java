package com.carlos.audit.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.audit.convert.AuditLogArchiveRecordConvert;
import com.carlos.audit.manager.AuditLogArchiveRecordManager;
import com.carlos.audit.mapper.AuditLogArchiveRecordMapper;
import com.carlos.audit.pojo.dto.AuditLogArchiveRecordDTO;
import com.carlos.audit.pojo.entity.AuditLogArchiveRecord;
import com.carlos.audit.pojo.param.AuditLogArchiveRecordPageParam;
import com.carlos.audit.pojo.vo.AuditLogArchiveRecordVO;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
/**
 * <p>
 * 审计日志归档记录（管理冷数据归档） 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuditLogArchiveRecordManagerImpl extends BaseServiceImpl<AuditLogArchiveRecordMapper, AuditLogArchiveRecord> implements AuditLogArchiveRecordManager {

    @Override
    public boolean add(AuditLogArchiveRecordDTO dto) {
        AuditLogArchiveRecord entity = AuditLogArchiveRecordConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'AuditLogArchiveRecord' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'AuditLogArchiveRecord' data: id:{}", entity.getId());
        }
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
            log.warn("Remove 'AuditLogArchiveRecord' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'AuditLogArchiveRecord' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(AuditLogArchiveRecordDTO dto) {
        AuditLogArchiveRecord entity = AuditLogArchiveRecordConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'AuditLogArchiveRecord' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'AuditLogArchiveRecord' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public AuditLogArchiveRecordDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        AuditLogArchiveRecord entity = getBaseMapper().selectById(id);
        return AuditLogArchiveRecordConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<AuditLogArchiveRecordVO> getPage(AuditLogArchiveRecordPageParam param) {
        LambdaQueryWrapper<AuditLogArchiveRecord> wrapper = queryWrapper();
        wrapper.select(

            AuditLogArchiveRecord::getId,
            AuditLogArchiveRecord::getArchiveId,
            AuditLogArchiveRecord::getArchiveDate,
            AuditLogArchiveRecord::getStartTime,
            AuditLogArchiveRecord::getEndTime,
            AuditLogArchiveRecord::getRecordCount,
            AuditLogArchiveRecord::getFileSizeBytes,
            AuditLogArchiveRecord::getStoragePath,
            AuditLogArchiveRecord::getStorageType,
            AuditLogArchiveRecord::getVerifyChecksum,
            AuditLogArchiveRecord::getState,
            AuditLogArchiveRecord::getCreatedTime
        );
        PageInfo<AuditLogArchiveRecord> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, AuditLogArchiveRecordConvert.INSTANCE::toVO);
    }

}
