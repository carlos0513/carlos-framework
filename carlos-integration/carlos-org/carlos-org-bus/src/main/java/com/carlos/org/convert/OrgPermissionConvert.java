package com.carlos.org.convert;


import com.carlos.org.pojo.dto.OrgPermissionDTO;
import com.carlos.org.pojo.entity.OrgPermission;
import com.carlos.org.pojo.param.OrgPermissionCreateParam;
import com.carlos.org.pojo.param.OrgPermissionUpdateParam;
import com.carlos.org.pojo.vo.OrgPermissionVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * 权限 mapstruct
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Mapper(componentModel = "spring")
public interface OrgPermissionConvert {

    OrgPermissionConvert INSTANCE = Mappers.getMapper(OrgPermissionConvert.class);

    // ==================== Entity ↔ DTO ====================

    OrgPermission toDO(OrgPermissionDTO dto);

    OrgPermissionDTO toDTO(OrgPermission entity);

    // ==================== Param ↔ DTO ====================

    OrgPermissionDTO toDTO(OrgPermissionCreateParam param);

    OrgPermissionDTO toDTO(OrgPermissionUpdateParam param);

    // ==================== DTO ↔ VO ====================

    @Mappings({
            @Mapping(target = "permType", expression = "java(dto.getPermType() != null ? dto.getPermType().getCode() : null)"),
            @Mapping(target = "permTypeName", expression = "java(dto.getPermType() != null ? dto.getPermType().getDesc() : null)"),
            @Mapping(target = "state", expression = "java(dto.getState() != null ? dto.getState().getCode() : null)"),
            @Mapping(target = "stateName", expression = "java(dto.getState() != null ? dto.getState().getDesc() : null)"),
            @Mapping(target = "children", ignore = true)
    })
    OrgPermissionVO toVO(OrgPermissionDTO dto);

    /**
     * 构建权限树
     */
    default List<OrgPermissionVO> buildTreeVOList(List<OrgPermissionDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>();
        }

        // 转换为VO
        List<OrgPermissionVO> voList = dtoList.stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        // 按父ID分组
        Map<Long, List<OrgPermissionVO>> parentMap = voList.stream()
                .collect(Collectors.groupingBy(vo -> vo.getParentId() != null ? vo.getParentId() : 0L));

        // 构建树形结构
        return buildTree(parentMap, 0L);
    }

    /**
     * 递归构建树
     */
    default List<OrgPermissionVO> buildTree(Map<Long, List<OrgPermissionVO>> parentMap, Long parentId) {
        List<OrgPermissionVO> children = parentMap.get(parentId);
        if (children == null) {
            return new ArrayList<>();
        }

        // 按排序排序
        children.sort(Comparator.comparing(OrgPermissionVO::getSort, Comparator.nullsLast(Comparator.naturalOrder())));

        // 递归设置子节点
        for (OrgPermissionVO child : children) {
            child.setChildren(buildTree(parentMap, child.getId()));
        }

        return children;
    }

}
