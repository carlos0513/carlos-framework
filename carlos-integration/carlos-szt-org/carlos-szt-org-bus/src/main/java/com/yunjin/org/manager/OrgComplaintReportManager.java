package com.yunjin.org.manager;


import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseService;
import com.yunjin.org.pojo.dto.OrgComplaintReportDTO;
import com.yunjin.org.pojo.entity.OrgComplaintReport;
import com.yunjin.org.pojo.param.OrgComplaintReportPageParam;
import com.yunjin.org.pojo.vo.OrgComplaintReportVO;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
public interface OrgComplaintReportManager extends BaseService<OrgComplaintReport> {

    /**
     * 新增
     *
     * @param dto 数据
     * @return boolean
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    boolean add(OrgComplaintReportDTO dto);

    /**
     * 删除
     *
     * @param id id
     * @return boolean
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    boolean delete(Serializable id);

    /**
     * 修改信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    boolean modify(OrgComplaintReportDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.yunjin.pojo.dto.OrgComplaintReportDTO
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    OrgComplaintReportDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param     分页参数
     * @param deptCodes
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    Paging<OrgComplaintReportVO> getPage(OrgComplaintReportPageParam param, Set<String> deptCodes);
}
