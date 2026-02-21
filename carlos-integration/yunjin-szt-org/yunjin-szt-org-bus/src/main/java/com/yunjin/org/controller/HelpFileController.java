package com.yunjin.org.controller;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.org.config.AuthorConstant;
import com.yunjin.org.convert.HelpFileConvert;
import com.yunjin.org.manager.HelpFileManager;
import com.yunjin.org.pojo.dto.HelpFileDTO;
import com.yunjin.org.pojo.param.HelpFileCreateParam;
import com.yunjin.org.pojo.param.HelpFilePageParam;
import com.yunjin.org.pojo.param.HelpFileUpdateParam;
import com.yunjin.org.pojo.vo.HelpFileVO;
import com.yunjin.org.service.HelpFileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("org/helpfile")
@Tag(name = "帮助中心文件")
public class HelpFileController {
    public static final String BASE_NAME = "帮助中心文件";
    public final HelpFileService helpFileService;
    public final HelpFileManager helpFileManager;
    @ApiOperationSupport(author = AuthorConstant.LIUHUAN)
    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated HelpFileCreateParam param) {
        String fileName = param.getFileName();
        HelpFileDTO dto = HelpFileConvert.INSTANCE.toDTO(param);
        if(StrUtil.isEmpty(fileName)){
            throw new RuntimeException("文件名称不能为空");
        }
        Boolean exist = helpFileService.isExist(fileName);
        if(exist){
            throw new RuntimeException("文件名称已存在");
        }
         helpFileService.add(dto);
    }
    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    public HelpFileVO detail(@PathVariable String id) {
        return HelpFileConvert.INSTANCE.toVO(helpFileManager.getDtoById(id));
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<HelpFileVO> page(HelpFilePageParam param) {
        return helpFileManager.getPage(param);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        helpFileManager.deleteHelpFile(param.getIds());
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated HelpFileUpdateParam param) {
        HelpFileDTO dto = HelpFileConvert.INSTANCE.toDTO(param);
        helpFileService.updateBbtLabelType(dto,null,null);
    }
}
