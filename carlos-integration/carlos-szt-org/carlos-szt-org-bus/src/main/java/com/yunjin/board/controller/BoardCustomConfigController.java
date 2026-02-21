package com.yunjin.board.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.board.convert.BoardCustomConfigConvert;
import com.yunjin.board.pojo.param.BoardCustomConfigModifyParam;
import com.yunjin.board.pojo.vo.BoardCustomConfigVO;
import com.yunjin.board.service.BoardCustomConfigService;
import com.yunjin.org.UserUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


/**
 * <p>
 * 看板自定义配置 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/board/custom/config")
@Tag(name = "看板自定义配置")
public class BoardCustomConfigController {

    public static final String BASE_NAME = "看板自定义配置";

    private final BoardCustomConfigService customConfigService;


    @ApiOperationSupport(author = "Carlos")
    @PostMapping("modify")
    @Operation(summary = "修改用户自定义配置")
    public void add(@RequestBody @Validated BoardCustomConfigModifyParam param) {
        customConfigService.changeConfig(BoardCustomConfigConvert.INSTANCE.toConfigDetail(param.getItems()));
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("user")
    @Operation(summary = "获取用户自定义配置")
    public List<BoardCustomConfigVO> userConfig() {
        String userId = UserUtil.getId();
        Set<String> roleIds = UserUtil.getRoleId();
        String roleId = roleIds.stream().findFirst().orElse(null);
        return BoardCustomConfigConvert.INSTANCE.toConfigVO(customConfigService.getUserConfig(userId, roleId));
    }
}
