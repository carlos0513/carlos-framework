package com.carlos.org.service;


import cn.hutool.core.util.StrUtil;
import com.carlos.org.exception.*;
import com.carlos.org.manager.OrgPermissionManager;
import com.carlos.org.pojo.dto.OrgPermissionDTO;
import com.carlos.org.pojo.enums.OrgPermissionStateEnum;
import com.carlos.org.pojo.enums.OrgPermissionTypeEnum;
import com.carlos.org.pojo.param.OrgPermissionSortParam;
import com.carlos.org.pojo.param.OrgPermissionSyncApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 权限 业务
 * </p>
 * <p>实现PM001-PM006所有权限管理需求</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgPermissionService {

    private final OrgPermissionManager permissionManager;


    /**
     * PM-001 获取权限树
     *
     * @return 权限树列表
     */
    public List<OrgPermissionDTO> getPermissionTree() {
        // 获取所有权限
        List<OrgPermissionDTO> allPermissions = permissionManager.listAll();

        // 构建树形结构在Convert中处理
        return allPermissions;
    }


    /**
     * PM-002 新增权限
     *
     * @param dto 权限数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void addPermission(OrgPermissionDTO dto) {
        // 校验权限编码唯一性
        if (StrUtil.isNotBlank(dto.getPermCode())) {
            OrgPermissionDTO existPerm = permissionManager.getByPermCode(dto.getPermCode());
            if (existPerm != null) {
                throw new PermissionCodeExistsException(dto.getPermCode());
            }
        }

        // 设置默认父ID
        if (dto.getParentId() == null) {
            dto.setParentId(0L);
        }

        // 设置默认状态为启用
        if (dto.getState() == null) {
            dto.setState(OrgPermissionStateEnum.ENABLE);
        }

        // 设置默认排序
        if (dto.getSort() == null) {
            dto.setSort(0);
        }

        boolean success = permissionManager.add(dto);
        if (!success) {
            throw new OrgModuleException("新增权限失败");
        }
        log.info("PM-002 新增权限成功：id={}, permName={}, permCode={}",
                dto.getId(), dto.getPermName(), dto.getPermCode());
    }


    /**
     * PM-003 编辑权限
     *
     * @param dto 权限数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(OrgPermissionDTO dto) {
        // 校验权限是否存在
        OrgPermissionDTO existPerm = permissionManager.getDtoById(dto.getId());
        if (existPerm == null) {
            throw new PermissionNotFoundException(String.valueOf(dto.getId()));
        }

        boolean success = permissionManager.modify(dto);
        if (!success) {
            throw new OrgModuleException("修改权限失败");
        }
        log.info("PM-003 修改权限成功：id={}", dto.getId());
    }


    /**
     * PM-004 删除权限
     * 删除前校验：1. 无子权限 2. 未被角色使用
     *
     * @param ids 权限id集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Set<Serializable> ids) {
        for (Serializable id : ids) {
            // 校验权限是否存在
            OrgPermissionDTO perm = permissionManager.getDtoById(id);
            if (perm == null) {
                throw new PermissionNotFoundException(String.valueOf(id));
            }

            // 校验是否有子权限
            List<OrgPermissionDTO> children = permissionManager.getChildrenByParentId(id);
            if (!children.isEmpty()) {
                throw new PermissionHasChildrenException(String.valueOf(id));
            }

            // 校验是否被角色使用
            int useCount = permissionManager.getRoleUseCount(id);
            if (useCount > 0) {
                throw new PermissionInUseException(String.valueOf(id));
            }

            // 逻辑删除
            boolean success = permissionManager.removeById(id);
            if (!success) {
                log.warn("删除权限失败：id={}", id);
            }
        }
        log.info("PM-004 删除权限成功：ids={}", ids);
    }


    /**
     * PM-005 权限排序
     *
     * @param param 排序参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void sortPermission(OrgPermissionSortParam param) {
        // 校验权限是否存在
        OrgPermissionDTO perm = permissionManager.getDtoById(param.getId());
        if (perm == null) {
            throw new PermissionNotFoundException(String.valueOf(param.getId()));
        }

        perm.setSort(param.getSort());
        boolean success = permissionManager.modify(perm);
        if (!success) {
            throw new OrgModuleException("权限排序失败");
        }
        log.info("PM-005 权限排序成功：id={}, sort={}", param.getId(), param.getSort());
    }


    /**
     * PM-006 同步API权限
     * 从代码注解自动同步API权限
     *
     * @param param API权限参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncApiPermission(OrgPermissionSyncApiParam param) {
        // 生成权限编码
        String permCode = generateApiPermCode(param.getApiPath(), param.getMethod());

        // 检查是否已存在
        OrgPermissionDTO existPerm = permissionManager.getByPermCode(permCode);
        if (existPerm != null) {
            log.info("API权限已存在，跳过同步：permCode={}", permCode);
            return;
        }

        // 创建API权限
        OrgPermissionDTO dto = new OrgPermissionDTO();
        dto.setPermName(param.getDescription() != null ? param.getDescription() : permCode);
        dto.setPermCode(permCode);
        dto.setPermType(OrgPermissionTypeEnum.API);
        dto.setResourceUrl(param.getApiPath());
        dto.setMethod(param.getMethod());
        dto.setParentId(param.getParentId() != null ? param.getParentId() : 0L);
        dto.setState(OrgPermissionStateEnum.ENABLE);
        dto.setSort(0);
        dto.setDescription(param.getDescription());

        boolean success = permissionManager.add(dto);
        if (!success) {
            throw new OrgModuleException("同步API权限失败");
        }
        log.info("PM-006 同步API权限成功：id={}, permCode={}", dto.getId(), permCode);
    }


    /**
     * 生成API权限编码
     * 格式：api:{method}:{path}
     * 例如：api:POST:org:user:add
     */
    private String generateApiPermCode(String apiPath, String method) {
        // 移除前导斜杠
        String path = apiPath.startsWith("/") ? apiPath.substring(1) : apiPath;
        // 替换斜杠为冒号
        path = path.replace("/", ":");
        return "api:" + method.toLowerCase() + ":" + path;
    }


    /**
     * 启用/禁用权限
     *
     * @param id    权限ID
     * @param state 状态：0禁用，1启用
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeState(Serializable id, Integer state) {
        if (state == null || (state != 0 && state != 1)) {
            throw new OrgModuleException("状态值不正确，只能是0（禁用）或1（启用）");
        }

        OrgPermissionDTO perm = permissionManager.getDtoById(id);
        if (perm == null) {
            throw new PermissionNotFoundException(String.valueOf(id));
        }

        perm.setState(state == 1 ? OrgPermissionStateEnum.ENABLE : OrgPermissionStateEnum.DISABLE);
        boolean success = permissionManager.modify(perm);
        if (!success) {
            throw new OrgModuleException("修改权限状态失败");
        }
        log.info("修改权限状态成功：id={}, state={}", id, state);
    }

}
