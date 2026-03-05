package com.carlos.audit.manager;

import com.carlos.audit.pojo.dto.AuditLogDataChangeDTO;
import com.carlos.audit.pojo.entity.AuditLogDataChange;
import com.carlos.audit.pojo.param.AuditLogDataChangePageParam;
import com.carlos.audit.pojo.vo.AuditLogDataChangeVO;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;

import java.io.Serializable;

/**
 * <p>
 * 审计日志-数据变更详情 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
public interface AuditLogDataChangeManager extends BaseService<AuditLogDataChange> {

    /**
     * 新增审计日志-数据变更详情
     *
     * @param dto 审计日志-数据变更详情数据
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean add(AuditLogDataChangeDTO dto);

    /**
     * 删除审计日志-数据变更详情
     *
     * @param id 审计日志-数据变更详情id
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean delete(Serializable id);

    /**
     * 修改审计日志-数据变更详情信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean modify(AuditLogDataChangeDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.audit.pojo.dto.AuditLogDataChangeDTO
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogDataChangeDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    Paging<AuditLogDataChangeVO> getPage(AuditLogDataChangePageParam param);
}
