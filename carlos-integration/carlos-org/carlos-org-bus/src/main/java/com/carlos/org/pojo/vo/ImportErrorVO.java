package com.carlos.org.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 导入错误明细
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportErrorVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 行号
     */
    private Integer rowIndex;

    /**
     * 错误信息
     */
    private String errorMsg;
}
