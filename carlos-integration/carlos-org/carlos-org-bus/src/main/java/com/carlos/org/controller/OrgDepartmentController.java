package com.carlos.org.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.org.convert.OrgDepartmentConvert;
import com.carlos.org.manager.OrgDepartmentManager;
import com.carlos.org.pojo.dto.OrgDepartmentDTO;
import com.carlos.org.pojo.param.OrgDepartmentCreateParam;
import com.carlos.org.pojo.param.OrgDepartmentPageParam;
import com.carlos.org.pojo.param.OrgDepartmentUpdateParam;
import com.carlos.org.pojo.vo.OrgDepartmentVO;
import com.carlos.org.service.OrgDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 部门 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/department")
@Tag(name = "部门")
public class OrgDepartmentController {

    public static final String BASE_NAME = "部门";

    private final OrgDepartmentService departmentService;

    private final OrgDepartmentManager departmentManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgDepartmentCreateParam param) {
        OrgDepartmentDTO dto = OrgDepartmentConvert.INSTANCE.toDTO(param);
        departmentService.addOrgDepartment(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        departmentService.deleteOrgDepartment(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgDepartmentUpdateParam param) {
        OrgDepartmentDTO dto = OrgDepartmentConvert.INSTANCE.toDTO(param);
        departmentService.updateOrgDepartment(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public OrgDepartmentVO detail(String id) {
        return OrgDepartmentConvert.INSTANCE.toVO(departmentManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<OrgDepartmentVO> page(OrgDepartmentPageParam param) {
        return departmentManager.getPage(param);
    }
}
