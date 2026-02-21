package com.yunjin.board.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.board.convert.BoardCardInfoConvert;
import com.yunjin.board.manager.BoardCardInfoManager;
import com.yunjin.board.pojo.dto.BoardCardInfoDTO;
import com.yunjin.board.pojo.param.BoardCardInfoCreateParam;
import com.yunjin.board.pojo.param.BoardCardInfoPageParam;
import com.yunjin.board.pojo.param.BoardCardInfoUpdateParam;
import com.yunjin.board.pojo.vo.BoardCardInfoVO;
import com.yunjin.board.service.BoardCardInfoService;
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
 * 工作台卡片信息 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
// @RestController
@RequiredArgsConstructor
@RequestMapping("board/card/info")
@Tag(name = "工作台卡片信息")
public class BoardCardInfoController {

    public static final String BASE_NAME = "工作台卡片信息";

    private final BoardCardInfoService cardInfoService;

    private final BoardCardInfoManager cardInfoManager;


    @ApiOperationSupport(author = "Carlos")
    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated BoardCardInfoCreateParam param) {
        BoardCardInfoDTO dto = BoardCardInfoConvert.INSTANCE.toDTO(param);
        cardInfoService.addBoardCardInfo(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        cardInfoService.deleteBoardCardInfo(param.getIds());
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated BoardCardInfoUpdateParam param) {
        BoardCardInfoDTO dto = BoardCardInfoConvert.INSTANCE.toDTO(param);
        cardInfoService.updateBoardCardInfo(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public BoardCardInfoVO detail(String id) {
        return BoardCardInfoConvert.INSTANCE.toVO(cardInfoManager.getDtoById(id));
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<BoardCardInfoVO> page(BoardCardInfoPageParam param) {
        return cardInfoManager.getPage(param);
    }
}
