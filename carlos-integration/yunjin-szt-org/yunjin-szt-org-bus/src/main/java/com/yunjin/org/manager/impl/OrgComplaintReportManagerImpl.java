package com.yunjin.org.manager.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.datasource.pagination.MybatisPage;
import com.yunjin.datasource.pagination.PageInfo;
import com.yunjin.org.convert.OrgComplaintReportConvert;
import com.yunjin.org.manager.OrgComplaintReportManager;
import com.yunjin.org.mapper.OrgComplaintReportMapper;
import com.yunjin.org.pojo.dto.OrgComplaintReportDTO;
import com.yunjin.org.pojo.entity.OrgComplaintReport;
import com.yunjin.org.pojo.param.OrgComplaintReportPageParam;
import com.yunjin.org.pojo.vo.OrgComplaintReportVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrgComplaintReportManagerImpl extends BaseServiceImpl<OrgComplaintReportMapper, OrgComplaintReport> implements OrgComplaintReportManager {

    @Override
    public boolean add(OrgComplaintReportDTO dto) {
        OrgComplaintReport entity = OrgComplaintReportConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'OrgComplaintReport' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'OrgComplaintReport' data: id:{}", entity.getId());
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
            log.warn("Remove 'OrgComplaintReport' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'OrgComplaintReport' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(OrgComplaintReportDTO dto) {
        OrgComplaintReport entity = OrgComplaintReportConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'OrgComplaintReport' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'OrgComplaintReport' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public OrgComplaintReportDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        OrgComplaintReport entity = getBaseMapper().selectById(id);
        return OrgComplaintReportConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<OrgComplaintReportVO> getPage(OrgComplaintReportPageParam param, Set<String> deptCodes) {
        LambdaQueryWrapper<OrgComplaintReport> wrapper = queryWrapper();
        wrapper.select(
                OrgComplaintReport::getId,
                OrgComplaintReport::getComplaintSource,
                OrgComplaintReport::getTaskSource,
                OrgComplaintReport::getTaskSys,
                OrgComplaintReport::getComplaintTask,
                OrgComplaintReport::getComplaintForm,
                OrgComplaintReport::getComplaintType,
                OrgComplaintReport::getReason,
                OrgComplaintReport::getFormDept,
                OrgComplaintReport::getStatus,
                OrgComplaintReport::getReply,
                OrgComplaintReport::getCreateBy,
                OrgComplaintReport::getCreateTime,
                OrgComplaintReport::getUpdateBy,
                OrgComplaintReport::getUpdateTime,
                OrgComplaintReport::getHandleLevel,
                OrgComplaintReport::getPictures
        );
        // 排序
        wrapper.orderByDesc(OrgComplaintReport::getCreateTime);
        // 查询条件
        wrapper.eq(param.getComplaintSource() != null, OrgComplaintReport::getComplaintSource, param.getComplaintSource())
                .eq(param.getStatus() != null, OrgComplaintReport::getStatus, param.getStatus())
                .eq(param.getUserId() != null, OrgComplaintReport::getCreateBy, param.getUserId())
                .eq(param.getTaskSource() != null, OrgComplaintReport::getTaskSource, param.getTaskSource())
                .eq(param.getTaskSys() != null, OrgComplaintReport::getTaskSys, param.getTaskSys())
                .eq(param.getHandleLevel() != null, OrgComplaintReport::getHandleLevel, param.getHandleLevel())
                .in(CollUtil.isNotEmpty(deptCodes), OrgComplaintReport::getFormDept, deptCodes)
                .eq(OrgComplaintReport::getDeleted, 0);
        PageInfo<OrgComplaintReport> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, OrgComplaintReportConvert.INSTANCE::toVO);
    }

}
