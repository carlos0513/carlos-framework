package com.carlos.org.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.convert.OrgDepartmentConvert;
import com.carlos.org.manager.OrgDepartmentManager;
import com.carlos.org.mapper.OrgDepartmentMapper;
import com.carlos.org.pojo.dto.OrgDepartmentDTO;
import com.carlos.org.pojo.entity.OrgDepartment;
import com.carlos.org.pojo.param.OrgDepartmentPageParam;
import com.carlos.org.pojo.vo.OrgDepartmentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 部门 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrgDepartmentManagerImpl extends BaseServiceImpl<OrgDepartmentMapper, OrgDepartment> implements OrgDepartmentManager {

    @Override
    public boolean add(OrgDepartmentDTO dto) {
        OrgDepartment entity = OrgDepartmentConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'OrgDepartment' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'OrgDepartment' data: id:{}", entity.getId());
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
            log.warn("Remove 'OrgDepartment' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'OrgDepartment' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(OrgDepartmentDTO dto) {
        OrgDepartment entity = OrgDepartmentConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'OrgDepartment' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'OrgDepartment' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public OrgDepartmentDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        OrgDepartment entity = getBaseMapper().selectById(id);
        return OrgDepartmentConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<OrgDepartmentVO> getPage(OrgDepartmentPageParam param) {
        LambdaQueryWrapper<OrgDepartment> wrapper = queryWrapper();
        wrapper.select(

                OrgDepartment::getId,
                OrgDepartment::getParentId,
                OrgDepartment::getDeptName,
                OrgDepartment::getDeptCode,
                OrgDepartment::getPath,
                OrgDepartment::getLeaderId,
                OrgDepartment::getState,
                OrgDepartment::getSort,
                OrgDepartment::getLevel,
                OrgDepartment::getDescription,
                OrgDepartment::getCreateBy,
                OrgDepartment::getCreateTime,
                OrgDepartment::getUpdateBy,
                OrgDepartment::getUpdateTime,
                OrgDepartment::getTenantId
        );
        PageInfo<OrgDepartment> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, OrgDepartmentConvert.INSTANCE::toVO);
    }

}
