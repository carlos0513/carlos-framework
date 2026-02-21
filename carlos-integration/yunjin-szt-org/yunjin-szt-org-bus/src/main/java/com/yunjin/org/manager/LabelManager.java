package com.yunjin.org.manager;

import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseService;
import com.yunjin.org.enums.LabelTypeEnum;
import com.yunjin.org.pojo.dto.LabelDTO;
import com.yunjin.org.pojo.entity.Label;
import com.yunjin.org.pojo.param.LabelPageParam;
import com.yunjin.org.pojo.vo.LabelVO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 标签 查询封装接口
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-23 12:31:52
 */
public interface LabelManager extends BaseService<Label>{

    /**
     * 新增标签
     *
     * @param dto 标签数据
     * @return boolean
     * @author  yunjin
     * @date    2024-3-23 12:31:52
     */
    boolean add(LabelDTO dto);

    /**
     * 删除标签
     *
     * @param id 标签id
     * @return boolean
     * @author  yunjin
     * @date    2024-3-23 12:31:52
     */
    boolean delete(Serializable id);

    /**
     * 修改标签信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author  yunjin
     * @date    2024-3-23 12:31:52
     */
    boolean modify(LabelDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.yunjin.org.pojo.dto.LabelDTO
     * @author yunjin
     * @date   2024-3-23 12:31:52
     */
    LabelDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author yunjin
     * @date   2024-3-23 12:31:52
     */
    Paging<LabelVO> getPage(LabelPageParam param);

    List<LabelDTO> listByType(String typeId, Boolean isHidden);

    /**
     * 批量查询多个typeId下的标签
     * @param typeIds 标签类型ID列表
     * @param isHidden 是否包含隐藏标签
     * @return 标签列表
     */
    List<LabelDTO> listByTypeIds(List<String> typeIds, Boolean isHidden);

    List<LabelDTO> getByName(String name, Boolean isHidden, LabelTypeEnum labelType);

    List<LabelDTO> listByIdsIncludeDeleted(List<String> ids);

    LabelDTO  getByIdIncludeDeleted(String id);
}
