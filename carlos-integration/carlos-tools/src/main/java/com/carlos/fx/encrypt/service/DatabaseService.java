package com.carlos.fx.encrypt.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.meta.MetaUtil;
import com.carlos.fx.encrypt.config.DatabaseInfo;
import lombok.extern.slf4j.Slf4j;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public DatabaseInfo getDatabaseInfo() {
        return databaseInfo;
    }

    public final DatabaseInfo databaseInfo;

    public DatabaseService(DatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
    }


    public List<String> getColumns(String tableName) {
        String[] columnNames = MetaUtil.getColumnNames(databaseInfo.getDataSource(), databaseInfo.getSelectTable());
        return Arrays.asList(columnNames);
    }

    /**
     * 当前数据库中具有哪些数据库(当前操作连接数据库后将数据库进行了关闭)
     *
     * @return List<String> 返回数据库中的表名列表
     * @author Carlos
     * @date 2019/4/14 0:05
     */
    public List<String> getSchemas() throws SQLException, ClassNotFoundException {

        DatabaseMetaData metaData = databaseInfo.getMetaData();
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
        return list;
    }


    public List<String> getTables() {
        databaseInfo.setUrl(databaseInfo.buildUrl());
        DatabaseMetaData metaData = databaseInfo.getMetaData();
        List<String> tables = MetaUtil.getTables(databaseInfo.getDataSource());
        return tables;
    }

    public void createTable(String tableName) {


    }
}
