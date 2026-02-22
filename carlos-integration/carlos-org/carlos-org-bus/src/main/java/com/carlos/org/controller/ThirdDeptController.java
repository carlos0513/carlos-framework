package com.carlos.org.controller;


import com.carlos.core.pagination.Paging;
import com.carlos.org.config.AuthorConstant;
import com.carlos.org.convert.DepartmentConvert;
import com.carlos.org.pojo.dto.DepartmentDTO;
import com.carlos.org.pojo.param.DepartmentCreateParam;
import com.carlos.org.pojo.param.DepartmentPageParam;
import com.carlos.org.pojo.param.UserPageParam;
import com.carlos.org.pojo.vo.DepartmentBaseVO;
import com.carlos.org.pojo.vo.DepartmentDetailVO;
import com.carlos.org.pojo.vo.DepartmentTreeVO;
import com.carlos.org.pojo.vo.ThirdDepartmentVO;
import com.carlos.org.service.DepartmentService;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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


    @GetMapping("/thirdDeptTree")
    @Operation(summary = "三方系统组织机构树")
    public List<DepartmentTreeVO> thirdDeptTree() {
        List<DepartmentDTO> dtos = this.departmentService.thirdDeptTree();
        return DepartmentConvert.INSTANCE.toTreeVO(dtos);
    }


    @GetMapping("/systemDeptTree")
    @Operation(summary = "本系统组织机构树")
    public List<DepartmentTreeVO> systemDeptTree() {
        List<DepartmentDTO> dtos = this.departmentService.systemDeptTree();
        return DepartmentConvert.INSTANCE.toTreeVO(dtos);
    }


    @PostMapping("/thirdPage")
    @Operation(summary = "组织机构分页")
    public Paging<ThirdDepartmentVO> thirdPage(@RequestBody DepartmentPageParam param) {
        return departmentService.thirdPage(param);
    }


    @PostMapping("/deptInfoPage")
    @Operation(summary = "本系统组织机构信息分页")
    public Paging<ThirdDepartmentVO> deptInfoPage(@RequestBody DepartmentPageParam param) {
        return departmentService.deptInfoPage(param);
    }

    @PostMapping("/edit")
    @Operation(summary = "新增或修改部门")

    public DepartmentBaseVO edit(@RequestBody @Validated DepartmentCreateParam param) {
        DepartmentDTO dto = DepartmentConvert.INSTANCE.toDTO(param);
        this.departmentService.saveOrUpdate(dto);
        return DepartmentConvert.INSTANCE.toBaseVO(dto);
    }


    @GetMapping("{id}")
    @Operation(summary = "部门编辑详情")
    public DepartmentDetailVO detail(@PathVariable String id, UserPageParam param) {
        return DepartmentConvert.INSTANCE.toDetailVO(this.departmentService.getDepartmentDetail(id, param));
    }

}
