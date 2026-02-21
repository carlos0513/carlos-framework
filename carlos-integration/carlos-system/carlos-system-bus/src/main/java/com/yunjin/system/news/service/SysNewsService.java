package com.carlos.system.news.service;

import com.carlos.core.pagination.Paging;
import com.carlos.system.news.pojo.dto.SysNewsDTO;
import com.carlos.system.news.pojo.param.SysNewsPageParam;
import com.carlos.system.news.pojo.vo.SysNewsVO;
import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 系统-通知公告 业务接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
public interface SysNewsService {

    /**
     * 新增系统-通知公告
     *
     * @param dto 系统-通知公告数据
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    boolean addSysNews(SysNewsDTO dto);

    /**
     * 删除系统-通知公告
     *
     * @param ids 系统-通知公告id
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    void deleteSysNews(Set<Serializable> ids);

    /**
     * 修改系统-通知公告信息
     *
     * @param dto 对象信息
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    void updateSysNews(SysNewsDTO dto);

    Paging<SysNewsVO> getPage(SysNewsPageParam param);

    SysNewsVO getDtoById(String id);
}
