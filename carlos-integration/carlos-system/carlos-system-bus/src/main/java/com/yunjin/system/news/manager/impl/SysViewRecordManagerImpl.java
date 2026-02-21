package com.carlos.system.news.manager.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.system.news.convert.SysViewRecordConvert;
import com.carlos.system.news.manager.SysViewRecordManager;
import com.carlos.system.news.mapper.SysViewRecordMapper;
import com.carlos.system.news.pojo.dto.SysViewRecordDTO;
import com.carlos.system.news.pojo.entity.SysViewRecord;
import com.carlos.system.news.pojo.param.SysViewRecordPageParam;
import com.carlos.system.news.pojo.vo.SysViewRecordVO;
import java.io.Serializable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 浏览记录 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2023-1-13 16:31:50
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SysViewRecordManagerImpl extends BaseServiceImpl<SysViewRecordMapper, SysViewRecord> implements SysViewRecordManager {

    @Override
    public boolean add(final SysViewRecordDTO dto) {
        final SysViewRecord entity = SysViewRecordConvert.INSTANCE.toDO(dto);
        final boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'SysViewRecord' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'SysViewRecord' data: id:{}", entity.getId());
        }
        return true;
    }

    @Override
    public boolean delete(final Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        final boolean success = removeById(id);
        if (!success) {
            log.warn("Remove 'SysViewRecord' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'SysViewRecord' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(final SysViewRecordDTO dto) {
        final SysViewRecord entity = SysViewRecordConvert.INSTANCE.toDO(dto);
        final boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'SysViewRecord' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'SysViewRecord' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public SysViewRecordDTO getDtoById(final Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        final SysViewRecord entity = getBaseMapper().selectById(id);
        return SysViewRecordConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<SysViewRecordVO> getPage(final SysViewRecordPageParam param) {
        final LambdaQueryWrapper<SysViewRecord> wrapper = queryWrapper();
        wrapper.select(
                SysViewRecord::getId,
                SysViewRecord::getType,
                SysViewRecord::getId,
                SysViewRecord::getId,
                SysViewRecord::getCreateTime
        );
        final PageInfo<SysViewRecord> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, SysViewRecordConvert.INSTANCE::toVO);
    }

    @Override
    public long getCountByUserId(final Serializable userId, final int type) {
        if (StrUtil.isBlankIfStr(userId)) {
            return 0;
        }
        final LambdaQueryWrapper<SysViewRecord> wrapper = queryWrapper();
        wrapper.eq(SysViewRecord::getUserId, userId);
        wrapper.eq(SysViewRecord::getType, type);
        return count(wrapper);
    }

    @Override
    public SysViewRecordDTO getByUserIdAndReferenceId(final Serializable userId, final String newsId) {

        if (StrUtil.isBlank(userId.toString()) || StrUtil.isBlank(newsId)) {
            return null;
        }
        final LambdaQueryWrapper<SysViewRecord> wrapper = queryWrapper();
        wrapper.select(
                SysViewRecord::getId,
                SysViewRecord::getReferenceId,
                SysViewRecord::getUserId,
                SysViewRecord::getType
        );
        wrapper.eq(SysViewRecord::getReferenceId, newsId);
        wrapper.eq(SysViewRecord::getUserId, userId);
        wrapper.eq(SysViewRecord::getType, 0);
        final SysViewRecord one = getOne(wrapper);
        return SysViewRecordConvert.INSTANCE.toDTO(one);
    }

}
