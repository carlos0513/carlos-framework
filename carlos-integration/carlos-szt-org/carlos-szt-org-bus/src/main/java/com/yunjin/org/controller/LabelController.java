package com.yunjin.org.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.org.UserUtil;
import com.yunjin.org.config.AuthorConstant;
import com.yunjin.org.convert.LabelConvert;
import com.yunjin.org.pojo.param.LabelCreateParam;
import com.yunjin.org.pojo.param.LabelUpdateParam;
import com.yunjin.org.pojo.param.LabelPageParam;
import com.yunjin.org.pojo.dto.LabelDTO;
import com.yunjin.org.pojo.vo.LabelVO;
import com.yunjin.org.service.LabelService;
import com.yunjin.org.manager.LabelManager;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



/**
 * <p>
 * 标签 rest服务接口
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-23 12:31:52
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/label")
@Tag(name = "标签")
public class LabelController {

    public static final String BASE_NAME = "标签";

    private final LabelService labelService;

    private final LabelManager labelManager;


    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated LabelCreateParam param) {
        LabelDTO dto = LabelConvert.INSTANCE.toDTO(param);
        String id = labelService.addLabel(dto,false);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        labelService.deleteLabel(param.getIds());
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated LabelUpdateParam param) {
        // 隐藏优先级最高  隐藏后直接忽略排序
        Boolean isToHidden = labelService.isToHidden(param.getId(),param.getHidden());
        Boolean isToOpen = labelService.isToOpen(param.getId(),param.getHidden());
        LabelDTO dto = LabelConvert.INSTANCE.toDTO(param);
        labelService.updateLabel(dto,null,null);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    public LabelVO detail(@PathVariable String id) {
        return LabelConvert.INSTANCE.toVO(labelManager.getDtoById(id));
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<LabelVO> page(LabelPageParam param) {
        return labelManager.getPage(param);
    }
}
