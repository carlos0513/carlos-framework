package com.carlos.org.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户导入结果 VO
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Data
public class UserImportResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总行数
     */
    private Integer totalCount = 0;

    /**
     * 成功数
     */
    private Integer successCount = 0;

    /**
     * 失败数
     */
    private Integer failCount = 0;

    /**
     * 错误明细
     */
    private List<ImportErrorVO> errors = new ArrayList<>();

    /**
     * 添加错误记录
     */
    public void addError(Integer rowIndex, String errorMsg) {
        this.errors.add(new ImportErrorVO(rowIndex, errorMsg));
        this.failCount++;
    }

    /**
     * 增加成功计数
     */
    public void incrementSuccess() {
        this.successCount++;
    }

    /**
     * 增加总行数
     */
    public void incrementTotal() {
        this.totalCount++;
    }
}
