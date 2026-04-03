package com.carlos.audit.clickhouse;

import com.clickhouse.client.*;
import com.clickhouse.data.ClickHouseFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * ClickHouse 查询客户端
 *
 * <p>封装 ClickHouse 查询操作，提供常用的查询方法</p>
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClickHouseQueryClient {

    private final ClickHouseClient clickHouseClient;
    private final ClickHouseNode clickHouseNode;

    /**
     * 默认查询超时时间（秒）
     */
    private static final int DEFAULT_QUERY_TIMEOUT = 30;

    /**
     * 执行查询并返回结果列表
     *
     * @param sql       SQL 语句
     * @param rowMapper 行映射器
     * @param <T>       返回类型
     * @return 结果列表
     */
    public <T> List<T> query(String sql, Function<ClickHouseRecord, T> rowMapper) {
        return query(sql, rowMapper, DEFAULT_QUERY_TIMEOUT);
    }

    /**
     * 执行查询并返回结果列表（带超时）
     *
     * @param sql       SQL 语句
     * @param rowMapper 行映射器
     * @param timeout   超时时间（秒）
     * @param <T>       返回类型
     * @return 结果列表
     */
    public <T> List<T> query(String sql, Function<ClickHouseRecord, T> rowMapper, int timeout) {
        log.debug("执行 ClickHouse 查询: {}", sql);

        List<T> results = new ArrayList<>();

        try {
            ClickHouseRequest<?> request = clickHouseClient.read(clickHouseNode)
                .query(sql)
                .format(ClickHouseFormat.RowBinaryWithNamesAndTypes);

            try (ClickHouseResponse response = request.execute().get(timeout, TimeUnit.SECONDS)) {
                for (ClickHouseRecord record : response.records()) {
                    T result = rowMapper.apply(record);
                    if (result != null) {
                        results.add(result);
                    }
                }
            }

            log.debug("查询完成，返回 {} 条记录", results.size());

        } catch (Exception e) {
            log.error("ClickHouse 查询失败: {}", sql, e);
            throw new RuntimeException("ClickHouse 查询失败: " + e.getMessage(), e);
        }

        return results;
    }

    /**
     * 执行查询并返回单条结果
     *
     * @param sql       SQL 语句
     * @param rowMapper 行映射器
     * @param <T>       返回类型
     * @return 单条结果，如果没有则返回 null
     */
    public <T> T queryOne(String sql, Function<ClickHouseRecord, T> rowMapper) {
        List<T> results = query(sql, rowMapper, 1);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * 执行计数查询
     *
     * @param sql SQL 语句
     * @return 计数结果
     */
    public long count(String sql) {
        log.debug("执行 ClickHouse 计数查询: {}", sql);

        try {
            ClickHouseRequest<?> request = clickHouseClient.read(clickHouseNode)
                .query(sql)
                .format(ClickHouseFormat.RowBinaryWithNamesAndTypes);

            try (ClickHouseResponse response = request.execute().get(DEFAULT_QUERY_TIMEOUT, TimeUnit.SECONDS)) {
                for (ClickHouseRecord record : response.records()) {
                    return record.getValue(0).asLong();
                }
            }

        } catch (Exception e) {
            log.error("ClickHouse 计数查询失败: {}", sql, e);
            throw new RuntimeException("ClickHouse 计数查询失败: " + e.getMessage(), e);
        }

        return 0L;
    }

    /**
     * 执行分页查询
     *
     * @param countSql  计数 SQL
     * @param querySql  查询 SQL
     * @param rowMapper 行映射器
     * @param pageNum   页码（从1开始）
     * @param pageSize  页大小
     * @param <T>       返回类型
     * @return 分页结果
     */
    public <T> PageResult<T> queryPage(String countSql, String querySql,
                                        Function<ClickHouseRecord, T> rowMapper,
                                        int pageNum, int pageSize) {
        log.debug("执行分页查询: pageNum={}, pageSize={}", pageNum, pageSize);

        // 查询总数
        long total = count(countSql);

        // 查询数据
        List<T> records = query(querySql, rowMapper);

        // 构建分页结果
        PageResult<T> result = new PageResult<>();
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setRecords(records);
        result.setPages((int) Math.ceil((double) total / pageSize));

        return result;
    }

    /**
     * 执行原生 SQL（用于复杂查询）
     *
     * @param sql SQL 语句
     * @return ClickHouseResponse
     */
    public ClickHouseResponse executeRaw(String sql) {
        log.debug("执行原生 SQL: {}", sql);

        try {
            ClickHouseRequest<?> request = clickHouseClient.read(clickHouseNode)
                .query(sql)
                .format(ClickHouseFormat.RowBinaryWithNamesAndTypes);

            return request.execute().get(DEFAULT_QUERY_TIMEOUT, TimeUnit.SECONDS);

        } catch (Exception e) {
            log.error("ClickHouse 执行失败: {}", sql, e);
            throw new RuntimeException("ClickHouse 执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 测试连接
     *
     * @return true-连接正常
     */
    public boolean testConnection() {
        try {
            ClickHouseRequest<?> request = clickHouseClient.read(clickHouseNode)
                .query("SELECT 1")
                .format(ClickHouseFormat.RowBinaryWithNamesAndTypes);

            try (ClickHouseResponse response = request.execute().get(5, TimeUnit.SECONDS)) {
                return true;
            }
        } catch (Exception e) {
            log.error("ClickHouse 连接测试失败", e);
            return false;
        }
    }

    /**
     * 分页结果内部类
     *
     * @param <T> 数据类型
     */
    public static class PageResult<T> {
        private long total;
        private int pageNum;
        private int pageSize;
        private int pages;
        private List<T> records;

        // Getters and Setters
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public int getPageNum() { return pageNum; }
        public void setPageNum(int pageNum) { this.pageNum = pageNum; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
        public int getPages() { return pages; }
        public void setPages(int pages) { this.pages = pages; }
        public List<T> getRecords() { return records; }
        public void setRecords(List<T> records) { this.records = records; }
    }
}
