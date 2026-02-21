package com.yunjin.board.data.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.google.common.collect.Lists;
import com.yunjin.board.data.query.BoardCommonApplicationsDataQuery;
import com.yunjin.board.data.result.BoardCommonApplicationsDataResult;
import com.yunjin.board.pojo.enums.FrontFixedMenuEnum;
import com.yunjin.core.response.Result;
import com.yunjin.org.UserUtil;
import com.yunjin.org.pojo.dto.OrgUserMenuDTO;
import com.yunjin.org.service.OrgUserMenuService;
import com.yunjin.system.api.ApiMenu;
import com.yunjin.system.pojo.ao.MenuAO;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 展示当前用户收藏过的菜单，点击直接跳转至对应的菜单页面（角色是准入审核员时，卡片放在首页右边）
 * </p>
 *
 * @author Carlos
 * @date 2025-05-20 23:32
 */
@Slf4j
public class BoardCommonApplicationsDataHandler extends AbstractBoardDataHandler<BoardCommonApplicationsDataQuery, BoardCommonApplicationsDataResult> {
    @Override
    public BoardCommonApplicationsDataResult getData(Map<String, Object> param) {
        BoardCommonApplicationsDataQuery query = convertQueryParams(param);
        BoardCommonApplicationsDataResult result = new BoardCommonApplicationsDataResult();

        OrgUserMenuService orgUserMenuService = SpringUtil.getBean(OrgUserMenuService.class);
        List<OrgUserMenuDTO> menus = orgUserMenuService.getMenusByUserId(UserUtil.getId());

        ApiMenu apiMenu = SpringUtil.getBean(ApiMenu.class);

        // 收集所有菜单ID
        Set<String> menuIds = menus.stream().map(OrgUserMenuDTO::getMenuId).filter(Objects::nonNull).collect(Collectors.toSet());

        if (CollUtil.isEmpty(menuIds)) {
            result.setItems(Collections.emptyList());
            return result;
        }

        // 批量查询菜单信息
        Result<List<MenuAO>> batchResult = apiMenu.listMenus(menuIds);
        List<MenuAO> menuAOList = batchResult != null &&
                batchResult.getSuccess() ? batchResult.getData() : Collections.emptyList();


        menuAOList = menuAOList.stream().filter(Objects::nonNull).collect(Collectors.toList());

        // 获取前端固定菜单的所有title
        Set<String> fixedMenuTitles = FrontFixedMenuEnum.getAllTitles();

        Map<String, OrgUserMenuDTO> menuDtoMap = menus.stream()
                .collect(Collectors.toMap(OrgUserMenuDTO::getMenuId, Function.identity(), (v1, v2) -> v1));

        // 使用LinkedHashMap保持插入顺序，后出现的覆盖先出现的（如果需要保留第一个出现的，可以调整）
        Map<String, BoardCommonApplicationsDataResult.Item> itemMap = new LinkedHashMap<>();

        for (MenuAO menuAO : menuAOList) {
            String title = menuAO.getTitle();

            // 如果title在前端固定菜单中，跳过
            if (fixedMenuTitles.contains(title)) {
                continue;
            }

            // 获取对应的OrgUserMenuDTO以获取menuId
            OrgUserMenuDTO menuDto = menuDtoMap.get(menuAO.getId());

            // 按title去重，如果已经有相同的title，保留第一个
            if (!itemMap.containsKey(title)) {
                BoardCommonApplicationsDataResult.Item item = new BoardCommonApplicationsDataResult.Item(
                        menuDto != null ? menuDto.getMenuId() : null,
                        menuAO.getPath(),
                        title,
                        menuAO.getIcon(),
                        menuAO.getRemark()
                );
                itemMap.put(title, item);
            }
        }

        result.setItems(new ArrayList<>(itemMap.values()));
        return result;

    }
}
