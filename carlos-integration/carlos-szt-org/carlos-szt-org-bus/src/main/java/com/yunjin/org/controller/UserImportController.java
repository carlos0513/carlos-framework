package com.yunjin.org.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Lists;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.core.response.Result;
import com.yunjin.docking.tftd.TfAuthService;
import com.yunjin.log.annotation.Log;
import com.yunjin.log.commonlog.pojo.common.LogConstant;
import com.yunjin.log.core.aop.aspect.LogCollector;
import com.yunjin.log.enums.BusinessType;
import com.yunjin.org.config.AuthorConstant;
import com.yunjin.org.pojo.entity.UserImport;
import com.yunjin.org.pojo.vo.UserImportCheckVO;
import com.yunjin.org.service.UserImportService;
import com.yunjin.system.api.ApiResource;
import com.yunjin.system.pojo.param.ApiResourceCategoryAddParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;


/**
 * <p>
 * 用户导入 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2023-5-27 12:52:09
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/org/init/user")
@Tag(name = "用户-数据初始化")
public class UserImportController {


    private final UserImportService userImportService;


    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("tfsync")
    @Operation(summary = "天府用户信息手动同步")
    public void tfsync(String token) {
        checkToken(token);
        try {
            TfAuthService bean = SpringUtil.getBean(TfAuthService.class);
            bean.syncUserInfo();
        } catch (Exception e) {
            log.error("sync user info failed:", e);
            throw new ServiceException("用户同步失败");
        }
    }


    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("export")
    @Operation(summary = "导出当前系统用户信息")
    public void export(HttpServletResponse response, String token) {
        checkToken(token);
        userImportService.export(response);
    }


    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("test")
    @Operation(summary = "导出当前系统用户信息")
    public Boolean test(HttpServletResponse response, String token) {
        checkToken(token);
        ApiResource bean = SpringUtil.getBean(ApiResource.class);
        ApiResourceCategoryAddParam param = new ApiResourceCategoryAddParam();
        param.setId("1325413");
        param.setParentId("0");
        param.setName("测试");
        param.setCreateTime(LocalDateTime.now());
        param.setUpdateTime(LocalDateTime.now());
        param.setChildren(Lists.newArrayList());
        Result<Boolean> listResult = bean.addResourceCategory(param);
        return listResult.getData();
    }


    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("init")
    @Operation(summary = "初始化用户信息")
    public Map<String, UserImport> init(String token) {
        checkToken(token);
        return userImportService.init();
    }

    @PostMapping("import/user")
    @Operation(summary = "导入用户")
    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @LogCollector(businessMod = "用户导入", businessType = "导入用户", businessDesc = "导入用户", logType =
            LogConstant.REQUEST_LOG)
    @Log(title = "导入用户", businessType = BusinessType.IMPORT)
    public UserImportCheckVO importUserData(@RequestPart MultipartFile file) {
        //return userImportService.handleUserData(file);
        return userImportService.importUserData(file);
    }

    @GetMapping("export/userTemplate")
    @Operation(summary = "生成用户导入模版")
    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @LogCollector(businessMod = "生成用户导入模板", businessType = "生成用户导入模板", businessDesc = "生成用户导入模板", logType =
            LogConstant.REQUEST_LOG)
    public void exportUserTemplate(HttpServletResponse response) {
        userImportService.exportUserTemplate(response);
    }

    @GetMapping("export/deptTemplate")
    @Operation(summary = "生成部门导入模版")
    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @LogCollector(businessMod = "生成部门导入模板", businessType = "生成部门导入模板", businessDesc = "生成部门导入模板", logType =
            LogConstant.REQUEST_LOG)
    public void exportDeptTemplate(HttpServletResponse response) {
        userImportService.exportDeptTemplate(response);
    }

    @GetMapping("export/regionTemplate")
    @Operation(summary = "生成区域导入模板")
    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @LogCollector(businessMod = "生成区域导入模板", businessType = "生成区域导入模板", businessDesc = "生成区域导入模板", logType =
            LogConstant.REQUEST_LOG)
    public void exportRegionTemplate(HttpServletResponse response) {
        userImportService.exportRegionTemplate(response);
    }

    @PostMapping("import/dept")
    @Operation(summary = "导入部门")
    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @LogCollector(businessMod = "部门导入", businessType = "部门导入", businessDesc = "部门导入", logType =
            LogConstant.REQUEST_LOG)
    @Log(title = "部门导入", businessType = BusinessType.IMPORT)
    public String importDeptData(@RequestPart MultipartFile file) {
        return userImportService.handleDeptData(file);
    }

    @PostMapping("import/region")
    @Operation(summary = "导入区域")
    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @LogCollector(businessMod = "导入区域", businessType = "导入区域", businessDesc = "导入区域", logType =
            LogConstant.REQUEST_LOG)
    @Log(title = "导入区域", businessType = BusinessType.IMPORT)
    public String importRegionData(@RequestPart MultipartFile file) {
        return userImportService.handleRegionData(file);
    }


    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("initDeptRegion")
    @Operation(summary = "初始化部门区域")
    public void initDeptRegion(HttpServletResponse response, String token) {
        checkToken(token);
        userImportService.initDeptRegion();
    }

    /**
     * 检查口令
     *
     * @param token 参数0
     * @author Carlos
     * @date 2024/9/14 14:53
     */
    private void checkToken(String token) {
        String format = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        if (token.equals(format)) {
            return;
        }
        throw new ServiceException("口令验证失败，请联系开发人员获取口令");
    }


    @PostMapping("generate/test")
    @Operation(summary = "根据行政区划生成用户模块测试数据")
    public void generate(HttpServletResponse response) {
        userImportService.generate(response);
    }


}
