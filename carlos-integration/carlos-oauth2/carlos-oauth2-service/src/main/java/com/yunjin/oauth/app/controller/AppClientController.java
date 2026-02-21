package com.carlos.oauth.app.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamId;
import com.carlos.core.param.ParamIdSet;
import com.carlos.excel.easyexcel.ExcelUtil;
import com.carlos.oauth.app.convert.AppClientConvert;
import com.carlos.oauth.app.listener.AppClientExcelListener;
import com.carlos.oauth.app.manager.AppClientManager;
import com.carlos.oauth.app.pojo.dto.AppClientDTO;
import com.carlos.oauth.app.pojo.excel.AppClientExcel;
import com.carlos.oauth.app.pojo.param.AppClientCreateParam;
import com.carlos.oauth.app.pojo.param.AppClientPageParam;
import com.carlos.oauth.app.pojo.param.AppClientUpdateParam;
import com.carlos.oauth.app.pojo.vo.AppClientListVO;
import com.carlos.oauth.app.pojo.vo.AppClientPageVO;
import com.carlos.oauth.app.pojo.vo.AppClientVO;
import com.carlos.oauth.app.service.AppClientService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("oauth2/app/client")
@Api(tags = "应用客户端信息")
public class AppClientController {

    public static final String BASE_NAME = "应用";

    private final AppClientService registeredClientService;

    private final AppClientManager registeredClientManager;


    @ApiOperationSupport(author = "Carlos")
    @PostMapping("add")
    @ApiOperation(value = "新增" + BASE_NAME)
    public AppClientVO add(@RequestBody @Validated AppClientCreateParam param) {
        AppClientDTO dto = AppClientConvert.INSTANCE.toDTO(param);
        registeredClientService.addAppClient(dto);
        return AppClientConvert.INSTANCE.toVO(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("delete")
    @ApiOperation(value = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        registeredClientService.deleteAppClient(param.getIds());
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("update")
    @ApiOperation(value = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated AppClientUpdateParam param) {
        AppClientDTO dto = AppClientConvert.INSTANCE.toDTO(param);
        registeredClientService.updateAppClient(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("resetSecret")
    @ApiOperation(value = "重置秘钥")
    public String resetSecret(@RequestBody @Validated ParamId<Serializable> param) {
        return registeredClientService.resetSecret(param.getId());
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("detail")
    @ApiOperation(value = BASE_NAME + "详情")
    public AppClientVO detail(String id) {
        return AppClientConvert.INSTANCE.toVO(registeredClientService.findById(id, true));
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("list")
    @ApiOperation(value = "应用列表")
    public List<AppClientListVO> list(String keyword) {
        return AppClientConvert.INSTANCE.toListVO(registeredClientService.list(keyword));
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("page")
    @ApiOperation(value = BASE_NAME + "分页列表")
    public Paging<AppClientPageVO> page(AppClientPageParam param) {
        return registeredClientManager.getPage(param);
    }

    @PostMapping("import")
    @ApiOperation(value = "导入应用数据")
    public void importData(@RequestPart final MultipartFile file) {
        final AppClientExcelListener listener = new AppClientExcelListener(registeredClientService);
        try {
            final String filename = file.getOriginalFilename();
            if (StrUtil.isBlank(filename)) {
                throw new ServiceException("文件名不能为空");
            }
            ExcelUtil.checkExcel(filename);
            EasyExcel.read(file.getInputStream(), AppClientExcel.class, listener).sheet().doRead();
        } catch (final IOException e) {

        }
    }

    @GetMapping("export")
    @ApiOperation(value = "导出应用数据")
    public void exportUser(final HttpServletResponse response) {
        registeredClientService.export(response, false);
    }

    @GetMapping("export/template")
    @ApiOperation(value = "导出应用模板")
    public void exportTemplate(final HttpServletResponse response) {
        registeredClientService.export(response, true);
    }
}
