package com.carlos.org.position.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.org.position.convert.OrgPositionLevelConvert;
import com.carlos.org.position.manager.OrgPositionLevelManager;
import com.carlos.org.position.pojo.dto.OrgPositionLevelDTO;
import com.carlos.org.position.pojo.param.OrgPositionLevelCreateParam;
import com.carlos.org.position.pojo.param.OrgPositionLevelPageParam;
import com.carlos.org.position.pojo.param.OrgPositionLevelUpdateParam;
import com.carlos.org.position.pojo.vo.OrgPositionLevelVO;
import com.carlos.org.position.service.OrgPositionLevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 职级表 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/position/level")
@Tag(name = "职级表")
public class OrgPositionLevelController {

    public static final String BASE_NAME = "职级表";

    private final OrgPositionLevelService positionLevelService;

    private final OrgPositionLevelManager positionLevelManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgPositionLevelCreateParam param) {
        OrgPositionLevelDTO dto = OrgPositionLevelConvert.INSTANCE.toDTO(param);
        positionLevelService.addOrgPositionLevel(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        positionLevelService.deleteOrgPositionLevel(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgPositionLevelUpdateParam param) {
        OrgPositionLevelDTO dto = OrgPositionLevelConvert.INSTANCE.toDTO(param);
        positionLevelService.updateOrgPositionLevel(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public OrgPositionLevelVO detail(String id) {
        return OrgPositionLevelConvert.INSTANCE.toVO(positionLevelManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<OrgPositionLevelVO> page(OrgPositionLevelPageParam param) {
        return positionLevelManager.getPage(param);
    }
}
