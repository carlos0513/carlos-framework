package com.yunjin.resource.manager;

import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseService;
import com.yunjin.resource.pojo.dto.ResourceGroupItemDTO;
import com.yunjin.resource.pojo.entity.ResourceGroupItem;
import com.yunjin.resource.pojo.param.ResourceGroupItemPageParam;
import com.yunjin.resource.pojo.vo.ResourceGroupItemVO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 资源组详情项 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
public interface ResourceGroupItemManager extends BaseService<ResourceGroupItem> {

    /**
     * 新增资源组详情项
     *
     * @param dto 资源组详情项数据
     * @return boolean
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    boolean add(ResourceGroupItemDTO dto);

    /**
     * 删除资源组详情项
     *
     * @param id 资源组详情项id
     * @return boolean
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    boolean delete(Serializable id);

    /**
     * 修改资源组详情项信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    boolean modify(ResourceGroupItemDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.yunjin.system.pojo.dto.ResourceGroupItemDTO
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    ResourceGroupItemDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    Paging<ResourceGroupItemVO> getPage(ResourceGroupItemPageParam param);

    /**
     * @Title: listByGroupIds
     * @Description: 根据资源组id获取
     * @Date: 2024/8/22 17:37
     * @Parameters: [resourceGroupIds]
     * @Return java.util.List<com.yunjin.system.pojo.dto.ResourceGroupItemDTO>
     */
    List<ResourceGroupItemDTO> listByGroupIds(Set<String> resourceGroupIds);
}
