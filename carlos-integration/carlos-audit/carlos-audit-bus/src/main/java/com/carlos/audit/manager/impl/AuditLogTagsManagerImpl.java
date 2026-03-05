package com.carlos.audit.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.audit.convert.AuditLogTagsConvert;
import com.carlos.audit.manager.AuditLogTagsManager;
import com.carlos.audit.mapper.AuditLogTagsMapper;
import com.carlos.audit.pojo.dto.AuditLogTagsDTO;
import com.carlos.audit.pojo.entity.AuditLogTags;
import com.carlos.audit.pojo.param.AuditLogTagsPageParam;
import com.carlos.audit.pojo.vo.AuditLogTagsVO;
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
 * 审计日志-动态标签 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuditLogTagsManagerImpl extends BaseServiceImpl<AuditLogTagsMapper, AuditLogTags> implements AuditLogTagsManager {

    @Override
    public boolean add(AuditLogTagsDTO dto) {
        AuditLogTags entity = AuditLogTagsConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'AuditLogTags' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'AuditLogTags' data: id:{}", entity.getId());
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
            log.warn("Remove 'AuditLogTags' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'AuditLogTags' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(AuditLogTagsDTO dto) {
        AuditLogTags entity = AuditLogTagsConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'AuditLogTags' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'AuditLogTags' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public AuditLogTagsDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        AuditLogTags entity = getBaseMapper().selectById(id);
        return AuditLogTagsConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<AuditLogTagsVO> getPage(AuditLogTagsPageParam param) {
        LambdaQueryWrapper<AuditLogTags> wrapper = queryWrapper();
        wrapper.select(

            AuditLogTags::getId,
            AuditLogTags::getAuditLogId,
            AuditLogTags::getTagKey,
            AuditLogTags::getTagValue,
            AuditLogTags::getCreatedTime
        );
        PageInfo<AuditLogTags> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, AuditLogTagsConvert.INSTANCE::toVO);
    }

}
