package com.carlos.system.menu.manager.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.system.menu.convert.MenuOperateConvert;
import com.carlos.system.menu.manager.MenuOperateManager;
import com.carlos.system.menu.mapper.MenuOperateMapper;
import com.carlos.system.menu.pojo.dto.MenuOperateDTO;
import com.carlos.system.menu.pojo.entity.MenuOperate;
import com.carlos.system.menu.pojo.param.MenuOperatePageParam;
import com.carlos.system.menu.pojo.vo.MenuOperateVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单操作 查询封装实现类
 * </p>
 *
 * @author carlos
 * @date 2023-7-7 14:19:55
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MenuOperateManagerImpl extends BaseServiceImpl<MenuOperateMapper, MenuOperate> implements MenuOperateManager {

    @Override
    public boolean add(MenuOperateDTO dto) {
        MenuOperate entity = MenuOperateConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'MenuOperate' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'MenuOperate' data: id:{}", entity.getId());
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
            log.warn("Remove 'MenuOperate' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'MenuOperate' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(MenuOperateDTO dto) {
        MenuOperate entity = MenuOperateConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'MenuOperate' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'MenuOperate' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public MenuOperateDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        MenuOperate entity = getBaseMapper().selectById(id);
        return MenuOperateConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<MenuOperateVO> getPage(MenuOperatePageParam param) {
        LambdaQueryWrapper<MenuOperate> wrapper = queryWrapper();
        wrapper.select(
                MenuOperate::getId,
                MenuOperate::getOperateName,
                MenuOperate::getOperateCode,
                MenuOperate::getPath,
                MenuOperate::getOperateMethod,
                MenuOperate::getIcon,
                MenuOperate::getOperateType,
                MenuOperate::getState,
                MenuOperate::getHidden,
                MenuOperate::getDescription,
                MenuOperate::getCreateTime,
                MenuOperate::getUpdateTime
        );
        PageInfo<MenuOperate> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, MenuOperateConvert.INSTANCE::toVO);
    }

    @Override
    public List<MenuOperateDTO> listByKeyword(String keyword) {
        List<MenuOperate> list = lambdaQuery()
                .like(StrUtil.isNotBlank(keyword), MenuOperate::getOperateName, keyword)
                .or(StrUtil.isNotBlank(keyword))
                .like(StrUtil.isNotBlank(keyword), MenuOperate::getDescription, keyword)
                .list();
        return MenuOperateConvert.INSTANCE.toDTO(list);
    }

    @Override
    public List<MenuOperateDTO> listById(Set<String> ids) {
        List<MenuOperate> list = listByIds(ids);
        return MenuOperateConvert.INSTANCE.toDTO(list);
    }

    @Override
    public List<MenuOperateDTO> listByMenuId(String menuId) {
        List<MenuOperate> list = lambdaQuery().eq(StrUtil.isNotBlank(menuId), MenuOperate::getMenuId, menuId).list();
        return MenuOperateConvert.INSTANCE.toDTO(list);
    }

    @Override
    public List<MenuOperateDTO> listByMenuIds(Set<String> menuIds) {
        List<MenuOperate> list = lambdaQuery().in(CollectionUtil.isNotEmpty(menuIds), MenuOperate::getMenuId, menuIds).list();
        return MenuOperateConvert.INSTANCE.toDTO(list);
    }
}
