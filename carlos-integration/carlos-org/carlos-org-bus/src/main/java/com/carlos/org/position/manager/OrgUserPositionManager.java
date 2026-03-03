package com.carlos.org.position.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.org.position.pojo.dto.OrgUserPositionDTO;
import com.carlos.org.position.pojo.entity.OrgUserPosition;
import com.carlos.org.position.pojo.param.OrgUserPositionPageParam;
import com.carlos.org.position.pojo.vo.OrgUserPositionVO;

import java.io.Serializable;

/**
 * <p>
 * 用户岗位职级关联表（核心任职信息） 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
public interface OrgUserPositionManager extends BaseService<OrgUserPosition> {

    /**
     * 新增用户岗位职级关联表（核心任职信息）
     *
     * @param dto 用户岗位职级关联表（核心任职信息）数据
     * @return boolean
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    boolean add(OrgUserPositionDTO dto);

    /**
     * 删除用户岗位职级关联表（核心任职信息）
     *
     * @param id 用户岗位职级关联表（核心任职信息）id
     * @return boolean
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    boolean delete(Serializable id);

    /**
     * 修改用户岗位职级关联表（核心任职信息）信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    boolean modify(OrgUserPositionDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.org.position.pojo.dto.OrgUserPositionDTO
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    OrgUserPositionDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    Paging<OrgUserPositionVO> getPage(OrgUserPositionPageParam param);
}
