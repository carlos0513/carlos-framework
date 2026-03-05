package com.carlos.audit.manager;

import com.carlos.audit.pojo.dto.AuditLogFieldChangeDTO;
import com.carlos.audit.pojo.entity.AuditLogFieldChange;
import com.carlos.audit.pojo.param.AuditLogFieldChangePageParam;
import com.carlos.audit.pojo.vo.AuditLogFieldChangeVO;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;

import java.io.Serializable;

/**
 * <p>
 * 审计日志-字段级变更明细 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
public interface AuditLogFieldChangeManager extends BaseService<AuditLogFieldChange> {

    /**
     * 新增审计日志-字段级变更明细
     *
     * @param dto 审计日志-字段级变更明细数据
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean add(AuditLogFieldChangeDTO dto);

    /**
     * 删除审计日志-字段级变更明细
     *
     * @param id 审计日志-字段级变更明细id
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean delete(Serializable id);

    /**
     * 修改审计日志-字段级变更明细信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean modify(AuditLogFieldChangeDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.audit.pojo.dto.AuditLogFieldChangeDTO
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogFieldChangeDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    Paging<AuditLogFieldChangeVO> getPage(AuditLogFieldChangePageParam param);
}
