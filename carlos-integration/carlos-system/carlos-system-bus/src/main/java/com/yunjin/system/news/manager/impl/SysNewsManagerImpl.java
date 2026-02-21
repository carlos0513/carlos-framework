package com.carlos.system.news.manager.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.system.enums.UserMessageType;
import com.carlos.system.news.convert.SysNewsConvert;
import com.carlos.system.news.manager.SysNewsManager;
import com.carlos.system.news.mapper.SysNewsMapper;
import com.carlos.system.news.pojo.dto.SysNewsDTO;
import com.carlos.system.news.pojo.entity.SysNews;
import com.carlos.system.news.pojo.param.SysNewsPageParam;
import com.carlos.system.news.pojo.vo.SysNewsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统-通知公告 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SysNewsManagerImpl extends BaseServiceImpl<SysNewsMapper, SysNews> implements SysNewsManager {

    @Override
    public boolean add(SysNewsDTO dto) {
        SysNews entity = SysNewsConvert.INSTANCE.toDO(dto);
        boolean success = this.save(entity);
        if (!success) {
            log.warn("Insert 'SysNews' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'SysNews' data: id:{}", entity.getId());
        }
        return true;
    }

    @Override
    public boolean delete(Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        boolean success = this.removeById(id);
        if (!success) {
            log.warn("Remove 'SysNews' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'SysNews' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(SysNewsDTO dto) {
        SysNews entity = SysNewsConvert.INSTANCE.toDO(dto);
        boolean success = this.updateById(entity);
        if (!success) {
            log.warn("Update 'SysNews' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'SysNews' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public SysNewsDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        SysNews entity = this.getBaseMapper().selectById(id);
        return SysNewsConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<SysNewsVO> getPage(SysNewsPageParam param) {
        LambdaQueryWrapper<SysNews> wrapper = this.queryWrapper();
        wrapper.orderByDesc(SysNews::getCreateTime);
        wrapper.select(
                SysNews::getId,
                SysNews::getTitle,
                SysNews::getSource,
                SysNews::getImage,
                SysNews::getSendDate,
                SysNews::getContent,
                SysNews::getEnabled,
                SysNews::getIntroducing,
                SysNews::getCreateBy,
                SysNews::getCreateTime,
                SysNews::getUpdateBy,
                SysNews::getUpdateTime
        );
        //只查公告  type=1
        wrapper.eq(SysNews::getType, UserMessageType.MESSAGE.getCode());
        if (StrUtil.isNotBlank(param.getTitle())) {
            wrapper.like(SysNews::getTitle, param.getTitle());
        }
        if (StrUtil.isNotBlank(param.getSource())) {
            wrapper.like(SysNews::getSource, param.getSource());
        }
        if (param.getSendDate() != null) {
            wrapper.eq(SysNews::getSendDate, param.getSendDate());
        }
        if (param.getStart() != null) {
            wrapper.ge(SysNews::getSendDate, param.getStart());
        }
        if (param.getEnd() != null) {
            wrapper.le(SysNews::getSendDate, param.getEnd());
        }

        PageInfo<SysNews> page = this.page(this.pageInfo(param), wrapper);
        log.info("通知公告分页查询" + System.currentTimeMillis());
        return MybatisPage.convert(page, SysNewsConvert.INSTANCE::toVO);
    }

    @Override
    public List<SysNewsDTO> listAll() {
        LambdaQueryWrapper<SysNews> wrapper = this.queryWrapper();
        wrapper.orderByDesc(SysNews::getCreateTime);
        wrapper.select(
                SysNews::getId,
                SysNews::getTitle,
                SysNews::getType,
                SysNews::getSource,
                SysNews::getSendDate,
                SysNews::getContent,
                SysNews::getEnabled,
                SysNews::getIntroducing,
                SysNews::getCreateBy,
                SysNews::getCreateTime,
                SysNews::getUpdateBy,
                SysNews::getUpdateTime
        );
        List<SysNews> news = baseMapper.selectList(wrapper);
        return SysNewsConvert.INSTANCE.toDTO(news);
    }

}
