package com.yunjin.org.manager;

import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseService;
import com.yunjin.org.enums.LabelTypeEnum;
import com.yunjin.org.pojo.dto.LabelTypeDTO;
import com.yunjin.org.pojo.entity.LabelType;
import com.yunjin.org.pojo.param.LabelTypePageParam;
import com.yunjin.org.pojo.vo.LabelTypeVO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 标签分类 查询封装接口
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-22 15:07:09
 */
public interface LabelTypeManager extends BaseService<LabelType>{

    /**
     * 新增标签分类
     *
     * @param dto 标签分类数据
     * @return boolean
     * @author  yunjin
     * @date    2024-3-22 15:07:09
     */
    boolean add(LabelTypeDTO dto);

    /**
     * 删除标签分类
     *
     * @param id 标签分类id
     * @return boolean
     * @author  yunjin
     * @date    2024-3-22 15:07:09
     */
    boolean delete(Serializable id);

    /**
     * 修改标签分类信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author  yunjin
     * @date    2024-3-22 15:07:09
     */
    boolean modify(LabelTypeDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.yunjin.org.pojo.dto.BbtLabelTypeDTO
     * @author yunjin
     * @date   2024-3-22 15:07:09
     */
    LabelTypeDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author yunjin
     * @date   2024-3-22 15:07:09
     */
    Paging<LabelTypeVO> getPage(LabelTypePageParam param);

    Boolean isExist(LabelTypeDTO dto);

    List<LabelTypeDTO> listByParentId(String parentId, Boolean isHidden);

    void modifySortForDel(String parentId, Integer sortDel);

    // 获取父ID
    Set<String> listParentIds(Set<String> ids);

    List<LabelTypeDTO> listByName(String name);

    List<LabelType> listByNameAndType(String name, LabelTypeEnum labelType);
}
