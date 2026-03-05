package com.carlos.audit.manager;

import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.carlos.audit.pojo.entity.AuditLogMain;
import com.carlos.audit.pojo.param.AuditLogMainPageParam;
import com.carlos.audit.pojo.vo.AuditLogMainVO;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;

import java.io.Serializable;

/**
 * <p>
 * 审计日志主表 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
public interface AuditLogMainManager extends BaseService<AuditLogMain> {

    /**
     * 新增审计日志主表
     *
     * @param dto 审计日志主表数据
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean add(AuditLogMainDTO dto);

    /**
     * 删除审计日志主表
     *
     * @param id 审计日志主表id
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean delete(Serializable id);

    /**
     * 修改审计日志主表信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean modify(AuditLogMainDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.audit.pojo.dto.AuditLogMainDTO
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogMainDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    Paging<AuditLogMainVO> getPage(AuditLogMainPageParam param);
}
