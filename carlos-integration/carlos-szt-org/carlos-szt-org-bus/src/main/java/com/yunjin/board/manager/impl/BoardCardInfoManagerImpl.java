package com.yunjin.board.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.board.convert.BoardCardInfoConvert;
import com.yunjin.board.manager.BoardCardInfoManager;
import com.yunjin.board.mapper.BoardCardInfoMapper;
import com.yunjin.board.pojo.dto.BoardCardInfoDTO;
import com.yunjin.board.pojo.entity.BoardCardInfo;
import com.yunjin.board.pojo.param.BoardCardInfoPageParam;
import com.yunjin.board.pojo.vo.BoardCardInfoVO;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.datasource.pagination.MybatisPage;
import com.yunjin.datasource.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工作台卡片信息 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BoardCardInfoManagerImpl extends BaseServiceImpl<BoardCardInfoMapper, BoardCardInfo> implements BoardCardInfoManager {

    @Override
    public boolean add(BoardCardInfoDTO dto) {
        BoardCardInfo entity = BoardCardInfoConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'BoardCardInfo' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'BoardCardInfo' data: id:{}", entity.getId());
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
            log.warn("Remove 'BoardCardInfo' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'BoardCardInfo' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(BoardCardInfoDTO dto) {
        BoardCardInfo entity = BoardCardInfoConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'BoardCardInfo' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'BoardCardInfo' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public BoardCardInfoDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        BoardCardInfo entity = getBaseMapper().selectById(id);
        return BoardCardInfoConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<BoardCardInfoVO> getPage(BoardCardInfoPageParam param) {
        LambdaQueryWrapper<BoardCardInfo> wrapper = queryWrapper();
        wrapper.select(
                BoardCardInfo::getId,
                BoardCardInfo::getCardName,
                BoardCardInfo::getCardCode,
                BoardCardInfo::getComponent,
                BoardCardInfo::getThumbnail,
                BoardCardInfo::getDescription,
                BoardCardInfo::getCreateBy,
                BoardCardInfo::getCreateTime,
                BoardCardInfo::getUpdateBy,
                BoardCardInfo::getUpdateTime
        );
        PageInfo<BoardCardInfo> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, BoardCardInfoConvert.INSTANCE::toVO);
    }

    @Override
    public List<BoardCardInfoDTO> getAllCard() {
        List<BoardCardInfo> entities = lambdaQuery().select(
                BoardCardInfo::getId,
                BoardCardInfo::getCardName,
                BoardCardInfo::getCardCode,
                BoardCardInfo::getComponent,
                BoardCardInfo::getThumbnail,
                BoardCardInfo::getDescription
        ).list();
        return BoardCardInfoConvert.INSTANCE.toDTO(entities);
    }
}
