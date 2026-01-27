package com.carlos.tool.codege.entity;

import java.sql.DatabaseMetaData;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 表基本信息
 * </p>
 *
 * @author Carlos
 * @date 2021/6/27 23:27
 */
@Accessors(chain = true)
@Data
public class MetaDataInfo {

    /**
     * 数据库综合信息对象
     */
    private DatabaseMetaData metaData;

    /**
     * 数据库名
     */
    private String schema;

    /**
     * 数据库名
     */
    private String catalog;

    /**
     * 需要生成代码的表
     */
    private List<TableInfo> tables;

}
