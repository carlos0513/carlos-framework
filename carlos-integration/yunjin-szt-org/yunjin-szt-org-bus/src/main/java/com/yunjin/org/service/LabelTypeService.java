package com.yunjin.org.service;



import cn.hutool.core.lang.tree.Tree;
import com.yunjin.org.enums.LabelTypeEnum;
import com.yunjin.org.pojo.dto.LabelTypeDTO;
import com.yunjin.org.pojo.entity.LabelType;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 标签分类 业务接口
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-22 15:07:09
 */
public interface LabelTypeService {

    /**
     * 新增标签分类
     *
     * @param dto 标签分类数据
     * @author  yunjin
     * @date    2024-3-22 15:07:09
     */
    void addBbtLabelType(LabelTypeDTO dto);

    List<LabelTypeDTO> listByParentId(String parentId,Boolean isHidden);


    void addSort(String parentId, int lowIndex, int highIndex);

    /**
     * 判断是否存在
     */
    Boolean isExist(LabelTypeDTO dto);

    /**
     * 删除标签分类
     *
     * @param ids 标签分类id
     * @author  yunjin
     * @date    2024-3-22 15:07:09
     */
    void deleteBbtLabelType(Set<String> ids);

    /**
     * 修改标签分类信息
     *
     * @param dto 对象信息
     * @author yunjin
     * @date 2024-3-22 15:07:09
     */
    void updateBbtLabelType(LabelTypeDTO dto, Boolean isToHidden, Boolean isToOpen);

    List<Tree<String>> getTree(String labelName, String typeHidden, String labelHidden, LabelTypeEnum labelType);

    List<Tree<String>> getTree(String typeHidden, String labelHidden, LabelTypeEnum labelType);

    List<LabelType> getByIds(Set<String> ids);

    Boolean isToHidden(String id, Boolean hidden);

    Boolean isToOpen(String id, Boolean hidden);

    List<Tree<String>> treeList(String name, LabelTypeEnum labelType);

    List<Tree<String>> getTreeByType(String typeName, String typeHidden, String labelHidden, LabelTypeEnum labelType);
}
