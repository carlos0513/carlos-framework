package com.yunjin.board.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.yunjin.board.manager.BoardCustomConfigManager;
import com.yunjin.board.pojo.dto.BoardCardInfoDTO;
import com.yunjin.board.pojo.dto.BoardCustomConfigDTO;
import com.yunjin.board.pojo.dto.BoardCustomConfigDetailDTO;
import com.yunjin.board.pojo.enums.CustomConfigType;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.org.UserUtil;
import com.yunjin.org.pojo.ao.UserLoginAO;
import com.yunjin.org.pojo.dto.RoleResourceGroupRefDTO;
import com.yunjin.org.pojo.enums.DepartmentTypeEnum;
import com.yunjin.org.service.RoleResourceGroupRefService;
import com.yunjin.system.api.ApiFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 看板自定义配置 业务
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BoardCustomConfigService {

    private final BoardCustomConfigManager customConfigManager;
    private final RoleResourceGroupRefService roleResourceGroupRefService;
    private final BoardCardInfoService boardCardInfoService;


    public void changeConfig(List<BoardCustomConfigDetailDTO> items) {
        String userId = UserUtil.getId();
        Set<String> roleIds = UserUtil.getRoleId();
        String roleId = roleIds.stream().findFirst().orElse(null);

        String key = userId + StrUtil.COLON + roleId;
        BoardCustomConfigDTO config = customConfigManager.getConfig(key, CustomConfigType.user_role);
        if (config == null) {
            // 新增配置
            config = new BoardCustomConfigDTO();
            config.setConfigKey(key);
            config.setConfigJson(JSONUtil.toJsonStr(items));
            config.setConfigType(CustomConfigType.user_role);
            customConfigManager.add(config);
        } else {
            config.setConfigJson(JSONUtil.toJsonStr(items));
            customConfigManager.modify(config);
        }
    }


    public List<BoardCustomConfigDetailDTO> getUserConfig(String userId, String roleId) {
        BoardCustomConfigDTO config = null;
        if (StrUtil.isNotBlank(userId) && StrUtil.isBlank(roleId)) {
            config = customConfigManager.getConfig(userId, CustomConfigType.user);
        }
        if (StrUtil.isBlank(userId) && StrUtil.isNotBlank(roleId)) {
            config = customConfigManager.getConfig(roleId, CustomConfigType.role);
        }
        if (StrUtil.isNotBlank(userId) && StrUtil.isNotBlank(roleId)) {
            config = customConfigManager.getConfig(userId + StrUtil.COLON + roleId, CustomConfigType.user_role);
        }

        if (config == null) {
            // 读取角色权限组默认配置
            RoleResourceGroupRefDTO ref = roleResourceGroupRefService.getByRoleId(roleId);
            if (ref == null) {
                throw new ServiceException("该用户角色未配置权限组，请检查配置！");
            }
            config = customConfigManager.getConfig(ref.getResourceGroupId(), CustomConfigType.role_group);
        }
        if (config == null) {
            throw new ServiceException("未查询到看板配置，请检查配置！");
        }
        String configJson = config.getConfigJson();
        if (StrUtil.isBlank(configJson)) {
            throw new ServiceException("配置内容解析失败，请检查配置！");
        }
        List<BoardCustomConfigDetailDTO> list = JSONUtil.toList(configJson, BoardCustomConfigDetailDTO.class);
        List<BoardCardInfoDTO> cards = boardCardInfoService.getAllCard();
        Map<String, BoardCardInfoDTO> map = cards.stream().collect(Collectors.toMap(BoardCardInfoDTO::getComponent, i -> i));

        // 个性化配置card display或者displayForMobile 开启或者关闭
        UserLoginAO.Department department = UserUtil.getDepartment();
        String departmentType = department.getDepartmentType();
        // 检查是否为市级部门
        boolean isCityDepartment = false;

        try {
            DepartmentTypeEnum deptEnum = DepartmentTypeEnum.codeOf(departmentType);
            // 判断是否为市级部门（CITY_01或CITY_02）
            isCityDepartment = deptEnum == DepartmentTypeEnum.CITY_01 || deptEnum == DepartmentTypeEnum.CITY_02;
        } catch (ServiceException e) {
            log.warn("未找到对应的部门类型枚举: {}", departmentType);
        }

        for (BoardCustomConfigDetailDTO item : list) {
            String component = item.getComponent();
            BoardCardInfoDTO card = map.get(component);
            if (card == null) {
                log.warn("未匹配到组件信息：{}", component);
                continue;
            }
            item.setCardName(card.getCardName());
            item.setDescription(card.getDescription());
            item.setThumbnail(card.getThumbnail());

            // 如果不是市级部门，则隐藏ReportsNumber卡片
            if ( !isCityDepartment && "ReportsNumber".equals(component) ) {
                item.setDisplay(false);
                item.setDisplayForMobile(false);
            }


        }
        return list;
    }

}
