package com.yunjin.org.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.org.UserUtil;
import com.yunjin.org.convert.LabelTypeConvert;
import com.yunjin.org.enums.LabelTypeEnum;
import com.yunjin.org.manager.LabelTypeManager;
import com.yunjin.org.pojo.dto.LabelDTO;
import com.yunjin.org.pojo.dto.LabelTypeDTO;
import com.yunjin.org.pojo.entity.LabelType;
import com.yunjin.org.service.LabelService;
import com.yunjin.org.service.LabelTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 标签分类 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2024-3-22 15:07:09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LabelTypeServiceImpl implements LabelTypeService {

    private final LabelTypeManager labelTypeManager;

    private final LabelService labelService;

    @Override
    @Transactional
    public void addBbtLabelType(LabelTypeDTO dto) {
        // 如果传入的排序序号小于当前的最大序号，则需要进行批量排序更新
        // dto.setSort(modifySortForAdd(dto));
        dto.setCreateBy(UserUtil.getId());
        dto.setUpdateBy(UserUtil.getId());
        boolean success = labelTypeManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    private Integer modifySortForAdd(LabelTypeDTO dto) {
        int topNum = listByParentId(dto.getParentId(), false).size();
        if (dto.getSort() <= topNum) {
            addSort(dto.getParentId(), dto.getSort(), topNum);
            return dto.getSort();
        } else {
            return topNum + 1;
        }
    }

    private Integer modifySortForUpdate(LabelTypeDTO dto) {
        int topNum = listByParentId(dto.getParentId(), Boolean.FALSE).size();
        Integer sortOld = labelTypeManager.getDtoById(dto.getId()).getSort();
        Integer sortTarget = dto.getSort();
        // 目标排序比之前大
        if (sortTarget > sortOld) {
            int min = Math.min(sortTarget, topNum);
            minusSort(dto.getParentId(), sortOld + 1, min);
            return min;
        } else {
            // 目标排序比之前小
            if (sortTarget < sortOld) {
                addSort(dto.getParentId(), sortTarget, sortOld - 1);
            }
            // 传入的排序号和之前的排序号一样
            return sortTarget;
        }
    }

    private void minusSort(String parentId, int lowIndex, int highIndex) {
        List<LabelTypeDTO> labelTypeDTOS = labelTypeManager.listByParentId(parentId, Boolean.FALSE);
        for (int i = lowIndex; i <= highIndex; i++) {
            LabelTypeDTO labelTypeDTO = labelTypeDTOS.get(i - 1);
            labelTypeDTO.setSort(labelTypeDTO.getSort() - 1);
            labelTypeManager.modify(labelTypeDTO);
        }
    }

    @Override
    public List<LabelTypeDTO> listByParentId(String parentId, Boolean isHiddden) {
        return labelTypeManager.listByParentId(parentId, isHiddden);
    }

    // 将lowIndex和highIndex之前的排序号+1
    @Override
    public void addSort(String parentId, int lowIndex, int highIndex) {
        List<LabelTypeDTO> labelTypeDTOS = labelTypeManager.listByParentId(parentId, Boolean.FALSE);
        for (int i = lowIndex; i <= highIndex; i++) {
            LabelTypeDTO labelTypeDTO = labelTypeDTOS.get(i - 1);
            labelTypeDTO.setSort(labelTypeDTO.getSort() + 1);
            labelTypeManager.modify(labelTypeDTO);
        }
    }

    @Override
    public Boolean isExist(LabelTypeDTO dto) {
        return labelTypeManager.isExist(dto);
    }

    @Override
    @Transactional
    public void deleteBbtLabelType(Set<String> ids) {
        for (String id : ids) {
            LabelTypeDTO dtoById = labelTypeManager.getDtoById(id);
            labelTypeManager.delete(id);
            // 删除子分类
            listByParentId(id, Boolean.TRUE).stream().map(LabelTypeDTO::getId).collect(Collectors.toList()).forEach(labelTypeManager::delete);
            // 删除成功的后续业务
            // modifySortForDel(dtoById.getParentId(), dtoById.getSort());
            // 删除类型下的标签 toDO 类型需要有显隐
            List<LabelDTO> labelDTOS = labelService.listByType(id, Boolean.TRUE);
            labelService.deleteLabel(labelDTOS.stream().map(LabelDTO::getId).collect(Collectors.toSet()));
        }
    }

    private void modifySortForDel(String prentId, Integer sortDel) {
        labelTypeManager.modifySortForDel(prentId, sortDel);
    }

    @Override
    @Transactional
    public void updateBbtLabelType(LabelTypeDTO dto, Boolean isToHidden, Boolean isToOpen) {
        if (!labelTypeManager.getDtoById(dto.getId()).getName().equals(dto.getName())) {
            if (isExist(dto)) {
                throw new ServiceException("标签类型已存在");
            }
        }
        if (!labelTypeManager.getDtoById(dto.getId()).getParentId().equals(dto.getParentId())) {
            // 顶级类型不允许移动
            if ("0".equals(labelTypeManager.getDtoById(dto.getId()).getParentId())
                    || dto.getParentId().equals(dto.getId()) ||
                    !"0".equals(labelTypeManager.getDtoById(dto.getParentId()).getParentId())) {
                throw new ServiceException("标签类型移动不合理");
            }
        }
//        if (isToHidden) {
//            Integer sortOld = labelTypeManager.getDtoById(dto.getId()).getSort();
//            modifySortForDel(labelTypeManager.getDtoById(dto.getId()).getParentId(), sortOld);
//        } else if (isToOpen) {
//            dto.setSort(modifySortForAdd(dto));
//        } else {
//            dto.setSort(modifySortForUpdate(dto));
//        }
        dto.setUpdateBy(UserUtil.getId());
        labelTypeManager.modify(dto);
//        if (isToHidden) {
//            // 隐藏孩子
//            List<LabelTypeDTO> childrenTypeDTOS = labelTypeManager.listByParentId(dto.getId(), Boolean.TRUE);
//            childrenTypeDTOS.forEach(labelTypeDTO -> {
//                labelTypeDTO.setHidden(Boolean.TRUE);
//                updateBbtLabelType(labelTypeDTO, true, false);
//            });
//        }
//        if (isToOpen) {
//            // 打开孩子
//            List<LabelTypeDTO> childrenTypeDTOS = labelTypeManager.listByParentId(dto.getId(), Boolean.TRUE);
//            childrenTypeDTOS.forEach(labelTypeDTO -> {
//                labelTypeDTO.setHidden(Boolean.FALSE);
//                updateBbtLabelType(labelTypeDTO, false, true);
//            });
//        }
    }

    @Override
    public List<Tree<String>> getTree(String labelName, String typeHidden, String labelHidden, LabelTypeEnum labelType) {
        List<TreeNode<String>> nodeList = getTreeNodeList(labelName, typeHidden, labelHidden, labelType);
        if (CollUtil.isEmpty(nodeList)) {
            return null;
        }
        return TreeUtil.build(nodeList, "0")
                .stream().filter(tree -> CollUtil.isNotEmpty(tree.getChildren())).collect(Collectors.toList());
    }


    @Override
    public List<Tree<String>> getTreeByType(String typeName, String typeHidden, String labelHidden, LabelTypeEnum labelType) {
        List<TreeNode<String>> nodeList = getTreeNodeListByType(typeName, typeHidden, labelHidden, labelType);
        if (CollUtil.isEmpty(nodeList)) {
            return null;
        }
        return TreeUtil.build(nodeList, "0")
                .stream().filter(tree -> CollUtil.isNotEmpty(tree.getChildren())).collect(Collectors.toList());
    }

    private List<TreeNode<String>> getTreeNodeListByType(String typeName, String typeHidden, String labelHidden, LabelTypeEnum labelTypeEnum) {
        List<LabelType> labelTypes = new ArrayList<>();
        // 这里的标签类型 按照二级标签名称过滤
        // 二级标签
        LambdaQueryWrapper<LabelType> query = new LambdaQueryWrapper<>();
        query.eq(LabelType::getLabelType, labelTypeEnum).ne(LabelType::getParentId, 0).like(LabelType::getName, typeName);
        if (LabelTypeEnum.CUSTOM == labelTypeEnum) {
            query.eq(LabelType::getCreateBy, UserUtil.getId());
        }
        List<LabelType> labelSecond = labelTypeManager.getBaseMapper().selectList(query);
        // 过滤掉没有标签的分类
        labelSecond = labelSecond.stream().filter(t -> labelService.hasLabelsByType(t.getId(), Boolean.FALSE)).collect(Collectors.toList());
        if (CollUtil.isEmpty(labelSecond)) {
            return null;
        }
        // 顶级标签
        List<LabelType> labelTop = labelTypeManager.getBaseMapper().selectList(
                new LambdaQueryWrapper<LabelType>()
                        .in(LabelType::getId, labelSecond.stream().map(LabelType::getParentId).collect(Collectors.toList())));
        if (CollUtil.isEmpty(labelTop)) {
            return null;
        }
        // 组装全部类型
        labelTypes.addAll(labelTop);
        labelTypes.addAll(labelSecond);
        return getTreeNodeListByTypes(labelTypes, typeHidden, labelHidden);
    }

    private List<TreeNode<String>> getTreeNodeListByTypes(List<LabelType> labelTypes, String typeHidden, String labelHidden) {
        if (CollUtil.isEmpty(labelTypes)) {
            return null;
        }
        if (!Boolean.parseBoolean(typeHidden)) {
            labelTypes = labelTypes.stream().filter(
                    t -> t.getHidden().equals(Boolean.FALSE)).collect(Collectors.toList());
        }
        if (CollUtil.isEmpty(labelTypes)) {
            return null;
        }

        // 批量查询所有typeId对应的labels
        List<String> typeIds = labelTypes.stream().map(LabelType::getId).collect(Collectors.toList());
        List<LabelDTO> allLabels = labelService.listByTypeIds(typeIds, Boolean.valueOf(labelHidden));
        // 按typeId分组
        Map<String, List<LabelDTO>> labelsByTypeId = allLabels.stream().collect(Collectors.groupingBy(LabelDTO::getTypeId));

        List<TreeNode<String>> nodeList = new ArrayList<>();
        for (LabelType labelType : labelTypes) {
            TreeNode<String> treeNode = new TreeNode<>();
            treeNode.setId(labelType.getId());
            treeNode.setParentId(labelType.getParentId());
            treeNode.setName(labelType.getName());
            treeNode.setWeight(labelType.getSort());
            // 添加标签的 显隐及排序
            Map<String, Object> extraListMap = new HashMap<>();
            extraListMap.put("sort", labelType.getSort());
            extraListMap.put("hidden", labelType.getHidden());
            extraListMap.put("createTime", labelType.getCreateTime());
            // 从批量查询的结果中获取对应的标签
            List<LabelDTO> labelDTOS = labelsByTypeId.getOrDefault(labelType.getId(), new ArrayList<>());
            // 方便前端获取，labels更名成为children
            extraListMap.put("children", labelDTOS);  // 需要说明这里的children是认为定义的 和建树代码名字相同 但是意义不同
            treeNode.setExtra(extraListMap);
            nodeList.add(treeNode);
        }
        return nodeList;
    }

    private List<TreeNode<String>> getTreeNodeList(String labelName, String typeHidden, String labelHidden, LabelTypeEnum labelTypeEnum) {
        List<TreeNode<String>> nodeList = new ArrayList<>();
        // 根据名称 选择出需要的labels
        List<LabelDTO> labelByNames = labelService.getByName(labelName, Boolean.valueOf(labelHidden), labelTypeEnum);
        if (CollUtil.isEmpty(labelByNames)) {
            return null;
        }
        Set<String> typeIdNeeds = labelByNames.stream().map(LabelDTO::getTypeId).collect(Collectors.toSet());

        Set<String> labelIdNeeds = labelByNames.stream().map(LabelDTO::getId).collect(Collectors.toSet());
        // 将需要显示的类型ID收集
        Set<String> needTypeIds = new HashSet<>();
        Set<String> topTypeIds = new HashSet<>(); //专门存放顶级
        Set<String> secondTypeIds = new HashSet<>(); //专门存放次级

        // 批量查询所有需要的LabelType
        List<LabelType> labelTypesNeed = getByIds(typeIdNeeds);

        for (LabelType labelTypeDTO : labelTypesNeed) {
            if (!"0".equals(labelTypeDTO.getParentId())) {
                Set<String> topTypes = labelTypeManager.listParentIds(Collections.singleton(labelTypeDTO.getId()));
                Iterator<String> iterator = topTypes.iterator();
                String topType = iterator.next();
                topTypeIds.add(topType);
            }
            secondTypeIds.add(labelTypeDTO.getId());
        }
        needTypeIds.addAll(topTypeIds);
        needTypeIds.addAll(secondTypeIds);

        List<LabelType> labelTypes = getByIds(needTypeIds);
        if (!Boolean.parseBoolean(typeHidden)) {
            labelTypes = labelTypes.stream().filter(
                    t -> t.getHidden().equals(Boolean.FALSE)).collect(Collectors.toList());
        }
        // 如果过滤后  topTypeIds为空，则返回空
        if (labelTypes.isEmpty()) {
            return null;
        }
        // 经过可视性过滤的
        List<String> labelTypesFilterIds = labelTypes.stream().map(LabelType::getId).collect(Collectors.toList());
        // 没有顶级了，则返回空
        if (topTypeIds.stream().noneMatch(labelTypesFilterIds::contains)) {
            return null;
        }

        // 批量查询所有typeId对应的labels
        List<LabelDTO> allLabels = labelService.listByTypeIds(labelTypesFilterIds, Boolean.valueOf(labelHidden));
        Map<String, List<LabelDTO>> labelsByTypeId = allLabels.stream()
                .collect(Collectors.groupingBy(LabelDTO::getTypeId));

        for (LabelType labelType : labelTypes) {
            TreeNode<String> treeNode = new TreeNode<>();
            treeNode.setId(labelType.getId());
            treeNode.setParentId(labelType.getParentId());
            treeNode.setName(labelType.getName());
            treeNode.setWeight(labelType.getSort());
            Map<String, Object> extraListMap = new HashMap<>();
            extraListMap.put("sort", labelType.getSort());
            extraListMap.put("hidden", labelType.getHidden());
            extraListMap.put("createTime", labelType.getCreateTime());
            // 从批量查询的结果中获取对应的标签
            List<LabelDTO> labelDTOS = labelsByTypeId.getOrDefault(labelType.getId(), new ArrayList<>());
            List<LabelDTO> labelFilterDTOS = labelDTOS.stream()
                    .filter(t -> labelIdNeeds.contains(t.getId())).collect(Collectors.toList());
            extraListMap.put("children", labelFilterDTOS);
            treeNode.setExtra(extraListMap);
            nodeList.add(treeNode);
        }
        return nodeList;
    }

    @Override
    public List<LabelType> getByIds(Set<String> ids) {
        return labelTypeManager.getBaseMapper().selectBatchIds(ids);
    }

    @Override
    public Boolean isToOpen(String id, Boolean hidden) {
        return labelTypeManager.getDtoById(id).getHidden().equals(Boolean.TRUE) && hidden.equals(Boolean.FALSE);
    }

    @Override
    public Boolean isToHidden(String id, Boolean hidden) {
        return labelTypeManager.getDtoById(id).getHidden().equals(Boolean.FALSE) && hidden.equals(Boolean.TRUE);
    }

    @Override
    public List<Tree<String>> getTree(String typeHidden, String labelHidden, LabelTypeEnum labelType) {
        List<TreeNode<String>> nodeList = getTreeNodeList(typeHidden, labelHidden, labelType);
        if (CollUtil.isEmpty(nodeList)) {
            return null;
        }
        return TreeUtil.build(nodeList, "0");
    }

    @Override
    public List<Tree<String>> treeList(String name, LabelTypeEnum labelType) {
        List<TreeNode<String>> nodeList = getTreeNodeTypeList(name, labelType);
        if (CollUtil.isNotEmpty(nodeList)) {
            return TreeUtil.build(nodeList, "-1");
        }
        return null;
    }

    private List<TreeNode<String>> getTreeNodeTypeList(String name, LabelTypeEnum labelTypeEnum) {
        // 需要一个顶级节点
        TreeNode<String> topNode = new TreeNode<>();
        topNode.setId("0");
        topNode.setParentId("-1");
        topNode.setName("顶级");

        List<LabelTypeDTO> labelTypes = LabelTypeConvert.INSTANCE.toDTO(labelTypeManager.listByNameAndType(null, labelTypeEnum));
        if (CollUtil.isEmpty(labelTypes)) {
            return null;
        }
        List<TreeNode<String>> nodeList = new ArrayList<>();
        // 如果存在name,先精简数据
        if (StrUtil.isNotBlank(name)) {
            // 首先包含name的类别需要留下 - 包括了顶级和次级
            List<LabelTypeDTO> labelTypesTmp =
                    labelTypes.stream().filter(t -> t.getName().contains(name)).collect(Collectors.toList());
            labelTypes.clear();
            // 次级中包含name,但是顶级不包含name的类别需要留下
            labelTypesTmp.stream().forEach(t -> {
                if (!"0".equals(t.getParentId())) {
                    labelTypes.add(labelTypeManager.getDtoById(t.getParentId()));
                }
                labelTypes.add(t);
            });
        }
        for (LabelTypeDTO labelTypeDTO : labelTypes) {
            TreeNode<String> treeNode = new TreeNode<>();
            treeNode.setId(labelTypeDTO.getId());
            treeNode.setParentId(labelTypeDTO.getParentId());
            treeNode.setName(labelTypeDTO.getName());
            treeNode.setWeight(labelTypeDTO.getSort());
            // 添加标签的 显隐及排序
            Map<String, Object> extraListMap = new HashMap<>();
            extraListMap.put("sort", labelTypeDTO.getSort());
            extraListMap.put("hidden", labelTypeDTO.getHidden());
            extraListMap.put("createTime", labelTypeDTO.getCreateTime());
            treeNode.setExtra(extraListMap);
            nodeList.add(treeNode);
        }
        nodeList.add(topNode);
        return nodeList;
    }

    private List<LabelTypeDTO> listByName(String name) {
        return labelTypeManager.listByName(name);
    }

    // 获取全部树节点
    private List<TreeNode<String>> getTreeNodeList(String typeHidden, String labelHidden, LabelTypeEnum labelType) {
        List<LabelType> labelTypes = labelTypeManager.listByNameAndType(null, labelType);
        return getTreeNodeListByTypes(labelTypes, typeHidden, labelHidden);
    }

}
