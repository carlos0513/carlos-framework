package com.carlos.org.position.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.org.position.convert.OrgUserPositionConvert;
import com.carlos.org.position.manager.OrgUserPositionManager;
import com.carlos.org.position.pojo.dto.OrgUserPositionDTO;
import com.carlos.org.position.pojo.param.OrgUserPositionCreateParam;
import com.carlos.org.position.pojo.param.OrgUserPositionPageParam;
import com.carlos.org.position.pojo.param.OrgUserPositionUpdateParam;
import com.carlos.org.position.pojo.vo.OrgUserPositionVO;
import com.carlos.org.position.service.OrgUserPositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 用户岗位职级关联表（核心任职信息） rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/user/position")
@Tag(name = "用户岗位职级关联表（核心任职信息）")
public class OrgUserPositionController {

    public static final String BASE_NAME = "用户岗位职级关联表（核心任职信息）";

    private final OrgUserPositionService userPositionService;

    private final OrgUserPositionManager userPositionManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgUserPositionCreateParam param) {
        OrgUserPositionDTO dto = OrgUserPositionConvert.INSTANCE.toDTO(param);
        userPositionService.addOrgUserPosition(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        userPositionService.deleteOrgUserPosition(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgUserPositionUpdateParam param) {
        OrgUserPositionDTO dto = OrgUserPositionConvert.INSTANCE.toDTO(param);
        userPositionService.updateOrgUserPosition(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public OrgUserPositionVO detail(String id) {
        return OrgUserPositionConvert.INSTANCE.toVO(userPositionManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<OrgUserPositionVO> page(OrgUserPositionPageParam param) {
        return userPositionManager.getPage(param);
    }
}
