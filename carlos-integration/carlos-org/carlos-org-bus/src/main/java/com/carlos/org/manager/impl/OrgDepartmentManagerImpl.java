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
import com.carlos.org.pojo.dto.OrgDepartmentUserDTO;
import com.carlos.org.pojo.dto.OrgUserDTO;
import com.carlos.org.pojo.entity.OrgDepartment;
import com.carlos.org.pojo.param.OrgDepartmentPageParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public Paging<OrgDepartmentDTO> getPage(OrgDepartmentPageParam param) {
        LambdaQueryWrapper<OrgDepartment> wrapper = new LambdaQueryWrapper<>();
        if (param.getParentId() != null) {
            wrapper.eq(OrgDepartment::getParentId, param.getParentId());
        }
        wrapper.orderByAsc(OrgDepartment::getSort);
        PageInfo<OrgDepartment> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, entities -> entities.stream()
                .map(OrgDepartmentConvert.INSTANCE::toDTO)
                .collect(java.util.stream.Collectors.toList()));
    }

    @Override
    public List<OrgDepartmentDTO> listAll() {
        LambdaQueryWrapper<OrgDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(OrgDepartment::getSort);
        List<OrgDepartment> list = list(wrapper);
        return list.stream()
                .map(entity -> OrgDepartmentConvert.INSTANCE.toDTO(entity))
                .collect(Collectors.toList());
    }

    @Override
    public OrgDepartmentDTO getByDeptCode(String deptCode) {
        LambdaQueryWrapper<OrgDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgDepartment::getDeptCode, deptCode);
        wrapper.last("LIMIT 1");
        OrgDepartment entity = getBaseMapper().selectOne(wrapper);
        return OrgDepartmentConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public List<OrgDepartmentDTO> getChildrenByParentId(Serializable parentId) {
        LambdaQueryWrapper<OrgDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgDepartment::getParentId, parentId);
        List<OrgDepartment> list = list(wrapper);
        return list.stream()
                .map(OrgDepartmentConvert.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrgUserDTO> getUsersByDeptId(Serializable deptId) {
        List<OrgDepartmentUserDTO> users = getBaseMapper().getUsersByDeptId(deptId);
        return Collections.emptyList();
    }

    @Override
    public Paging<OrgDepartmentUserDTO> getUsersByDeptId(Serializable deptId, OrgDepartmentPageParam param) {
        List<OrgDepartmentUserDTO> allUsers = getBaseMapper().getUsersByDeptId(deptId);

        int total = allUsers.size();
        int pages = (int) Math.ceil((double) total / param.getSize());
        int fromIndex = (param.getCurrent() - 1) * param.getSize();
        int toIndex = Math.min(fromIndex + param.getSize(), total);

        List<OrgDepartmentUserDTO> records = fromIndex < total
                ? allUsers.subList(fromIndex, toIndex)
                : Collections.emptyList();

        Paging<OrgDepartmentUserDTO> paging = new Paging<>();
        paging.setCurrent(param.getCurrent());
        paging.setSize(param.getSize());
        paging.setTotal(total);
        paging.setPages(pages);
        paging.setRecords(records);
        return paging;
    }

}
