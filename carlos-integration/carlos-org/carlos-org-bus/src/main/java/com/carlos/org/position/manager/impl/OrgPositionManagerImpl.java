package com.carlos.org.position.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.position.convert.OrgPositionConvert;
import com.carlos.org.position.manager.OrgPositionManager;
import com.carlos.org.position.mapper.OrgPositionMapper;
import com.carlos.org.position.pojo.dto.OrgPositionDTO;
import com.carlos.org.position.pojo.entity.OrgPosition;
import com.carlos.org.position.pojo.param.OrgPositionPageParam;
import com.carlos.org.position.pojo.vo.OrgPositionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 岗位表 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrgPositionManagerImpl extends BaseServiceImpl<OrgPositionMapper, OrgPosition> implements OrgPositionManager {

    @Override
    public boolean add(OrgPositionDTO dto) {
        OrgPosition entity = OrgPositionConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'OrgPosition' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'OrgPosition' data: id:{}", entity.getId());
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
            log.warn("Remove 'OrgPosition' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'OrgPosition' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(OrgPositionDTO dto) {
        OrgPosition entity = OrgPositionConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'OrgPosition' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'OrgPosition' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public OrgPositionDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        OrgPosition entity = getBaseMapper().selectById(id);
        return OrgPositionConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<OrgPositionVO> getPage(OrgPositionPageParam param) {
        LambdaQueryWrapper<OrgPosition> wrapper = queryWrapper();
        wrapper.select(

                OrgPosition::getId,
                OrgPosition::getPositionCode,
                OrgPosition::getPositionName,
                OrgPosition::getPositionShort,
                OrgPosition::getCategoryId,
                OrgPosition::getDefaultLevelId,
                OrgPosition::getLevelRange,
                OrgPosition::getPositionType,
                OrgPosition::getPositionNature,
                OrgPosition::getDepartmentId,
                OrgPosition::getParentId,
                OrgPosition::getHeadcount,
                OrgPosition::getCurrentCount,
                OrgPosition::getVacancyCount,
                OrgPosition::getDuty,
                OrgPosition::getRequirement,
                OrgPosition::getQualification,
                OrgPosition::getKpiIndicator,
                OrgPosition::getCostCenter,
                OrgPosition::getLocation,
                OrgPosition::getSalaryGrade,
                OrgPosition::getState,
                OrgPosition::getTenantId,
                OrgPosition::getCreateBy,
                OrgPosition::getCreateTime,
                OrgPosition::getUpdateBy,
                OrgPosition::getUpdateTime
        );
        PageInfo<OrgPosition> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, OrgPositionConvert.INSTANCE::toVO);
    }

}
