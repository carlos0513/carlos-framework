package com.yunjin.org.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.pagination.PageInfo;
import com.yunjin.log.annotation.Log;
import com.yunjin.log.enums.BusinessType;
import com.yunjin.org.config.AuthorConstant;
import com.yunjin.org.convert.DepartmentConvert;
import com.yunjin.org.manager.DepartmentManager;
import com.yunjin.org.pojo.dto.DepartmentDTO;
import com.yunjin.org.pojo.param.*;
import com.yunjin.org.pojo.vo.*;
import com.yunjin.org.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 部门 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/org/third/department")
@Tag(name = "用户-三方系统组织机构")
@ApiSupport(author = AuthorConstant.ZHU_JUN)
public class ThirdDeptController {

    public static final String BASE_NAME = "部门";

    private final DepartmentService departmentService;

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("/thirdDeptTree")
    @Operation(summary = "三方系统组织机构树")
    public List<DepartmentTreeVO> thirdDeptTree() {
        List<DepartmentDTO> dtos = this.departmentService.thirdDeptTree();
        return DepartmentConvert.INSTANCE.toTreeVO(dtos);
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("/systemDeptTree")
    @Operation(summary = "本系统组织机构树")
    public List<DepartmentTreeVO> systemDeptTree() {
        List<DepartmentDTO> dtos = this.departmentService.systemDeptTree();
        return DepartmentConvert.INSTANCE.toTreeVO(dtos);
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @PostMapping("/thirdPage")
    @Operation(summary = "组织机构分页")
    public Paging<ThirdDepartmentVO> thirdPage(@RequestBody DepartmentPageParam param) {
        return departmentService.thirdPage(param);
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @PostMapping("/deptInfoPage")
    @Operation(summary = "本系统组织机构信息分页")
    public Paging<ThirdDepartmentVO> deptInfoPage(@RequestBody DepartmentPageParam param) {
        return departmentService.deptInfoPage(param);
    }

    @PostMapping("/edit")
    @Operation(summary = "新增或修改部门")
    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    public DepartmentBaseVO edit(@RequestBody @Validated DepartmentCreateParam param) {
        DepartmentDTO dto = DepartmentConvert.INSTANCE.toDTO(param);
        this.departmentService.saveOrUpdate(dto);
        return DepartmentConvert.INSTANCE.toBaseVO(dto);
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("{id}")
    @Operation(summary = "部门编辑详情")
    public DepartmentDetailVO detail(@PathVariable String id, UserPageParam param) {
        return DepartmentConvert.INSTANCE.toDetailVO(this.departmentService.getDepartmentDetail(id, param));
    }

}
