package com.carlos.fx.codege.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.fx.codege.config.Constant;
import com.carlos.fx.codege.config.DatabaseInfo;
import com.carlos.fx.codege.entity.ColumnBean;
import com.carlos.fx.codege.entity.TableBean;
import com.carlos.fx.codege.entity.TableInfo;
import com.carlos.fx.codege.entity.TemplateConfig;
import com.carlos.fx.codege.utils.NameUtil;
import com.carlos.fx.codege.utils.XmlUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

/**
 * <p>
 * 和数据库交互相关方法
 * </p>
 *
 * @author Carlos
 * @date 2019/12/25 15:25
 * @ModificationHistory Who  When  What ---------     -------------   --------------------------------------
 */
@Slf4j
public class DatabaseService {

    private final DatabaseInfo databaseInfo;
    private TemplateConfig templateConfig;

    /**
     * 数据库字段类型和Java数据类型映射
     */
    private static final Map<String, String> CONVERT_MAP = XmlUtils.readConvertXml();

    public DatabaseService(DatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
    }

    public DatabaseService(DatabaseInfo databaseInfo, TemplateConfig templateConfig) {
        this.databaseInfo = databaseInfo;
        this.templateConfig = templateConfig;
    }

    /**
     * 当前数据库中具有哪些数据库(当前操作连接数据库后将数据库进行了关闭)
     *
     * @return List<String> 返回数据库中的表名列表
     * @author Carlos
     * @date 2019/4/14 0:05
     */
    public List<String> getSchemas() throws SQLException, ClassNotFoundException {
        ResultSet rs;
        List<String> list;

        // 得到数据库列表信息
        rs = databaseInfo.getMetaData().getCatalogs();
        list = new ArrayList<>();
        String[] sysTable = databaseInfo.getDbType().getSysTable();
        while (rs.next()) {
            String databaseName = rs.getString(1);
            if (ArrayUtil.isNotEmpty(sysTable) && ArrayUtil.contains(sysTable, databaseName)) {
                continue;
            }
            list.add(databaseName);
        }
        release(databaseInfo.getMetaData().getConnection(), rs);
        return list;
    }

    /**
     * 获取库中的所有表信息
     *
     * @return List<Table> 返回数据库中所有的表信息
     * @author Carlos
     * @date 2019/4/12 17:01
     */
    public List<TableInfo> getTables() throws SQLException, ClassNotFoundException {
        // 连接数据库
        Connection connection = databaseInfo.getDataSource().getConnection();
        // 检索DatabaseMetaData对象包含有关哪个这个数据库的元数据Connection对象表示的连接。 元数据包括有关数据库表，其支持的SQL语法，其存储过程，此连接的功能等的信息。
        // 关于整个数据库的综合信息。
        DatabaseMetaData metaData = connection.getMetaData();
        String schema = connection.getSchema();
        String catalog = connection.getCatalog();
        // 针对Oracle的特殊处理
        if ((databaseInfo.getDbType() != null) && (databaseInfo.getDbType().getDescribe().toUpperCase().contains("ORACLE"))) {
            schema = databaseInfo.getUser().toUpperCase();
            catalog = connection.getCatalog();
        }
        ResultSet tables = metaData.getTables(catalog, schema, "%", new String[]{"TABLE"});

        List<TableInfo> list = new ArrayList<>();
        TableInfo table;
        // 表名
        String tableName;
        String tableRemark;
        while (tables.next()) {
            table = new TableInfo();
            tableName = tables.getString("TABLE_NAME");
            tableRemark = tables.getString("REMARKS");

            table.setComment(tableRemark);
            if (tableName.contains("=") || tableName.contains("$")) {
                continue;
            }
            if (tableName.toUpperCase().equals(tableName)) {
                table.setName(tableName.toLowerCase());
            } else {
                table.setName(tableName);
            }
            list.add(table);
        }
        release(connection, tables);
        return list;
    }

    /**
     * 获取数据库中相关的表信息
     *
     * @return List<Table> 返回数据库中所有的表信息
     * @author Carlos
     * @date 2019/4/12 17:01
     */
    public List<TableBean> getTablesDetailInfo() throws SQLException {
        Connection connection = databaseInfo.getDataSource().getConnection();
        String catalog = connection.getCatalog();
        String schema = connection.getSchema();
        DatabaseMetaData metaData = connection.getMetaData();
        List<TableInfo> tables = databaseInfo.getTables();

        List<TableBean> list = new ArrayList<>();
        TableBean table;
        // 表名
        List<ColumnBean> columnList;
        for (TableInfo tableInfo : tables) {
            table = new TableBean();
            String tableName = tableInfo.getName();
            table.setComment(StrUtil.removeAll(tableInfo.getComment(), CharPool.CR, CharPool.LF, CharPool.TAB));
            log.info("表信息：" + catalog + "   " + tableName);
            // 获取主键描述
            ResultSet primaryKeys = metaData.getPrimaryKeys(catalog, schema, tableName);
            // 排除部分表
            if (tableName.contains("=") || tableName.contains("$")) {
                continue;
            }

            List<String> items = StrUtil.split(tableName, CharUtil.UNDERLINE);


            boolean useTablePrefix = databaseInfo.isUseTablePrefix();
            if (items.size() == 1) {
                table.setModule("");
            } else {
                table.setModule(items.get(0));
            }


            // if (useTablePrefix) {
            //     table.setBeanName("");
            //     table.setBeanPropertyName("");
            //     table.setSimplePropertyName("");
            //     table.setApiPath("");
            // }
            // 设置模块名


            table.setName(tableName.toLowerCase());

            table.setClassPrefix(NameUtil.getClassNamePrefix(tableName, databaseInfo.isUseTablePrefix()));
            table.setClassPropertyPrefix(NameUtil.getClassPropertyNamePrefix(table.getClassPrefix()));
            table.setClassMainPrefix(StrUtil.lowerFirst(StrUtil.removePrefix(table.getClassPropertyPrefix(), table.getModule())));
            table.setApiPath(NamingCase.toSymbolCase(table.getClassPrefix(), CharUtil.SLASH));


            // 存储主键字段名
            List<String> keys = new ArrayList<>();
            while (primaryKeys.next()) {
                String keyname = primaryKeys.getString("COLUMN_NAME");
                if (keyname.toUpperCase().equals(keyname)) {
                    keyname = keyname.toLowerCase();
                }
                keys.add(keyname);
            }
            columnList = getColumnList(metaData, catalog, schema, tableName, keys, table);
            // 获取表字段信息
            table.setColumns(columnList);
            list.add(table);
        }
        return list;
    }

    /**
     * 获取表的列信息
     *
     * @author Carlos
     * @date 2019/12/26 13:42
     */
    private List<ColumnBean> getColumnList(DatabaseMetaData metaData, String catalog, String schema, String tableName, List<String> keys,
                                           TableBean table) throws SQLException {
        // 表列
        ResultSet resultSet = metaData.getColumns(catalog, schema, tableName, null);
        List<ColumnBean> columnList = new ArrayList<>();
        ColumnBean column;
        // 列名
        String columnName;
        // 属性名
        String propertyName;
        // 列类型
        String columnDbType;
        // 对应的Java数据类型
        String fullJavaType;
        // 备注
        String remarks;
        // 自增
        boolean autoIncrement;
        // 是否允许空值
        boolean nullable;
        // 字段默认值
        String columnDefault;
        // 小数位数
        int decimalDigits;
        // 列大小
        int columnSize;
        Set<String> imports = new HashSet<>();
        while (resultSet.next()) {
            column = new ColumnBean();
            columnName = resultSet.getString("COLUMN_NAME");
            autoIncrement = "YES".equals(resultSet.getString("IS_AUTOINCREMENT"));
            nullable = !"0".equals(resultSet.getString("NULLABLE"));
            columnDbType = resultSet.getString("TYPE_NAME");
            columnDefault = resultSet.getString("COLUMN_DEF");
            remarks = resultSet.getString("REMARKS");
            decimalDigits = resultSet.getInt("DECIMAL_DIGITS");
            columnSize = resultSet.getInt("COLUMN_SIZE");
            column.setAutoIncrement(autoIncrement);
            column.setNullable(nullable);
            column.setColumnName(columnName);
            column.setColumnDbType(columnDbType);
            // 获取数据库数据类型对应的Java数据类型 全包名路径
            fullJavaType = getColumnFullJavaType(columnDbType);
            imports.add(fullJavaType);
            column.setColumnType(fullJavaType);
            // 如果列备注为空，则使用字段名称作为备注名
            if (remarks == null) {
                remarks = columnName;
                // 去除备注中的换行符
            }
            column.setColumnComment(StrUtil.removeAll(remarks, CharPool.CR, CharPool.LF, CharPool.TAB));
            // 主键列处理
            if (keys.contains(columnName)) {
                column.setPrimaryKey(true);
                table.setPrimaryKey(columnName);
            } else {
                column.setPrimaryKey(false);
            }

            boolean logicDelete = checkLogicDeleteField(columnName);

            if (logicDelete) {
                // 设置表的逻辑删除字段
                table.setLogicDelete(columnName);
                column.setLogicField(logicDelete);
            }

            // 逻辑删除字段处理
            if (logicDelete || checkBooleanField(columnName)) {
                fullJavaType = "java.long.Boolean";
                // 去除数据库字段中的is_前缀， is前缀在Java类型为boolean时有时候会出错
                columnName = StrUtil.removePrefixIgnoreCase(columnName, Constant.BOOLEAN_PREFIX);
            }

            column.setJavaType(StrUtil.subAfter(fullJavaType, CharUtil.DOT, true));

            // 针对键类型字段，如id  xxx_id 字段，统一采用Serializable类型，方便进行id类型更改，提升系统兼容性
            checkIdColumn(column);
            column.setDecimalDigits(decimalDigits);
            column.setColumnsSize(columnSize);
            column.setColumnDefault(columnDefault);
            switch (databaseInfo.getNameType()) {
                case NOT_PREFIX_AND_UNDERLINE:
                    propertyName = NameUtil.underLineToCamel(columnName);
                    break;
                case NOT_PREFIX_AND_CAMEL:
                    propertyName = columnName;
                    break;
                case PREFIX_AND_UNDERLINE:
                    propertyName = NameUtil.delPrefixToCamel(columnName);
                    break;
                case PREFIX_AND_CAMEL:
                    propertyName = NameUtil.delPrefix(columnName);
                    break;
                case ALL_UPPER:
                    propertyName = NameUtil.upperToLowerAndCamel(columnName);
                    break;
                default:
                    propertyName = columnName;
            }
            // 设置数据库列名对应的属性名
            column.setPropertyName(propertyName);
            column.setPropertyNameUp(NameUtil.getPropertyNameUp(column.getPropertyName()));
            column.setCommonField(checkCommonField(columnName));
            column.setVersionField(checkVersionField(columnName));
            columnList.add(column);
        }
        table.setImports(imports);
        resultSet.close();
        return columnList;
    }

    private void checkIdColumn(ColumnBean column) {
        String columnName = column.getColumnName();
        // if (column.isPrimaryKey() || columnName.endsWith("_id")) {
        //     String className = ClassUtil.getClassName(Serializable.class, true);
        //     column.setJavaType(className);
        // }
    }

    /**
     * 检查逻辑删除字段
     *
     * @param columnName 数据库字段名
     * @return boolean
     * @author Carlos
     * @date 2021/11/22 16:22
     */
    private boolean checkLogicDeleteField(String columnName) {
        TemplateConfig.Field fields = templateConfig.getLogicDeleteFields();
        if (fields == null) {
            return false;
        }
        Set<String> name = fields.getName();
        if (CollUtil.isEmpty(name)) {
            return false;
        }
        return name.contains(columnName);
    }

    /**
     * 检查是否是boolean类型字段
     *
     * @param columnName 列名
     * @return boolean
     * @author Carlos
     * @date 2021/12/28 15:42
     */
    private boolean checkBooleanField(String columnName) {
        return columnName.startsWith(Constant.BOOLEAN_PREFIX);
    }

    /**
     * 检查字段是否是通用字段
     *
     * @param columnName 列名
     * @return boolean
     * @author Carlos
     * @date 2021/11/22 15:20
     */
    private boolean checkCommonField(String columnName) {
        TemplateConfig.Field fields = templateConfig.getCommonFields();
        if (fields == null) {
            return false;
        }
        Set<String> name = fields.getName();
        if (CollUtil.isEmpty(name)) {
            return false;
        }
        return name.contains(columnName);
    }

    /**
     * 检查字段是否是版本字段
     *
     * @param columnName 列名
     * @return boolean
     * @author Carlos
     * @date 2021/11/22 15:20
     */
    private boolean checkVersionField(String columnName) {
        TemplateConfig.Field fields = templateConfig.getVersionFields();
        if (fields == null) {
            return false;
        }
        Set<String> name = fields.getName();
        if (CollUtil.isEmpty(name)) {
            return false;
        }
        return name.contains(columnName);
    }

    /**
     * 获取数据库字段对应的Java类型
     *
     * @param columnDbType 数据库数据类型
     * @author Carlos
     * @date 2019/12/31 13:45
     */
    private String getColumnFullJavaType(String columnDbType) {
        if (databaseInfo.isUseJdk8Date()) {
            switch (columnDbType) {
                case "DATE":
                    return "java.time.LocalDate";
                case "TIME":
                    return "java.time.LocalTime ";
                case "YEAR":
                    return "java.time.Year";
                case "DATETIME":
                case "TIMESTAMP":
                    return "java.time.LocalDateTime";
                default:
            }
        }
        String s = CONVERT_MAP.get(columnDbType);
        if (StrUtil.isBlank(s)) {
            s = "java.lang.String";
        }
        return s;
    }

    /**
     * @param conn 连接对象
     * @author Carlos
     * @date 2019/12/25 17:32
     */

    public void release(Connection conn, Statement st) {
        closeSt(st);
        closeConn(conn);
    }

    public void release(Connection conn, ResultSet rs) {
        closeRs(rs);
        closeConn(conn);
    }

    private void closeRs(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeSt(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConn(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
