package com.carlos.system.news.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.system.news.pojo.dto.SysNewsDTO;
import com.carlos.system.news.pojo.entity.SysNews;
import com.carlos.system.news.pojo.param.SysNewsPageParam;
import com.carlos.system.news.pojo.vo.SysNewsVO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统-通知公告 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
public interface SysNewsManager extends BaseService<SysNews> {

    /**
     * 新增系统-通知公告
     *
     * @param dto 系统-通知公告数据
     * @return boolean
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    boolean add(SysNewsDTO dto);

    /**
     * 删除系统-通知公告
     *
     * @param id 系统-通知公告id
     * @return boolean
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    boolean delete(Serializable id);

    /**
     * 修改系统-通知公告信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    boolean modify(SysNewsDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.system.news.pojo.dto.SysNewsDTO
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    SysNewsDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    Paging<SysNewsVO> getPage(SysNewsPageParam param);

    /**
     * 列出所有公告
     *
     * @return java.util.List<com.carlos.system.news.pojo.dto.SysNewsDTO>
     * @throws
     * @author Carlos
     * @date 2024/2/26 15:14
     */
    List<SysNewsDTO> listAll();
}
