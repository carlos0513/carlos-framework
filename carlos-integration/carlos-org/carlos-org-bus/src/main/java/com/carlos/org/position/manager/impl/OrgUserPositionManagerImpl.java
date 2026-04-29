package com.carlos.org.position.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.position.convert.OrgUserPositionConvert;
import com.carlos.org.position.manager.OrgUserPositionManager;
import com.carlos.org.position.mapper.OrgUserPositionMapper;
import com.carlos.org.position.pojo.dto.OrgUserPositionDTO;
import com.carlos.org.position.pojo.entity.OrgUserPosition;
import com.carlos.org.position.pojo.param.OrgUserPositionPageParam;
import com.carlos.org.position.pojo.vo.OrgUserPositionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 用户岗位职级关联表（核心任职信息） 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrgUserPositionManagerImpl extends BaseServiceImpl<OrgUserPositionMapper, OrgUserPosition> implements OrgUserPositionManager {

    @Override
    public boolean add(OrgUserPositionDTO dto) {
        OrgUserPosition entity = OrgUserPositionConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'OrgUserPosition' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        log.debug("Insert 'OrgUserPosition' data: id:{}", entity.getId());
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
            log.warn("Remove 'OrgUserPosition' data fail, id:{}", id);
            return false;
        }
        log.debug("Remove 'OrgUserPosition' data by id:{}", id);
        return true;
    }

    @Override
    public boolean modify(OrgUserPositionDTO dto) {
        OrgUserPosition entity = OrgUserPositionConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'OrgUserPosition' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        log.debug("Update 'OrgUserPosition' data by id:{}", dto.getId());
        return true;
    }

    @Override
    public OrgUserPositionDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        OrgUserPosition entity = getBaseMapper().selectById(id);
        return OrgUserPositionConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<OrgUserPositionVO> getPage(OrgUserPositionPageParam param) {
        LambdaQueryWrapper<OrgUserPosition> wrapper = queryWrapper();
        wrapper.select(

            OrgUserPosition::getId,
            OrgUserPosition::getUserId,
            OrgUserPosition::getPositionId,
            OrgUserPosition::getLevelId,
            OrgUserPosition::getDepartmentId,
            OrgUserPosition::getMain,
            OrgUserPosition::getPositionStatus,
            OrgUserPosition::getAppointType,
            OrgUserPosition::getAppointDate,
            OrgUserPosition::getProbationEnd,
            OrgUserPosition::getDimissionDate,
            OrgUserPosition::getCostRatio,
            OrgUserPosition::getReportTo,
            OrgUserPosition::getDottedReport,
            OrgUserPosition::getWorkLocation,
            OrgUserPosition::getContractType,
            OrgUserPosition::getPerformanceRating,
            OrgUserPosition::getSalaryLevel,
            OrgUserPosition::getStockGrant,
            OrgUserPosition::getTenantId,
            OrgUserPosition::getCreateBy,
            OrgUserPosition::getCreateTime,
            OrgUserPosition::getUpdateBy,
            OrgUserPosition::getUpdateTime
        );
        PageInfo<OrgUserPosition> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, OrgUserPositionConvert.INSTANCE::toVO);
    }

}
