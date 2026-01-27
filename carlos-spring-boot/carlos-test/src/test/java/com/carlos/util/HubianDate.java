package com.carlos.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class HubianDate {

    @Test
    public void mimeType() {
        String mimeType = FileUtil.getMimeType("abc.pdf");
        System.out.println(mimeType);
    }

    @Test
    public void createUser() {

        HashSet<String> tableIds = Sets.newHashSet("37955813318149", "38337522142213", "37760247288581", "38663922755589");

        final String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOjEzNTA4MDc1MDU4LCJUZW5hbnRJZCI6MTMwMDAwMDAwMDAwMSwiQWNjb3VudCI6IjEzNTA4MDc1MDU4IiwiUmVhbE5hbWUiOiLnjovmn6_lj4giLCJQaG9uZSI6IjEzNTA4MDc1MDU4IiwiQWNjb3VudFR5cGUiOjk5OSwiUmVnaW9uSWQiOjMzNzgyODQxNDgwNzczLCJSZWdpb25OYW1lIjpudWxsLCJPcmdJZCI6OTMyMjEsIk9yZ05hbWUiOiLov5DooYzosIPluqbnp5EiLCJPcmdUeXBlIjpudWxsLCJQb3NJZHMiOiIzNDI5NTEzMTQ0NTUwOSIsIlNpZ25hdHVyZSI6bnVsbCwiRXh0T3JnSWRzIjoiIiwiaWF0IjoxNzMzMjE0MTEyLCJuYmYiOjE3MzMyMTQxMTIsImV4cCI6MTczMzI1NzMxMiwiaXNzIjoiSHVpQmlhbiIsImF1ZCI6Ikh1aUJpYW4ifQ.dxXeqi1Xd6gsB1uvqO0gyWhnjWo5zQkQYhyH_BO2IQ4";


        Set<String> dms = Sets.newHashSet("tyshxydm0", "shxydm1", "tyshxydm0", "tongyishehuixinyongdaima");
        Set<String> dmIds = new HashSet<>();
        for (String tableId : tableIds) {
            HttpRequest tGet = HttpUtil.createGet("http://10.1.191.50/api/diyTable/" + tableId);
            tGet.header("authorization", token);
            HttpResponse response = tGet.execute();
            TableInfo tableInfo = JSONUtil.toBean(response.body(), TableInfo.class);
            TableInfo.ResultDTO result = tableInfo.getResult();
            String title = result.getTitle();
            List<TableInfo.ResultDTO.ColumnsDTO> columns = tableInfo.getResult().getColumns();
            Map<String, String> fieldMap = columns.stream().collect(Collectors.toMap(TableInfo.ResultDTO.ColumnsDTO::getColumn, TableInfo.ResultDTO.ColumnsDTO::getName));


            // 查询分页数据
            HttpRequest post = HttpUtil.createPost("http://10.1.191.50/api/diyData/page");
            post.header("authorization", token);
            Map<String, Object> param = new HashMap<>(4);
            param.put("tableId", tableId);
            param.put("page", 1);
            param.put("pageSize", 10000000);
            param.put("conditions", Collections.emptyList());
            post.body(JSONUtil.toJsonStr(param));
            HttpResponse execute = post.execute();
            DataPage page = JSONUtil.toBean(execute.body(), DataPage.class);
            List<Map<String, Object>> items = page.getResult().getItems();

            List<Map<String, Object>> list = new ArrayList<>();
            for (Map<String, Object> item : items) {
                String createTime = (String) item.get("CreateTime");
                // 使用时间过滤今日新增数据
                if (StrUtil.isNotBlank(createTime)) {
                    DateTime parse = DateUtil.parse(createTime, DatePattern.NORM_DATETIME_PATTERN);
                    long between = parse.between(new Date(), DateUnit.DAY);
                    if (between >= 1) {
                        continue;
                    }
                }


                Map<String, Object> map = new HashMap<>(4);
                for (Map.Entry<String, Object> entry : item.entrySet()) {
                    Object value = entry.getValue();
                    String key = entry.getKey();
                    if (dms.contains(key)) {
                        dmIds.add((String) value);
                    }
                    String s = fieldMap.get(key);
                    if (StrUtil.isNotBlank(s)) {
                        key = s;
                    }
                    map.put(key, value);


                }
                list.add(map);
            }

            ExcelWriter writer = ExcelUtil.getWriter("D:\\data\\" + title + System.currentTimeMillis() + ".xlsx");
            writer.write(list);
            writer.flush();
        }

        DataGet.export(dmIds);
    }


    @NoArgsConstructor
    @Data
    public static class DataPage {

        @JsonProperty("code")
        private Integer code;
        @JsonProperty("type")
        private String type;
        @JsonProperty("message")
        private String message;
        @JsonProperty("result")
        private ResultDTO result;
        @JsonProperty("extras")
        private Object extras;
        @JsonProperty("time")
        private String time;

        @NoArgsConstructor
        @Data
        public static class ResultDTO {
            @JsonProperty("page")
            private Integer page;
            @JsonProperty("pageSize")
            private Integer pageSize;
            @JsonProperty("total")
            private Integer total;
            @JsonProperty("totalPages")
            private Integer totalPages;
            @JsonProperty("items")
            private List<Map<String, Object>> items;
            @JsonProperty("hasPrevPage")
            private Boolean hasPrevPage;
            @JsonProperty("hasNextPage")
            private Boolean hasNextPage;

        }
    }


    @NoArgsConstructor
    @Data
    public static class TableInfo {

        @JsonProperty("code")
        private Integer code;
        @JsonProperty("type")
        private String type;
        @JsonProperty("message")
        private String message;
        @JsonProperty("result")
        private ResultDTO result;
        @JsonProperty("extras")
        private Object extras;
        @JsonProperty("time")
        private String time;

        @NoArgsConstructor
        @Data
        public static class ResultDTO {
            @JsonProperty("sysFlowId")
            private Object sysFlowId;
            @JsonProperty("sysFlowName")
            private Object sysFlowName;
            @JsonProperty("sort")
            private Integer sort;
            @JsonProperty("orgIds")
            private List<String> orgIds;
            @JsonProperty("category")
            private Integer category;
            @JsonProperty("reportType")
            private Integer reportType;
            @JsonProperty("pathUrl")
            private Object pathUrl;
            @JsonProperty("shownon")
            private Integer shownon;
            @JsonProperty("title")
            private String title;
            @JsonProperty("tableName")
            private String tableName;
            @JsonProperty("tableJson")
            private String tableJson;
            @JsonProperty("reportSql")
            private Object reportSql;
            @JsonProperty("reportCountSql")
            private Object reportCountSql;
            @JsonProperty("desensitizeColumns")
            private Object desensitizeColumns;
            @JsonProperty("autoTime")
            private Object autoTime;
            @JsonProperty("getDataing")
            private Boolean getDataing;
            @JsonProperty("getDataTime")
            private Object getDataTime;
            @JsonProperty("xfReportId")
            private Object xfReportId;
            @JsonProperty("frequency")
            private Integer frequency;
            @JsonProperty("isEnable")
            private Boolean isEnable;
            @JsonProperty("isHide")
            private Boolean isHide;
            @JsonProperty("isShowPwd")
            private Boolean isShowPwd;
            @JsonProperty("formPwd")
            private Object formPwd;
            @JsonProperty("dataCount")
            private Integer dataCount;
            @JsonProperty("lastUpdateTime")
            private Object lastUpdateTime;
            @JsonProperty("fillingDescription")
            private String fillingDescription;
            @JsonProperty("description")
            private Object description;
            @JsonProperty("isFieldStore")
            private Boolean isFieldStore;
            @JsonProperty("isBasic")
            private Boolean isBasic;
            @JsonProperty("status")
            private Boolean status;
            @JsonProperty("columns")
            private List<ColumnsDTO> columns;
            @JsonProperty("reportColumn")
            private List<?> reportColumn;
            @JsonProperty("isHeader")
            private Boolean isHeader;
            @JsonProperty("headerJson")
            private String headerJson;
            @JsonProperty("classifyId")
            private String classifyId;
            @JsonProperty("fieldStoreClassifyId")
            private Object fieldStoreClassifyId;
            @JsonProperty("highlightJson")
            private String highlightJson;
            @JsonProperty("authApplyFlowId")
            private Object authApplyFlowId;
            @JsonProperty("myAuth")
            private Object myAuth;
            @JsonProperty("isQRCode")
            private Boolean isQRCode;
            @JsonProperty("scanType")
            private Object scanType;
            @JsonProperty("isAuditing")
            private Object isAuditing;
            @JsonProperty("auditStatus")
            private Object auditStatus;
            @JsonProperty("tableRoleByUpdateTimeType")
            private Integer tableRoleByUpdateTimeType;
            @JsonProperty("tableRoleByStartTime")
            private String tableRoleByStartTime;
            @JsonProperty("tableRoleByEndTime")
            private String tableRoleByEndTime;
            @JsonProperty("tableRoleByCreateUser")
            private Boolean tableRoleByCreateUser;
            @JsonProperty("tableRoleByDataInputUser")
            private Boolean tableRoleByDataInputUser;
            @JsonProperty("isTaskCloneTable")
            private Boolean isTaskCloneTable;
            @JsonProperty("isCollect")
            private Boolean isCollect;
            @JsonProperty("classifiedLevel")
            private Object classifiedLevel;
            @JsonProperty("fillLevel")
            private String fillLevel;
            @JsonProperty("inTask")
            private Boolean inTask;
            @JsonProperty("diyTableAffiliation")
            private Integer diyTableAffiliation;
            @JsonProperty("registerFlowId")
            private Object registerFlowId;
            @JsonProperty("createTime")
            private String createTime;
            @JsonProperty("updateTime")
            private Object updateTime;
            @JsonProperty("createUserId")
            private String createUserId;
            @JsonProperty("createUserName")
            private String createUserName;
            @JsonProperty("updateUserId")
            private Object updateUserId;
            @JsonProperty("updateUserName")
            private Object updateUserName;
            @JsonProperty("isDelete")
            private Boolean isDelete;
            @JsonProperty("id")
            private String id;

            @NoArgsConstructor
            @Data
            public static class ColumnsDTO {
                @JsonProperty("tableId")
                private String tableId;
                @JsonProperty("tableName")
                private String tableName;
                @JsonProperty("type")
                private Integer type;
                @JsonProperty("column")
                private String column;
                @JsonProperty("name")
                private String name;
                @JsonProperty("isPrimary")
                private Boolean isPrimary;
                @JsonProperty("isNullable")
                private Boolean isNullable;
                @JsonProperty("toTableId")
                private Object toTableId;
                @JsonProperty("toTableType")
                private Integer toTableType;
                @JsonProperty("dataType")
                private String dataType;
                @JsonProperty("length")
                private Integer length;
                @JsonProperty("digits")
                private Integer digits;
                @JsonProperty("sort")
                private Integer sort;
                @JsonProperty("columnJson")
                private String columnJson;
                @JsonProperty("isDesensitize")
                private Boolean isDesensitize;
                @JsonProperty("isEncryption")
                private Boolean isEncryption;
                @JsonProperty("isAutoInupt")
                private Boolean isAutoInupt;
                @JsonProperty("isDataLimit")
                private Boolean isDataLimit;
                @JsonProperty("isField")
                private Boolean isField;
                @JsonProperty("isUnique")
                private Boolean isUnique;
                @JsonProperty("isFreeze")
                private Boolean isFreeze;
                @JsonProperty("isLimits")
                private Boolean isLimits;
                @JsonProperty("isRequired")
                private Boolean isRequired;
                @JsonProperty("isRole")
                private Boolean isRole;
                @JsonProperty("fieldId")
                private Object fieldId;
                @JsonProperty("isFieldStore")
                private Boolean isFieldStore;
                @JsonProperty("createTime")
                private String createTime;
                @JsonProperty("updateTime")
                private String updateTime;
                @JsonProperty("createUserId")
                private String createUserId;
                @JsonProperty("createUserName")
                private String createUserName;
                @JsonProperty("updateUserId")
                private String updateUserId;
                @JsonProperty("updateUserName")
                private String updateUserName;
                @JsonProperty("isDelete")
                private Boolean isDelete;
                @JsonProperty("id")
                private String id;
            }
        }
    }

}