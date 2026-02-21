package com.yunjin.board.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.board.convert.BoardOptionInfoConvert;
import com.yunjin.board.manager.BoardOptionInfoManager;
import com.yunjin.board.mapper.BoardOptionInfoMapper;
import com.yunjin.board.pojo.dto.BoardOptionInfoDTO;
import com.yunjin.board.pojo.entity.BoardOptionInfo;
import com.yunjin.board.pojo.param.BoardOptionInfoPageParam;
import com.yunjin.board.pojo.vo.BoardOptionInfoVO;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.datasource.pagination.MybatisPage;
import com.yunjin.datasource.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 工作台卡片选项信息 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BoardOptionInfoManagerImpl extends BaseServiceImpl<BoardOptionInfoMapper, BoardOptionInfo> implements BoardOptionInfoManager {

    @Override
    public boolean add(BoardOptionInfoDTO dto) {
        BoardOptionInfo entity = BoardOptionInfoConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'BoardOptionInfo' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'BoardOptionInfo' data: id:{}", entity.getId());
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
            log.warn("Remove 'BoardOptionInfo' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'BoardOptionInfo' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(BoardOptionInfoDTO dto) {
        BoardOptionInfo entity = BoardOptionInfoConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'BoardOptionInfo' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'BoardOptionInfo' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public BoardOptionInfoDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        BoardOptionInfo entity = getBaseMapper().selectById(id);
        return BoardOptionInfoConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<BoardOptionInfoVO> getPage(BoardOptionInfoPageParam param) {
        LambdaQueryWrapper<BoardOptionInfo> wrapper = queryWrapper();
        wrapper.select(
                BoardOptionInfo::getId,
                BoardOptionInfo::getOptionId,
                BoardOptionInfo::getOptionType,
                BoardOptionInfo::getDescription,
                BoardOptionInfo::getCreateBy,
                BoardOptionInfo::getCreateTime,
                BoardOptionInfo::getUpdateBy,
                BoardOptionInfo::getUpdateTime
        );
        PageInfo<BoardOptionInfo> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, BoardOptionInfoConvert.INSTANCE::toVO);
    }

}
