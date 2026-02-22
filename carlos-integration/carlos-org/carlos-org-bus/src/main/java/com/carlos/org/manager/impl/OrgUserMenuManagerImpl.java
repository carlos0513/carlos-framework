package com.carlos.org.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.convert.OrgUserMenuConvert;
import com.carlos.org.manager.OrgUserMenuManager;
import com.carlos.org.mapper.OrgUserMenuMapper;
import com.carlos.org.pojo.dto.OrgUserMenuDTO;
import com.carlos.org.pojo.entity.OrgUserMenu;
import com.carlos.org.pojo.param.OrgUserMenuPageParam;
import com.carlos.org.pojo.vo.OrgUserMenuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
/**
 * <p>
 * 用户菜单收藏表 查询封装实现类
 * </p>
 *
 * @author carlos
 * @date    2024-2-28 11:10:01
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrgUserMenuManagerImpl  extends BaseServiceImpl<OrgUserMenuMapper, OrgUserMenu> implements OrgUserMenuManager {

    @Override
    public boolean add(OrgUserMenuDTO dto) {
        OrgUserMenu entity = OrgUserMenuConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'OrgUserMenu' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'OrgUserMenu' data: id:{}", entity.getId());
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
            log.warn("Remove 'OrgUserMenu' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'OrgUserMenu' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(OrgUserMenuDTO dto) {
        OrgUserMenu entity = OrgUserMenuConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'OrgUserMenu' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'OrgUserMenu' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public OrgUserMenuDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        OrgUserMenu entity = getBaseMapper().selectById(id);
        return OrgUserMenuConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<OrgUserMenuVO> getPage(OrgUserMenuPageParam param){
        LambdaQueryWrapper<OrgUserMenu> wrapper = queryWrapper();
        wrapper.select(
                OrgUserMenu::getId,
                OrgUserMenu::getUserId,
                OrgUserMenu::getMenuId
                );
        PageInfo<OrgUserMenu> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, OrgUserMenuConvert.INSTANCE::toVO);
    }

}
