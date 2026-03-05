package com.carlos.audit.manager;

import com.carlos.audit.pojo.dto.AuditLogTechnicalDTO;
import com.carlos.audit.pojo.entity.AuditLogTechnical;
import com.carlos.audit.pojo.param.AuditLogTechnicalPageParam;
import com.carlos.audit.pojo.vo.AuditLogTechnicalVO;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;

import java.io.Serializable;

/**
 * <p>
 * 审计日志-技术上下文 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
public interface AuditLogTechnicalManager extends BaseService<AuditLogTechnical> {

    /**
     * 新增审计日志-技术上下文
     *
     * @param dto 审计日志-技术上下文数据
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean add(AuditLogTechnicalDTO dto);

    /**
     * 删除审计日志-技术上下文
     *
     * @param id 审计日志-技术上下文id
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean delete(Serializable id);

    /**
     * 修改审计日志-技术上下文信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean modify(AuditLogTechnicalDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.audit.pojo.dto.AuditLogTechnicalDTO
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogTechnicalDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    Paging<AuditLogTechnicalVO> getPage(AuditLogTechnicalPageParam param);
}
