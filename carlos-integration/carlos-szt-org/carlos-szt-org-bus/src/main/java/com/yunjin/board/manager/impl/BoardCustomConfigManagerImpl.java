package com.yunjin.board.manager.impl;

import com.yunjin.board.convert.BoardCustomConfigConvert;
import com.yunjin.board.manager.BoardCustomConfigManager;
import com.yunjin.board.mapper.BoardCustomConfigMapper;
import com.yunjin.board.pojo.dto.BoardCustomConfigDTO;
import com.yunjin.board.pojo.entity.BoardCustomConfig;
import com.yunjin.board.pojo.enums.CustomConfigType;
import com.yunjin.datasource.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 看板自定义配置 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BoardCustomConfigManagerImpl extends BaseServiceImpl<BoardCustomConfigMapper, BoardCustomConfig> implements BoardCustomConfigManager {

    @Override
    public boolean add(BoardCustomConfigDTO dto) {
        BoardCustomConfig entity = BoardCustomConfigConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'BoardCustomConfig' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'BoardCustomConfig' data: id:{}", entity.getId());
        }
        return true;
    }


    @Override
    public boolean modify(BoardCustomConfigDTO dto) {
        BoardCustomConfig entity = BoardCustomConfigConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'BoardCustomConfig' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'BoardCustomConfig' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public BoardCustomConfigDTO getConfig(String key, CustomConfigType type) {
        if (key == null) {
            log.warn("key is null");
            return null;
        }
        BoardCustomConfig entity = lambdaQuery()
                .select(
                        BoardCustomConfig::getId,
                        BoardCustomConfig::getConfigKey,
                        BoardCustomConfig::getConfigJson
                )
                .eq(BoardCustomConfig::getConfigKey, key)
                .eq(BoardCustomConfig::getConfigType, type)
                .one();
        return BoardCustomConfigConvert.INSTANCE.toDTO(entity);
    }


}
