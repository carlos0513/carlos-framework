package com.carlos.org.excel;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import com.carlos.org.pojo.dto.UserImportExcel;
import com.carlos.org.pojo.vo.UserImportResultVO;
import com.carlos.org.service.OrgUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户导入 Excel 监听器
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Slf4j
@RequiredArgsConstructor
public class UserImportListener extends AnalysisEventListener<UserImportExcel> {

    /**
     * 每批处理数量
     */
    private static final int BATCH_COUNT = 100;

    /**
     * 缓存数据
     */
    private final List<UserImportExcel> cachedDataList = new ArrayList<>();

    /**
     * 导入结果
     */
    private final UserImportResultVO result = new UserImportResultVO();

    /**
     * 行号计数器（从2开始，第1行是表头）
     */
    private int rowIndex = 1;

    private final OrgUserService userService;

    @Override
    public void invoke(UserImportExcel data, AnalysisContext context) {
        rowIndex++;
        result.incrementTotal();

        // 数据校验
        String errorMsg = validate(data);
        if (errorMsg != null) {
            data.setErrorMsg(errorMsg);
            result.addError(rowIndex, errorMsg);
            return;
        }

        cachedDataList.add(data);

        // 达到批量阈值时处理
        if (cachedDataList.size() >= BATCH_COUNT) {
            processData();
            cachedDataList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 处理剩余数据
        if (!cachedDataList.isEmpty()) {
            processData();
        }
        log.info("用户导入完成：总计 {} 条，成功 {} 条，失败 {} 条", 
            result.getTotalCount(), result.getSuccessCount(), result.getFailCount());
    }

    /**
     * 获取导入结果
     */
    public UserImportResultVO getResult() {
        return result;
    }

    /**
     * 处理批量数据
     */
    private void processData() {
        int success = userService.batchImportUsers(cachedDataList);
        for (int i = 0; i < success; i++) {
            result.incrementSuccess();
        }
    }

    /**
     * 校验单条数据
     */
    private String validate(UserImportExcel data) {
        StringBuilder errors = new StringBuilder();

        // 校验用户名
        if (data.getUsername() == null || data.getUsername().trim().isEmpty()) {
            errors.append("用户名不能为空;");
        }

        // 校验姓名
        if (data.getRealName() == null || data.getRealName().trim().isEmpty()) {
            errors.append("姓名不能为空;");
        }

        // 校验手机号格式
        if (data.getPhone() != null && !data.getPhone().isEmpty()) {
            if (!data.getPhone().matches("^1[3-9]\\d{9}$")) {
                errors.append("手机号格式不正确;");
            }
        }

        // 校验邮箱格式
        if (data.getEmail() != null && !data.getEmail().isEmpty()) {
            if (!data.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                errors.append("邮箱格式不正确;");
            }
        }

        return errors.length() > 0 ? errors.toString() : null;
    }
}
