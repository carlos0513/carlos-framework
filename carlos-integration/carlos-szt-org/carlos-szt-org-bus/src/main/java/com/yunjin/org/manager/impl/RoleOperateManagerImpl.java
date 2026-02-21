package com.yunjin.org.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.datasource.pagination.MybatisPage;
import com.yunjin.datasource.pagination.PageInfo;
import com.yunjin.org.convert.RoleOperateConvert;
import com.yunjin.org.manager.RoleOperateManager;
import com.yunjin.org.mapper.RoleOperateMapper;
import com.yunjin.org.pojo.dto.RoleOperateDTO;
import com.yunjin.org.pojo.entity.RoleOperate;
import com.yunjin.org.pojo.param.RoleOperatePageParam;
import com.yunjin.org.pojo.vo.RoleOperateVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 角色菜单操作表 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RoleOperateManagerImpl extends BaseServiceImpl<RoleOperateMapper, RoleOperate> implements RoleOperateManager {

    @Override
    public boolean add(RoleOperateDTO dto) {
        RoleOperate entity = RoleOperateConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'RoleOperate' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'RoleOperate' data: id:{}", entity.getId());
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
            log.warn("Remove 'RoleOperate' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'RoleOperate' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(RoleOperateDTO dto) {
        RoleOperate entity = RoleOperateConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'RoleOperate' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'RoleOperate' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public RoleOperateDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        RoleOperate entity = getBaseMapper().selectById(id);
        return RoleOperateConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<RoleOperateVO> getPage(RoleOperatePageParam param) {
        LambdaQueryWrapper<RoleOperate> wrapper = queryWrapper();
        wrapper.select(
                RoleOperate::getId,
                RoleOperate::getRoleId,
                RoleOperate::getOperateId
        );
        PageInfo<RoleOperate> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, RoleOperateConvert.INSTANCE::toVO);
    }

}
