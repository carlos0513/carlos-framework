package com.yunjin.board.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.board.convert.BoardOptionInfoConvert;
import com.yunjin.board.manager.BoardOptionInfoManager;
import com.yunjin.board.pojo.dto.BoardOptionInfoDTO;
import com.yunjin.board.pojo.param.BoardOptionInfoCreateParam;
import com.yunjin.board.pojo.param.BoardOptionInfoPageParam;
import com.yunjin.board.pojo.param.BoardOptionInfoUpdateParam;
import com.yunjin.board.pojo.vo.BoardOptionInfoVO;
import com.yunjin.board.service.BoardOptionInfoService;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamIdSet;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;


/**
 * <p>
 * 工作台卡片选项信息 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
// @RestController
@RequiredArgsConstructor
@RequestMapping("board/option/info")
@Tag(name = "工作台卡片选项信息")
public class BoardOptionInfoController {

    public static final String BASE_NAME = "工作台卡片选项信息";

    private final BoardOptionInfoService optionInfoService;

    private final BoardOptionInfoManager optionInfoManager;


    @ApiOperationSupport(author = "Carlos")
    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated BoardOptionInfoCreateParam param) {
        BoardOptionInfoDTO dto = BoardOptionInfoConvert.INSTANCE.toDTO(param);
        optionInfoService.addBoardOptionInfo(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        optionInfoService.deleteBoardOptionInfo(param.getIds());
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated BoardOptionInfoUpdateParam param) {
        BoardOptionInfoDTO dto = BoardOptionInfoConvert.INSTANCE.toDTO(param);
        optionInfoService.updateBoardOptionInfo(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public BoardOptionInfoVO detail(String id) {
        return BoardOptionInfoConvert.INSTANCE.toVO(optionInfoManager.getDtoById(id));
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<BoardOptionInfoVO> page(BoardOptionInfoPageParam param) {
        return optionInfoManager.getPage(param);
    }
}
