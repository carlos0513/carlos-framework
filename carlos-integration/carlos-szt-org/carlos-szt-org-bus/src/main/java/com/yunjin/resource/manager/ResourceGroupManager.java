package com.yunjin.resource.manager;

import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseService;
import com.yunjin.resource.pojo.dto.ResourceGroupDTO;
import com.yunjin.resource.pojo.entity.ResourceGroup;
import com.yunjin.resource.pojo.param.ResourceGroupPageParam;
import com.yunjin.resource.pojo.vo.ResourceGroupVO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 资源组 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
public interface ResourceGroupManager extends BaseService<ResourceGroup> {

    /**
     * 新增资源组
     *
     * @param dto 资源组数据
     * @return boolean
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    boolean add(ResourceGroupDTO dto);

    /**
     * 删除资源组
     *
     * @param id 资源组id
     * @return boolean
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    boolean delete(Serializable id);

    /**
     * 修改资源组信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    boolean modify(ResourceGroupDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.yunjin.system.pojo.dto.ResourceGroupDTO
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    ResourceGroupDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    Paging<ResourceGroupVO> getPage(ResourceGroupPageParam param);

    /**
     * @Title: listAll
     * @Description: 获取全量列表
     * @Date: 2024/8/22 14:31
     * @Parameters: []
     * @Return java.util.List<com.yunjin.system.pojo.dto.ResourceGroupDTO>
     */
    List<ResourceGroupDTO> listAll();

    /**
     * 通过groupIds获取资源组
     *
     * @param groupIds ids
     * @return List<ResourceGroupDTO>
     */
    List<ResourceGroupDTO> getByGroupIds(Set<String> groupIds);
}
