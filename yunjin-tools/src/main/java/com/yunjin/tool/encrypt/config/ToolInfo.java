package com.yunjin.tool.encrypt.config;

import com.yunjin.tool.encrypt.enums.ToolType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 项目相关属性
 * </p>
 *
 * @author Carlos
 * @date 2021/10/28 13:58
 */
@Accessors(chain = true)
@Data
public class ToolInfo {

    /**
     * 工具类型
     */
    private ToolType toolType;

    /**
     * 源表
     */
    private String sourceTable;
    /**
     * 目标表
     */
    private String targetTable;
    /**
     * 加解密配置
     */
    private Encrypt encrypt;

    /**
     * 选择的字段
     */
    private List<String> selectFields;


    @Accessors(chain = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Encrypt {

        /**
         * 秘钥
         */
        private String key;
        /**
         * 加密向量
         */
        private String iv;
    }

}
