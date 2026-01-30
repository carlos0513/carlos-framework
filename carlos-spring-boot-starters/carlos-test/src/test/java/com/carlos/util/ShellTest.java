package com.carlos.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.SqlConnRunner;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ShellTest {


    @Test
    public void scan() {
        final String URL = "jdbc:mysql://[ip]:[port]/[databaseName]?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";

        String sourceUrl = URL.replace("[ip]", "100.127.6.146");
        sourceUrl = sourceUrl.replace("[port]", "13306");
        sourceUrl = sourceUrl.replace("[databaseName]", "carlos_bbt_xj");
        final String driver = "com.mysql.cj.jdbc.Driver";
        SimpleDataSource dataSource = new SimpleDataSource(sourceUrl, "root", "carlos@123456", driver);
        // Db.use(dataSource)
        SqlConnRunner sqlConnRunner = DbUtil.newSqlConnRunner(dataSource);
        Db db = DbUtil.use(dataSource);

        HashSet<String> columaKeys = Sets.newHashSet(
                // "%号",
                // "%电话%",
                // "%联系%",
                // "%证%",
                // "%码%",
                // "%手机%", "%座机%",
                "%信用代码%"
        );
        Set<String> conditions = columaKeys.stream().map(i -> {
            return "c.column_name like  \"" + i + "\"";
        }).collect(Collectors.toSet());


        List<Entity> colums = null;
        try {
            String ss = "select t.table_name, t.table_label, c.column_code, c.column_name from bbt_form_table t left join bbt_form_column  c on c.form_table_id = t.id where " + StrUtil.join(" or ", conditions);
            colums = db.query(ss);
            System.out.println(ss);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Map<String, List<Entity>> tables = colums.stream().collect(Collectors.groupingBy(i -> i.getStr("table_name")));

        final String sql = "UPDATE {table} SET {col} =IF(CHAR_LENGTH( {col} ) > 6,CONCAT( SUBSTRING( {col}, 1, CHAR_LENGTH( {col} ) - 6 ), REPEAT ( '*', 6 ) ),{col} ) WHERE {col} <> '' OR {col} IS NULL;";
        tables.forEach((tableName, fields) -> {

            fields.forEach(i -> {
                String tableLabel = i.getStr("table_label");
                String columnCode = i.getStr("column_code");
                String columnName = i.getStr("column_name");
                Map<String, String> map = new HashMap<>(4);
                map.put("table", tableName);
                map.put("col", columnCode);
                String exesql = StrUtil.format(sql, map);
                try {
                    int execute = db.execute(exesql);
                    System.out.println("修改”" + tableLabel + "“     " + columnName);
                    System.out.println("执行成功 “" + execute + "” 条：" + exesql);
                } catch (Exception e) {
                    System.out.println("执行失败：" + exesql);
                    e.printStackTrace();
                }
            });

        });


    }

    @Test
    public void test() {
        String s = "SELECT\n" +
                "\tqx_0 AS 所属区县,count(1) 需求总数,\n" +
                "\tsum( CASE WHEN shfwch_6 = '是' THEN 1 ELSE 0 END ) AS '已完成' ,\n" +
                "\tsum( CASE WHEN shfwch_6 = '否' THEN 1 ELSE 0 END ) AS '未完成' \n" +
                "FROM\n" +
                "\tdynamic_yyxbbtdkgngdtz_20240705093525 \n" +
                "GROUP BY\n" +
                "\tqx_0";
        s = StrUtil.replace(s, StrUtil.TAB, StrUtil.SPACE);
        s = StrUtil.replace(s, StrUtil.LF, StrUtil.SPACE);
        getTotalSQL(s);
    }

    public static String getTotalSQL(String resultedSql) {
        System.out.println(resultedSql);
        String resultedSqlTotal = resultedSql.replaceAll("SELECT.*FROM", "SELECT count(*) FROM");
        // 计算总数，需要去除GROUP BY语句
        System.out.println(resultedSqlTotal);
        final String groupByPattern = "GROUP\\s+BY\\s+.+?(?=\\s+HAVING|$)";
        final String havingPattern = "HAVING\\s+.+?(?=\\s+ORDER\\s+BY|$)";
        resultedSqlTotal = resultedSqlTotal.replaceAll(groupByPattern, "");
        resultedSqlTotal = resultedSqlTotal.replaceAll(havingPattern, "");
        System.out.println(resultedSql);
        return resultedSqlTotal;
    }

    @Test
    public void ocr() {
        final String url = "http://100.127.5.125:8501/ocr";


        // byte[] imageData = FileUtil.readBytes(new File(""));
        HttpRequest post = HttpUtil.createPost(url);
        post.form("image", new File("D:\\ocr.jpg"));
        HttpResponse execute = post.execute();
        System.out.println(JSONUtil.toJsonPrettyStr(execute.body()));
    }

    @Test
    public void ocrUrl() {
        final String url = "http://100.127.5.125:8501/ocr";
        final String imageURL = "https://pic2.zhimg.com/v2-399984446e9cd7a179370af2a7f8baad_r.jpg";

        Map<String, Object> map = new HashMap<>(4);
        // HttpUtil.post()
        //
        // System.out.println(JSONUtil.toJsonPrettyStr(execute.body()));
    }

}
