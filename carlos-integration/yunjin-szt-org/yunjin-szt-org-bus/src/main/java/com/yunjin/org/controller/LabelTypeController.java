package com.yunjin.org.controller;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.org.config.AuthorConstant;
import com.yunjin.org.convert.LabelTypeConvert;
import com.yunjin.org.enums.LabelTypeEnum;
import com.yunjin.org.manager.LabelTypeManager;
import com.yunjin.org.pojo.dto.LabelTypeDTO;
import com.yunjin.org.pojo.param.LabelTypeCreateParam;
import com.yunjin.org.pojo.param.LabelTypePageParam;
import com.yunjin.org.pojo.param.LabelTypeUpdateParam;
import com.yunjin.org.pojo.vo.LabelTypeVO;
import com.yunjin.org.service.LabelTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 标签分类 rest服务接口
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-22 15:07:09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/label/type")
@Tag(name = "标签分类")
public class LabelTypeController {

    public static final String BASE_NAME = "标签分类";

    private final LabelTypeService labelTypeService;

    private final LabelTypeManager labelTypeManager;

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated LabelTypeCreateParam param) {
        LabelTypeDTO dto = LabelTypeConvert.INSTANCE.toDTO(param);
        if(labelTypeService.isExist(dto)){
            throw new ServiceException("标签类型已存在");
        }
        labelTypeService.addBbtLabelType(dto);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        labelTypeService.deleteBbtLabelType(param.getIds());
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("{parentId}")
    @Operation(summary = "获取本级分类已存在的" + BASE_NAME+ "的个数")
    public int getTopNum(@PathVariable String parentId) {
        return labelTypeService.listByParentId(parentId,Boolean.FALSE).size();
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated LabelTypeUpdateParam param) {
        Boolean isToHidden = labelTypeService.isToHidden(param.getId(),param.getHidden());
        Boolean isToOpen = labelTypeService.isToOpen(param.getId(),param.getHidden());
        LabelTypeDTO dto = LabelTypeConvert.INSTANCE.toDTO(param);
        labelTypeService.updateBbtLabelType(dto,null,null);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    public LabelTypeVO detail(@PathVariable String id) {
        return LabelTypeConvert.INSTANCE.toVO(labelTypeManager.getDtoById(id));
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<LabelTypeVO> page(LabelTypePageParam param) {
        return labelTypeManager.getPage(param);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("list")
    @Operation(summary = BASE_NAME + "列表")
    public List<Tree<String>> list(@RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "labelType") LabelTypeEnum labelType) {
        return labelTypeService.treeList(name, labelType);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("tree")
    @Operation(summary = BASE_NAME + "树形")
    public List<Tree<String>> tree(@RequestParam(value = "labelName", required = false) String labelName,
                                   @RequestParam(value = "typeHidden", required = false) String typeHidden,
                                   @RequestParam(value = "labelHidden", required = false) String labelHidden,
                                   @RequestParam(value = "labelType") LabelTypeEnum labelType) {
        return StrUtil.isBlankIfStr(labelName) ?
                labelTypeService.getTree(typeHidden, labelHidden, labelType) : labelTypeService.getTree(labelName, typeHidden, labelHidden, labelType);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("treeByType")
    @Operation(summary = "根据分类名称获取" + BASE_NAME + "树形")
    public List<Tree<String>> treeByType(@RequestParam(value = "typeName") String typeName,
                                         @RequestParam(value = "typeHidden", required = false) String typeHidden,
                                         @RequestParam(value = "labelHidden", required = false) String labelHidden,
                                         @RequestParam(value = "labelType") LabelTypeEnum labelType) {
        return labelTypeService.getTreeByType(typeName, typeHidden, labelHidden, labelType);
    }
}
