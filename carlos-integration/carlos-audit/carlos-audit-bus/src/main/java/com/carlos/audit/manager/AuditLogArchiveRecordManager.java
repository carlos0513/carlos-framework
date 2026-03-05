package com.carlos.audit.manager;

import com.carlos.audit.pojo.dto.AuditLogArchiveRecordDTO;
import com.carlos.audit.pojo.entity.AuditLogArchiveRecord;
import com.carlos.audit.pojo.param.AuditLogArchiveRecordPageParam;
import com.carlos.audit.pojo.vo.AuditLogArchiveRecordVO;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;

import java.io.Serializable;

/**
 * <p>
 * 审计日志归档记录 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
public interface AuditLogArchiveRecordManager extends BaseService<AuditLogArchiveRecord> {

    /**
     * 新增审计日志归档记录
     *
     * @param dto 审计日志归档记录数据
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean add(AuditLogArchiveRecordDTO dto);

    /**
     * 删除审计日志归档记录
     *
     * @param id 审计日志归档记录id
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean delete(Serializable id);

    /**
     * 修改审计日志归档记录信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean modify(AuditLogArchiveRecordDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.audit.pojo.dto.AuditLogArchiveRecordDTO
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogArchiveRecordDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    Paging<AuditLogArchiveRecordVO> getPage(AuditLogArchiveRecordPageParam param);
}
