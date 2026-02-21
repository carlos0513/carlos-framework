package com.carlos.oauth.app.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.carlos.boot.util.ResponseUtil;
import com.carlos.core.exception.ServiceException;
import com.carlos.excel.easyexcel.ExcelUtil;
import com.carlos.oauth.app.pojo.dto.AppClientDTO;
import com.carlos.oauth.app.pojo.excel.AppClientExcel;
import com.carlos.oauth.app.service.AppClientService;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *   应用信息导入
 * </p>
 *
 * @author Carlos
 * @date 2025-03-27 13:58
 */
@Slf4j
@Data
@RequiredArgsConstructor
public class AppClientExcelListener implements ReadListener<AppClientExcel> {

    private final AppClientService clientService;


    private List<AppClientExcel> errorList = ListUtils.newArrayList();

    @Override
    public void invoke(final AppClientExcel client, final AnalysisContext context) {
        // 这里可以进行单元格的校验  或者整体的校验
        AppClientDTO dto = new AppClientDTO();
        dto.setClientIssuedAt(client.getClientIssuedAt());
        dto.setClientSecretExpiresAt(client.getClientSecretExpiresAt());
        if (StrUtil.isBlank(client.getAuthenticationMethods())) {
            dto.setAuthenticationMethods(Sets.newHashSet(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()));
        } else {
            dto.setAuthenticationMethods(Sets.newHashSet(StrUtil.split(client.getAuthenticationMethods(), StrUtil.COMMA)));
        }
        if (StrUtil.isBlank(client.getAuthorizationGrantTypes())) {
            dto.setAuthorizationGrantTypes(Sets.newHashSet(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()));
        } else {
            dto.setAuthorizationGrantTypes(Sets.newHashSet(StrUtil.split(client.getAuthorizationGrantTypes(), StrUtil.COMMA)));
        }
        dto.setAppLogo(client.getAppLogo());

        String appName = client.getAppName();
        if (StrUtil.isBlank(appName)) {
            client.setError("应用名称不能为空");
            errorList.add(client);
            return;
        }
        dto.setAppName(appName);
        try {
            clientService.addAppClient(dto);
        } catch (Exception e) {
            log.error("应用信息保存失败：{}", e.getMessage(), e);
            client.setError(e.getMessage());
            errorList.add(client);
        }
    }

    @Override
    public void doAfterAllAnalysed(final AnalysisContext context) {
        // 将处理写过写回
        HttpServletResponse response = ResponseUtil.getResponse();
        String name = "应用导入失败信息-" + System.currentTimeMillis();
        try {
            ExcelUtil.download(response, name, AppClientExcel.class, errorList);
        } catch (Exception e) {
            throw new ServiceException("应用信息导出失败");
        }
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     */
    @Override
    public void onException(final Exception exception, final AnalysisContext context) {
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            final ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            final Integer rowIndex = excelDataConvertException.getRowIndex();
            final Integer columnIndex = excelDataConvertException.getColumnIndex();
            final CellData<?> cellData = excelDataConvertException.getCellData();
            log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
            throw new ServiceException("第" + rowIndex + "行，第" + columnIndex + "列解析异常，数据为:" + cellData + "");
        }
    }

}