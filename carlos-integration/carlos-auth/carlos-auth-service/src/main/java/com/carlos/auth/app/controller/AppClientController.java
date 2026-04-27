package com.carlos.auth.app.controller;

import cn.hutool.core.util.StrUtil;
import cn.idev.excel.FastExcel;
import com.carlos.auth.api.enums.AuthErrorCode;
import com.carlos.auth.app.convert.AppClientConvert;
import com.carlos.auth.app.listener.AppClientExcelListener;
import com.carlos.auth.app.manager.AppClientManager;
import com.carlos.auth.app.pojo.dto.AppClientDTO;
import com.carlos.auth.app.pojo.excel.AppClientExcel;
import com.carlos.auth.app.pojo.param.AppClientCreateParam;
import com.carlos.auth.app.pojo.param.AppClientPageParam;
import com.carlos.auth.app.pojo.param.AppClientUpdateParam;
import com.carlos.auth.app.pojo.vo.AppClientListVO;
import com.carlos.auth.app.pojo.vo.AppClientPageVO;
import com.carlos.auth.app.pojo.vo.AppClientVO;
import com.carlos.auth.app.service.AppClientService;
import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamId;
import com.carlos.core.param.ParamIdSet;
import com.carlos.util.easyexcel.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 应用信息 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-12 14:00:14
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("auth/app/client")
@Tag(name = "应用客户端信息")
public class AppClientController {

    public static final String BASE_NAME = "应用";

    private final AppClientService registeredClientService;

    private final AppClientManager registeredClientManager;

    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public AppClientVO add(@RequestBody @Validated AppClientCreateParam param) {
        AppClientDTO dto = AppClientConvert.INSTANCE.toDTO(param);
        registeredClientService.addAppClient(dto);
        return AppClientConvert.INSTANCE.toVO(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        registeredClientService.deleteAppClient(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated AppClientUpdateParam param) {
        AppClientDTO dto = AppClientConvert.INSTANCE.toDTO(param);
        registeredClientService.updateAppClient(dto);
    }


    @PostMapping("resetSecret")
    @Operation(summary = "重置秘钥")
    public String resetSecret(@RequestBody @Validated ParamId<Serializable> param) {
        return registeredClientService.resetSecret(param.getId());
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public AppClientVO detail(String id) {
        return AppClientConvert.INSTANCE.toVO(registeredClientService.findById(id, true));
    }


    @GetMapping("list")
    @Operation(summary = "应用列表")
    public List<AppClientListVO> list(String keyword) {
        return AppClientConvert.INSTANCE.toListVO(registeredClientService.list(keyword));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<AppClientPageVO> page(AppClientPageParam param) {
        return registeredClientManager.getPage(param);
    }

    @PostMapping("import")
    @Operation(summary = "导入应用数据")
    public void importData(@RequestPart final MultipartFile file) {
        final AppClientExcelListener listener = new AppClientExcelListener(registeredClientService);
        try {
            final String filename = file.getOriginalFilename();
            if (StrUtil.isBlank(filename)) {
                throw AuthErrorCode.AUTH_PARAM_INVALID.exception("文件名不能为空");
            }
            ExcelUtil.checkExcel(filename);
            FastExcel.read(file.getInputStream(), AppClientExcel.class, listener).sheet().doRead();
        } catch (final IOException e) {
            throw AuthErrorCode.AUTH_CLIENT_IMPORT_FAILED.exception();
        }
    }

    @GetMapping("export")
    @Operation(summary = "导出应用数据")
    public void exportUser(final HttpServletResponse response) {
        registeredClientService.export(response, false);
    }

    @GetMapping("export/template")
    @Operation(summary = "导出应用模板")
    public void exportTemplate(final HttpServletResponse response) {
        registeredClientService.export(response, true);
    }
}
