package com.carlos.audit.manager;

import com.carlos.audit.pojo.dto.AuditLogTagsDTO;
import com.carlos.audit.pojo.entity.AuditLogTags;
import com.carlos.audit.pojo.param.AuditLogTagsPageParam;
import com.carlos.audit.pojo.vo.AuditLogTagsVO;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;

import java.io.Serializable;

/**
 * <p>
 * 审计日志-动态标签 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
public interface AuditLogTagsManager extends BaseService<AuditLogTags> {

    /**
     * 新增审计日志-动态标签
     *
     * @param dto 审计日志-动态标签数据
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean add(AuditLogTagsDTO dto);

    /**
     * 删除审计日志-动态标签
     *
     * @param id 审计日志-动态标签id
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean delete(Serializable id);

    /**
     * 修改审计日志-动态标签信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    boolean modify(AuditLogTagsDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.audit.pojo.dto.AuditLogTagsDTO
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogTagsDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    Paging<AuditLogTagsVO> getPage(AuditLogTagsPageParam param);
}
