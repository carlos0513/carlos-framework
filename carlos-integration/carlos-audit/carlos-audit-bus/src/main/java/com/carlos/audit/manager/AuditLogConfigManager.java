package com.carlos.audit.manager;

import com.carlos.audit.pojo.dto.AuditLogConfigDTO;
import com.carlos.audit.pojo.entity.AuditLogConfig;
import com.carlos.audit.pojo.param.AuditLogConfigPageParam;
import com.carlos.audit.pojo.vo.AuditLogConfigVO;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;

import java.io.Serializable;

/**
 * <p>
 * 审计日志配置 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
public interface AuditLogConfigManager extends BaseService<AuditLogConfig> {

    /**
     * 新增审计日志配置
     *
     * @param dto 审计日志配置数据
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean add(AuditLogConfigDTO dto);

    /**
     * 删除审计日志配置
     *
     * @param id 审计日志配置id
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean delete(Serializable id);

    /**
     * 修改审计日志配置信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean modify(AuditLogConfigDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.audit.pojo.dto.AuditLogConfigDTO
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogConfigDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    Paging<AuditLogConfigVO> getPage(AuditLogConfigPageParam param);
}
