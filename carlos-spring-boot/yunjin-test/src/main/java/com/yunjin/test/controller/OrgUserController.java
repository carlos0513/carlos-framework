package com.yunjin.test.controller;

import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.excel.luckysheet.ExcelSheet;
import com.yunjin.test.convert.OrgUserConvert;
import com.yunjin.test.manager.OrgUserManager;
import com.yunjin.test.pojo.dto.OrgUserDTO;
import com.yunjin.test.pojo.param.OrgUserCreateParam;
import com.yunjin.test.pojo.param.OrgUserPageParam;
import com.yunjin.test.pojo.param.OrgUserUpdateParam;
import com.yunjin.test.pojo.vo.OrgUserVO;
import com.yunjin.test.service.OrgUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


/**
 * <p>
 * 系统用户 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2023-8-12 11:16:18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/user")
@Tag(name = "系统用户")
public class OrgUserController {

    public static final String BASE_NAME = "系统用户";

    private final OrgUserService userService;

    private final OrgUserManager userManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgUserCreateParam param) {
        OrgUserDTO dto = OrgUserConvert.INSTANCE.toDTO(param);
        userService.addOrgUser(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        userService.deleteOrgUser(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgUserUpdateParam param) {
        OrgUserDTO dto = OrgUserConvert.INSTANCE.toDTO(param);
        userService.updateOrgUser(dto);
    }


    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    public OrgUserVO detail(@PathVariable String id) {
        return OrgUserConvert.INSTANCE.toVO(userManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<OrgUserVO> page(OrgUserPageParam param) {
        return userManager.getPage(param);
    }


    @PostMapping("excelImport")
    @Operation(summary = "导入Excel数据")
    public List<ExcelSheet> importFile(@RequestPart("file") MultipartFile file) throws IOException {
        return userService.excelImport(file);
    }
}
