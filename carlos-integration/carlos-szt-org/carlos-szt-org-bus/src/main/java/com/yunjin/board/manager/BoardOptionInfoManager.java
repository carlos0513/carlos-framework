package com.yunjin.board.manager;

import com.yunjin.board.pojo.dto.BoardOptionInfoDTO;
import com.yunjin.board.pojo.entity.BoardOptionInfo;
import com.yunjin.board.pojo.param.BoardOptionInfoPageParam;
import com.yunjin.board.pojo.vo.BoardOptionInfoVO;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseService;

import java.io.Serializable;

/**
 * <p>
 * 工作台卡片选项信息 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
public interface BoardOptionInfoManager extends BaseService<BoardOptionInfo> {

    /**
     * 新增工作台卡片选项信息
     *
     * @param dto 工作台卡片选项信息数据
     * @return boolean
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    boolean add(BoardOptionInfoDTO dto);

    /**
     * 删除工作台卡片选项信息
     *
     * @param id 工作台卡片选项信息id
     * @return boolean
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    boolean delete(Serializable id);

    /**
     * 修改工作台卡片选项信息信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    boolean modify(BoardOptionInfoDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.yunjin.board.pojo.dto.BoardOptionInfoDTO
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardOptionInfoDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    Paging<BoardOptionInfoVO> getPage(BoardOptionInfoPageParam param);
}
