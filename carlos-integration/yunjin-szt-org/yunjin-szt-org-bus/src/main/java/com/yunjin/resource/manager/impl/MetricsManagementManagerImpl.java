package com.yunjin.resource.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.datasource.pagination.MybatisPage;
import com.yunjin.datasource.pagination.PageInfo;
import com.yunjin.resource.convert.MetricsManagementConvert;
import com.yunjin.resource.manager.MetricsManagementManager;
import com.yunjin.resource.mapper.MetricsManagementMapper;
import com.yunjin.resource.pojo.dto.MetricsManagementDTO;
import com.yunjin.resource.pojo.entity.MetricsManagement;
import com.yunjin.resource.pojo.param.MetricsManagementPageParam;
import com.yunjin.resource.pojo.vo.MetricsManagementVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 系统指标管理 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MetricsManagementManagerImpl extends BaseServiceImpl<MetricsManagementMapper, MetricsManagement> implements MetricsManagementManager {

    @Override
    public boolean add(MetricsManagementDTO dto) {
        MetricsManagement entity = MetricsManagementConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'MetricsManagement' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'MetricsManagement' data: id:{}", entity.getId());
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
            log.warn("Remove 'MetricsManagement' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'MetricsManagement' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(MetricsManagementDTO dto) {
        MetricsManagement entity = MetricsManagementConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'MetricsManagement' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'MetricsManagement' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public MetricsManagementDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        MetricsManagement entity = getBaseMapper().selectById(id);
        return MetricsManagementConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<MetricsManagementVO> getPage(MetricsManagementPageParam param) {
        LambdaQueryWrapper<MetricsManagement> wrapper = queryWrapper();
        wrapper.select(
                MetricsManagement::getId,
                MetricsManagement::getMetricsCode,
                MetricsManagement::getMetricsName,
                MetricsManagement::getMetricsType,
                MetricsManagement::getState,
                MetricsManagement::getDescription,
                MetricsManagement::getCreateBy,
                MetricsManagement::getCreateTime,
                MetricsManagement::getUpdateBy,
                MetricsManagement::getUpdateTime
        );
        PageInfo<MetricsManagement> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, MetricsManagementConvert.INSTANCE::toVO);
    }

}
