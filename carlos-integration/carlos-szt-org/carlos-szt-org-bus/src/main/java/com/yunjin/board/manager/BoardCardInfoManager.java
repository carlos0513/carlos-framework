package com.yunjin.board.manager;

import com.yunjin.board.pojo.dto.BoardCardInfoDTO;
import com.yunjin.board.pojo.entity.BoardCardInfo;
import com.yunjin.board.pojo.param.BoardCardInfoPageParam;
import com.yunjin.board.pojo.vo.BoardCardInfoVO;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseService;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工作台卡片信息 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
public interface BoardCardInfoManager extends BaseService<BoardCardInfo> {

    /**
     * 新增工作台卡片信息
     *
     * @param dto 工作台卡片信息数据
     * @return boolean
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    boolean add(BoardCardInfoDTO dto);

    /**
     * 删除工作台卡片信息
     *
     * @param id 工作台卡片信息id
     * @return boolean
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    boolean delete(Serializable id);

    /**
     * 修改工作台卡片信息信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    boolean modify(BoardCardInfoDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.yunjin.board.pojo.dto.BoardCardInfoDTO
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardCardInfoDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    Paging<BoardCardInfoVO> getPage(BoardCardInfoPageParam param);

    /**
     * 获取所有配置
     *
     * @return java.util.List<com.yunjin.board.pojo.dto.BoardCardInfoDTO>
     * @throws
     * @author Carlos
     * @date 2025-05-13 14:12
     */
    List<BoardCardInfoDTO> getAllCard();
}
