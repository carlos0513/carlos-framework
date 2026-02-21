package com.yunjin.org.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.org.UserUtil;
import com.yunjin.org.convert.LabelConvert;
import com.yunjin.org.enums.LabelTypeEnum;
import com.yunjin.org.manager.LabelManager;
import com.yunjin.org.pojo.dto.LabelDTO;
import com.yunjin.org.pojo.entity.Label;
import com.yunjin.org.service.LabelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 标签 业务接口实现类
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-23 12:31:52
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelManager labelManager;

    @Override
    public Boolean hasLabelsByType(String typeId, Boolean isHidden) {
        LambdaQueryWrapper<Label> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(Label::getTypeId,typeId);
        if(!isHidden){
            queryWrapper.eq(Label::getHidden,isHidden);
        }
        return (long) labelManager.list(queryWrapper).size() >0L;
    }

    @Override
    public LabelDTO getById(String id) {
        return LabelConvert.INSTANCE.toDTO(labelManager.getById(id));
    }

    @Override
    @Transactional
    public String addLabel(LabelDTO dto,Boolean isTemp){
        if(isTemp==Boolean.FALSE){
            exist(dto.getName(), dto.getLabelType());
        }
        //dto.setSort(modifySortForAdd(dto));
        String userId = UserUtil.getId();
        dto.setCreateBy(userId);
        dto.setUpdateBy(userId);
        boolean success = labelManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return "";
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
        return id.toString();
    }

    private Integer modifySortForAdd(LabelDTO dto) {
        int topNum = listByType(dto.getTypeId(),Boolean.FALSE).size();
        int targetSort = dto.getSort();
        if (targetSort<=topNum) {
            addSort(dto.getTypeId(), targetSort,topNum);
            return targetSort;
        }else {
            return topNum+1;
        }
    }

    public void addSort(String typeId, int lowIndex, int highIndex) {
        List<LabelDTO> labelDTOS = listByType(typeId,Boolean.FALSE);
        for (int i = lowIndex; i <= highIndex; i++) {
            LabelDTO labelDTO = labelDTOS.get(i-1);
            labelDTO.setSort(labelDTO.getSort()+1);
            labelManager.modify(labelDTO);
        }
    }

    @Override
    public List<LabelDTO> getByName(String name, Boolean isHidden, LabelTypeEnum labelType) {
        return labelManager.getByName(name, isHidden, labelType);
    }


    @Override
    public Boolean isToOpen(String id, Boolean hidden) {
        return labelManager.getDtoById(id).getHidden().equals(Boolean.TRUE) && hidden.equals(Boolean.FALSE);
    }

    @Override
    public Boolean isToHidden(String id,Boolean hidden) {
        return labelManager.getDtoById(id).getHidden().equals(Boolean.FALSE) && hidden.equals(Boolean.TRUE);
    }

    @Override
    public List<LabelDTO> getByIds(String ids) {
        if(ids==null)
            return null;
        if(!ids.contains(",")){
            return Collections.singletonList(labelManager.getByIdIncludeDeleted(ids));
        }else{
            String[] split = ids.split(",");
            return labelManager.listByIdsIncludeDeleted(Arrays.asList(split));
        }
    }

    @Override
    public List<LabelDTO> listByType(String typeId, Boolean isHidden) {
        return labelManager.listByType(typeId, isHidden);
    }

    @Override
    public List<LabelDTO> listByTypeIds(List<String> typeIds, Boolean isHidden) {
        return labelManager.listByTypeIds(typeIds, isHidden);
    }

    @Override
    public void exist(String name, LabelTypeEnum labelType) {
        Long count = labelManager.lambdaQuery().eq(Label::getName, name).eq(Label::getLabelType, labelType.getCode()).count();
        if (count > 0L) {
            throw new ServiceException("该标签已存在");
        }
    }

    @Override
    @Transactional
    public void deleteLabel(Set<String> ids){
        for (Serializable id : ids) {
           // modifySortForDel(id);
            boolean success = labelManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    // 删除前 需要更新整个系列的SORT
    private void modifySortForDel(Serializable id) {
        LabelDTO dtoById = labelManager.getDtoById(id);
        Integer sortOld = dtoById.getSort();
        List<LabelDTO> labelDTOS = listByType(dtoById.getTypeId(),Boolean.FALSE);
        List<LabelDTO> filteredList = labelDTOS.stream().filter(t -> t.getSort() > sortOld).collect(Collectors.toList());
        if (!filteredList.isEmpty()) {
            // 批量更新排序
            filteredList.forEach(t -> t.setSort(t.getSort() - 1));
            labelManager.updateBatchById(LabelConvert.INSTANCE.toDO(filteredList));
        }
    }

    @Override
    @Transactional
    public void updateLabel(LabelDTO dto,Boolean isToHidden,Boolean isToOpen){
//        String idOld = dto.getId();
//        LabelDTO dtoOld = labelManager.getDtoById(idOld);
//        String typeOld =  dtoOld.getTypeId();
//        if (isToHidden) {
//            modifySortForDel(idOld);
//        }
//        else if(isToOpen || !typeOld.equals(dto.getTypeId())){
//            modifySortForDel(idOld);
//            String id = addLabel(dto.setId(null), true);
//            copyLabelAttributes(idOld, id);
//        }else {
//            dto.setSort(modifySortForUpdate(dto));
//        }

        // 标签名称过长提示
        if (StrUtil.length(dto.getName()) == 0 || StrUtil.length(dto.getName()) > 100) {
            throw new ServiceException("标签名称过长，请重新输入");
        }

        // 判断名字是否有冲突
        if (!labelManager.getById(dto.getId()).getName().equals(dto.getName())) {
            exist(dto.getName(), dto.getLabelType());
        }
        dto.setUpdateBy(UserUtil.getId());
        labelManager.modify(dto);
    }
    private void copyLabelAttributes(String idOld, String idNew) {
        LabelDTO dtoById = labelManager.getDtoById(idNew);
        LabelDTO dtoByIdOld = labelManager.getDtoById(idOld)
                .setTypeId(dtoById.getTypeId())
                .setSort(dtoById.getSort())
                .setName(dtoById.getName())
                .setUpdateTime(dtoById.getCreateTime())
                .setHidden(dtoById.getHidden())
                .setUpdateBy(UserUtil.getId());
        labelManager.modify(dtoByIdOld);
        labelManager.delete(idNew);
    }


    private Integer modifySortForUpdate(LabelDTO dto) {
        int topNum = listByType(dto.getTypeId(),Boolean.FALSE).size();
        if(topNum==0){
            return 1; // 说明新的类型下没有标签，初始数字是1
        }
        Integer sortOld = labelManager.getDtoById(dto.getId()).getSort();
        Integer sortTarget = dto.getSort();
        // 目标排序比之前大
        if (sortTarget > sortOld) {
            int min = Math.min(sortTarget, topNum);
            minusSort(dto.getTypeId(), sortOld + 1, min);
            return min;
        } else {
            // 目标排序比之前小
            if (sortTarget < sortOld) {
                addSort(dto.getTypeId(), sortTarget, sortOld - 1);
            }
            // 传入的排序号和之前的排序号一样
            return sortTarget;
        }
    }

    private void minusSort(String typeId, int lowIndex, int highIndex) {
        List<LabelDTO> labelDTOS = listByType(typeId,Boolean.FALSE);
        for (int i = lowIndex; i <= highIndex; i++) {
            LabelDTO labelDTO = labelDTOS.get(i-1);
            labelDTO.setSort(labelDTO.getSort()-1);
            labelManager.modify(labelDTO);
        }
    }

}
