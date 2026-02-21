package com.yunjin.board.manager;

import com.yunjin.board.pojo.dto.BoardCustomConfigDTO;
import com.yunjin.board.pojo.entity.BoardCustomConfig;
import com.yunjin.board.pojo.enums.CustomConfigType;
import com.yunjin.datasource.base.BaseService;

/**
 * <p>
 * 看板自定义配置 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
public interface BoardCustomConfigManager extends BaseService<BoardCustomConfig> {

    /**
     * 新增看板自定义配置
     *
     * @param dto 看板自定义配置数据
     * @return boolean
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    boolean add(BoardCustomConfigDTO dto);

    /**
     * 修改看板自定义配置信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    boolean modify(BoardCustomConfigDTO dto);

    /**
     * 获取配置
     *
     * @param key  参数0
     * @param type 参数1
     * @return com.yunjin.board.pojo.dto.BoardCustomConfigDTO
     * @throws
     * @author Carlos
     * @date 2025-05-13 14:04
     */
    BoardCustomConfigDTO getConfig(String key, CustomConfigType type);
}
