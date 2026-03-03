package com.carlos.org.position.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.org.position.convert.OrgPositionCategoryConvert;
import com.carlos.org.position.manager.OrgPositionCategoryManager;
import com.carlos.org.position.pojo.dto.OrgPositionCategoryDTO;
import com.carlos.org.position.pojo.param.OrgPositionCategoryCreateParam;
import com.carlos.org.position.pojo.param.OrgPositionCategoryPageParam;
import com.carlos.org.position.pojo.param.OrgPositionCategoryUpdateParam;
import com.carlos.org.position.pojo.vo.OrgPositionCategoryVO;
import com.carlos.org.position.service.OrgPositionCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 岗位类别表（职系） rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/position/category")
@Tag(name = "岗位类别表（职系）")
public class OrgPositionCategoryController {

    public static final String BASE_NAME = "岗位类别表（职系）";

    private final OrgPositionCategoryService positionCategoryService;

    private final OrgPositionCategoryManager positionCategoryManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgPositionCategoryCreateParam param) {
        OrgPositionCategoryDTO dto = OrgPositionCategoryConvert.INSTANCE.toDTO(param);
        positionCategoryService.addOrgPositionCategory(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        positionCategoryService.deleteOrgPositionCategory(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgPositionCategoryUpdateParam param) {
        OrgPositionCategoryDTO dto = OrgPositionCategoryConvert.INSTANCE.toDTO(param);
        positionCategoryService.updateOrgPositionCategory(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public OrgPositionCategoryVO detail(String id) {
        return OrgPositionCategoryConvert.INSTANCE.toVO(positionCategoryManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<OrgPositionCategoryVO> page(OrgPositionCategoryPageParam param) {
        return positionCategoryManager.getPage(param);
    }
}
