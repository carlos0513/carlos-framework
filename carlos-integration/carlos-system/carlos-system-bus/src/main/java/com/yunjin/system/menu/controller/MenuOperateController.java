package com.carlos.system.menu.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.system.AuthorConstant;
import com.carlos.system.menu.convert.MenuOperateConvert;
import com.carlos.system.menu.manager.MenuOperateManager;
import com.carlos.system.menu.pojo.dto.MenuOperateDTO;
import com.carlos.system.menu.pojo.param.MenuOperateCreateParam;
import com.carlos.system.menu.pojo.param.MenuOperatePageParam;
import com.carlos.system.menu.pojo.param.MenuOperateSearchParam;
import com.carlos.system.menu.pojo.param.MenuOperateUpdateParam;
import com.carlos.system.menu.pojo.vo.MenuOperateVO;
import com.carlos.system.menu.service.MenuOperateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 菜单操作 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/menu/operate")
@Tag(name = "菜单操作")
public class MenuOperateController {

    public static final String BASE_NAME = "菜单操作";

    private final MenuOperateService menuOperateService;

    private final MenuOperateManager menuOperateManager;


    @ApiOperationSupport(author = "yunjin")
    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated MenuOperateCreateParam param) {
        MenuOperateDTO dto = MenuOperateConvert.INSTANCE.toDTO(param);
        menuOperateService.addMenuOperate(dto);
    }

    @ApiOperationSupport(author = "yunjin")
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        menuOperateService.deleteMenuOperate(param.getIds());
    }

    @ApiOperationSupport(author = "yunjin")
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated MenuOperateUpdateParam param) {
        MenuOperateDTO dto = MenuOperateConvert.INSTANCE.toDTO(param);
        menuOperateService.updateMenuOperate(dto);
    }

    @ApiOperationSupport(author = "yunjin")
    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    public MenuOperateVO detail(@PathVariable String id) {
        return MenuOperateConvert.INSTANCE.toVO(menuOperateManager.getDtoById(id));
    }

    @ApiOperationSupport(author = "yunjin")
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<MenuOperateVO> page(MenuOperatePageParam param) {
        return menuOperateManager.getPage(param);
    }

    @ApiOperationSupport(author = AuthorConstant.YANG_LE)
    @GetMapping("list")
    @Operation(summary = BASE_NAME + "查询列表")
    public List<MenuOperateVO> list(MenuOperateSearchParam param) {
        List<MenuOperateDTO> list = menuOperateService.listByKeyword(param);
        return MenuOperateConvert.INSTANCE.toVOS(list);
    }
}
