package com.carlos.org.position.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.org.position.convert.OrgPositionHistoryConvert;
import com.carlos.org.position.manager.OrgPositionHistoryManager;
import com.carlos.org.position.pojo.dto.OrgPositionHistoryDTO;
import com.carlos.org.position.pojo.param.OrgPositionHistoryCreateParam;
import com.carlos.org.position.pojo.param.OrgPositionHistoryPageParam;
import com.carlos.org.position.pojo.param.OrgPositionHistoryUpdateParam;
import com.carlos.org.position.pojo.vo.OrgPositionHistoryVO;
import com.carlos.org.position.service.OrgPositionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 岗位变更历史表 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/position/history")
@Tag(name = "岗位变更历史表")
public class OrgPositionHistoryController {

    public static final String BASE_NAME = "岗位变更历史表";

    private final OrgPositionHistoryService positionHistoryService;

    private final OrgPositionHistoryManager positionHistoryManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgPositionHistoryCreateParam param) {
        OrgPositionHistoryDTO dto = OrgPositionHistoryConvert.INSTANCE.toDTO(param);
        positionHistoryService.addOrgPositionHistory(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        positionHistoryService.deleteOrgPositionHistory(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgPositionHistoryUpdateParam param) {
        OrgPositionHistoryDTO dto = OrgPositionHistoryConvert.INSTANCE.toDTO(param);
        positionHistoryService.updateOrgPositionHistory(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public OrgPositionHistoryVO detail(String id) {
        return OrgPositionHistoryConvert.INSTANCE.toVO(positionHistoryManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<OrgPositionHistoryVO> page(OrgPositionHistoryPageParam param) {
        return positionHistoryManager.getPage(param);
    }
}
