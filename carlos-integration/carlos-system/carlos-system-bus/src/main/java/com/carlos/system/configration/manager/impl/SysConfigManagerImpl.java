package com.carlos.system.configration.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.system.configration.convert.SysConfigConvert;
import com.carlos.system.configration.manager.SysConfigManager;
import com.carlos.system.configration.mapper.SysConfigMapper;
import com.carlos.system.configration.pojo.dto.SysConfigDTO;
import com.carlos.system.configration.pojo.entity.SysConfig;
import com.carlos.system.configration.pojo.param.SysConfigPageParam;
import com.carlos.system.configration.pojo.vo.SysConfigVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统配置 查询封装实现类
 * </p>
 *
 * @author carlos
 * @date 2022-11-3 13:47:54
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SysConfigManagerImpl extends BaseServiceImpl<SysConfigMapper, SysConfig> implements SysConfigManager {


    @Override
    public boolean add(SysConfigDTO dto) {
        SysConfig entity = SysConfigConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'SysConfig' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'SysConfig' data: id:{}", entity.getId());
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
            log.warn("Remove 'SysConfig' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'SysConfig' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(SysConfigDTO dto) {
        SysConfig entity = SysConfigConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'SysConfig' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'SysConfig' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public SysConfigDTO getDtoById(String id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        SysConfig entity = getBaseMapper().selectById(id);
        return SysConfigConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<SysConfigVO> getPage(SysConfigPageParam param) {
        LambdaQueryWrapper<SysConfig> wrapper = queryWrapper();
        wrapper.select(
                SysConfig::getId,
                SysConfig::getConfigName,
                SysConfig::getConfigCode,
                SysConfig::getConfigValue,
                SysConfig::getValueType,
                SysConfig::getState,
                SysConfig::getRemark,
                SysConfig::getCreateBy,
                SysConfig::getCreateTime,
                SysConfig::getUpdateBy,
                SysConfig::getUpdateTime
        );
        wrapper.orderByDesc(SysConfig::getCreateTime);
        if (StrUtil.isNotBlank(param.getConfigName())) {
            wrapper.like(SysConfig::getConfigName, param.getConfigName()).or().like(SysConfig::getConfigCode, param.getConfigName());
        }

        PageInfo<SysConfig> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, SysConfigConvert.INSTANCE::toVO);
    }


    @Override
    public List<SysConfigDTO> getAllConfig(Set<String> excludes) {
        List<SysConfig> list = lambdaQuery()
                .select(
                        SysConfig::getConfigName,
                        SysConfig::getConfigCode,
                        SysConfig::getConfigValue
                ).eq(SysConfig::getState, true)
                .notIn(CollUtil.isNotEmpty(excludes), SysConfig::getConfigCode, excludes)
                .list();
        return SysConfigConvert.INSTANCE.toDTO(list);
    }

    @Override
    public List<SysConfigDTO> listByCodes(Set<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return null;
        }
        return SysConfigConvert.INSTANCE.toDTO(lambdaQuery().select(
                SysConfig::getConfigName,
                SysConfig::getConfigCode,
                SysConfig::getConfigValue,
                SysConfig::getValueType,
                SysConfig::getState
        ).in(SysConfig::getConfigCode, codes).list());
    }
}
