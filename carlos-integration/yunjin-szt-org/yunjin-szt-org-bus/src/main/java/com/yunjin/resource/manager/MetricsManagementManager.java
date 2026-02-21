package com.yunjin.resource.manager;

import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseService;
import com.yunjin.resource.pojo.dto.MetricsManagementDTO;
import com.yunjin.resource.pojo.entity.MetricsManagement;
import com.yunjin.resource.pojo.param.MetricsManagementPageParam;
import com.yunjin.resource.pojo.vo.MetricsManagementVO;

import java.io.Serializable;

/**
 * <p>
 * 系统指标管理 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
public interface MetricsManagementManager extends BaseService<MetricsManagement> {

    /**
     * 新增系统指标管理
     *
     * @param dto 系统指标管理数据
     * @return boolean
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    boolean add(MetricsManagementDTO dto);

    /**
     * 删除系统指标管理
     *
     * @param id 系统指标管理id
     * @return boolean
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    boolean delete(Serializable id);

    /**
     * 修改系统指标管理信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    boolean modify(MetricsManagementDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.yunjin.system.pojo.dto.MetricsManagementDTO
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    MetricsManagementDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    Paging<MetricsManagementVO> getPage(MetricsManagementPageParam param);
}
