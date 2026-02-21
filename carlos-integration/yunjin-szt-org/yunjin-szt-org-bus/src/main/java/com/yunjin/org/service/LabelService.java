package com.yunjin.org.service;

import com.yunjin.org.enums.LabelTypeEnum;
import com.yunjin.org.pojo.dto.LabelDTO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 标签 业务接口
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-23 12:31:52
 */
public interface LabelService{

    Boolean hasLabelsByType(String typeId,Boolean isHidden);

    LabelDTO getById(String id);

    /**
     * 新增标签
     *
     * @param dto 标签数据
     * @author  yunjin
     * @date    2024-3-23 12:31:52
     */
    String addLabel(LabelDTO dto,Boolean isTemp);

    void exist(String name, LabelTypeEnum labelType);

    /**
     * 删除标签
     *
     * @param ids 标签id
     * @author  yunjin
     * @date    2024-3-23 12:31:52
     */
    void deleteLabel(Set<String> ids);

    /**
     * 修改标签信息
     *
     * @param dto 对象信息
     * @author  yunjin
     * @date    2024-3-23 12:31:52
     */
    void updateLabel(LabelDTO dto,Boolean isToHidden,Boolean isToOpen);

    List<LabelDTO> listByType(String typeId,Boolean isHidden);

    /**
     * 批量查询多个typeId下的标签
     * @param typeIds 标签类型ID列表
     * @param isHidden 是否包含隐藏标签
     * @return 标签列表
     */
    List<LabelDTO> listByTypeIds(List<String> typeIds, Boolean isHidden);

    List<LabelDTO> getByName(String name, Boolean isHidden, LabelTypeEnum labelType);

    List<LabelDTO> getByIds(String ids);

    Boolean isToHidden(String id, Boolean hidden);

    Boolean isToOpen(String id, Boolean hidden);
}
