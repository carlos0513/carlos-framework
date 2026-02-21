package com.yunjin.org.manager;

import com.yunjin.datasource.base.BaseService;
import com.yunjin.org.pojo.dto.OrgComplaintLogDTO;
import com.yunjin.org.pojo.entity.OrgComplaintLog;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 投诉建议处理节点日志 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
public interface OrgComplaintLogManager extends BaseService<OrgComplaintLog> {

    /**
     * 新增投诉建议处理节点日志
     *
     * @param dto 投诉建议处理节点日志数据
     * @return boolean
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    boolean add(OrgComplaintLogDTO dto);

    /**
     * 删除投诉建议处理节点日志
     *
     * @param id 投诉建议处理节点日志id
     * @return boolean
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    boolean delete(Serializable id);


    OrgComplaintLogDTO getLastAcceptedLog(String complaintId);

    List<OrgComplaintLogDTO> getDtoByComplaintId(String complaintId);
}
