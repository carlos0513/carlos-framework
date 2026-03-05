package com.carlos.audit.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.audit.convert.AuditLogAttachmentsConvert;
import com.carlos.audit.manager.AuditLogAttachmentsManager;
import com.carlos.audit.mapper.AuditLogAttachmentsMapper;
import com.carlos.audit.pojo.dto.AuditLogAttachmentsDTO;
import com.carlos.audit.pojo.entity.AuditLogAttachments;
import com.carlos.audit.pojo.param.AuditLogAttachmentsPageParam;
import com.carlos.audit.pojo.vo.AuditLogAttachmentsVO;
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
 * 审计日志-附件引用 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuditLogAttachmentsManagerImpl extends BaseServiceImpl<AuditLogAttachmentsMapper, AuditLogAttachments> implements AuditLogAttachmentsManager {

    @Override
    public boolean add(AuditLogAttachmentsDTO dto) {
        AuditLogAttachments entity = AuditLogAttachmentsConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'AuditLogAttachments' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'AuditLogAttachments' data: id:{}", entity.getId());
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
            log.warn("Remove 'AuditLogAttachments' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'AuditLogAttachments' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(AuditLogAttachmentsDTO dto) {
        AuditLogAttachments entity = AuditLogAttachmentsConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'AuditLogAttachments' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'AuditLogAttachments' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public AuditLogAttachmentsDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        AuditLogAttachments entity = getBaseMapper().selectById(id);
        return AuditLogAttachmentsConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<AuditLogAttachmentsVO> getPage(AuditLogAttachmentsPageParam param) {
        LambdaQueryWrapper<AuditLogAttachments> wrapper = queryWrapper();
        wrapper.select(

            AuditLogAttachments::getId,
            AuditLogAttachments::getAuditLogId,
            AuditLogAttachments::getAttachmentType,
            AuditLogAttachments::getStorageType,
            AuditLogAttachments::getBucketName,
            AuditLogAttachments::getObjectKey,
            AuditLogAttachments::getFileName,
            AuditLogAttachments::getFileSize,
            AuditLogAttachments::getContentType,
            AuditLogAttachments::getCreatedTime
        );
        PageInfo<AuditLogAttachments> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, AuditLogAttachmentsConvert.INSTANCE::toVO);
    }

}
