package com.yunjin;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;


@Slf4j
public class ScDataHandle {

    private final static Table<String, String, String> DICT = HashBasedTable.create();

    public void init() {
        // 向Table中添加数据
        DICT.put("EXT_SEX", "1", "男");
        DICT.put("EXT_SEX", "2", "女");
        DICT.put("EXT_SEX", "3", "未知");
        DICT.put("T_TYPE", "VISIT", "大走访");
        DICT.put("T_TYPE", "INVESTIGATION", "大调查");
        DICT.put("T_TYPE", "COLLECT", "大征集");
        DICT.put("T_TYPE", "SORTING", "大梳理");
        DICT.put("IS_APPEALER_FEEDBACK", "1", "是");
        DICT.put("IS_APPEALER_FEEDBACK", "0", "否");
        DICT.put("IS_RESOLVE", "1", "是");
        DICT.put("IS_RESOLVE", "0", "否");
        DICT.put("IS_RESOLVE", "2", "未知");

        String json = ResourceUtil.readUtf8Str("appeals.json");
        List<AppealsLevel> jsonList = JSONUtil.toList(json, AppealsLevel.class);
        recursionDict("REG_MAIN_APPEAL_ONE", jsonList);
        recursionDict("REG_MAIN_APPEAL_TWO", jsonList);
        recursionDict("REG_MAIN_APPEAL_THREE", jsonList);
    }

    private void recursionDict(String key, List<AppealsLevel> jsonList) {
        for (AppealsLevel item : jsonList) {
            String value = item.getValue();
            String label = item.getLabel();
            DICT.put(key, value, label);
            List<AppealsLevel> children = item.getChildren();
            if (children != null) {
                recursionDict(key, children);
            }
        }

    }

    @NoArgsConstructor
    @Data
    public class AppealsLevel {

        @JsonProperty("value")
        private String value;
        @JsonProperty("label")
        private String label;
        @JsonProperty("children")
        private List<AppealsLevel> children;
    }


    @Test
    public void createUser() {

        init();
        File file = new File("D:\\WeChat\\WeChat Files\\wxid_ynwk6vd57f8i22\\FileStorage\\File\\2024-11\\成都市温江区.xlsx");
        // File file = new File("D:\\WeChat\\WeChat Files\\wxid_ynwk6vd57f8i22\\FileStorage\\File\\2024-11\\绵阳市江油市.xlsx");
        ExcelReader reader = ExcelUtil.getReader(file);
        List<Map> data = reader.readAll(Map.class);


        Map<String, String> map = new HashMap<>(4);
        map.put("EXT_AGE", "年龄");
        map.put("EXT_IDCARD", "身份证号");
        map.put("EXT_NAME", "姓名");
        map.put("EXT_SEX", "性别");
        map.put("FEEDBACK", "反馈情况描述");
        map.put("FEEDBACK_TIME", "反馈时间");
        map.put("ID", "主键");
        map.put("IS_APPEALER_FEEDBACK", "是否申诉人反馈");
        map.put("IS_RESOLVE", "是否解决");
        map.put("PHONE", "手机号");
        map.put("REG_APPEAL_CONTENT", "诉求详情");
        map.put("REG_MAIN_APPEAL_ONE", "注册主要申诉一");
        map.put("REG_MAIN_APPEAL_THREE", "注册主要申诉三");
        map.put("REG_MAIN_APPEAL_TWO", "注册主要申诉二");
        map.put("RESOLVE_SITUATION", "解决情况");
        map.put("RESOLVE_TIME", "解决时间");
        map.put("STRU_PATH", "组织机构路径");
        map.put("T_TYPE", "类型");
        map.put("VISIT_TIME", "访问时间");

        // 注册字段处理器
        Map<String, FieldHandler> handlerMap = new HashMap<>(4);
        // handlerMap.put("EXT_AGE", "年龄");
        // handlerMap.put("EXT_IDCARD", "身份证号");
        // handlerMap.put("EXT_NAME", "姓名");
        handlerMap.put("EXT_SEX", new DictHandler());
        // handlerMap.put("FEEDBACK", "反馈情况描述");
        handlerMap.put("FEEDBACK_TIME", new TimeHandler());
        // handlerMap.put("ID", "主键");
        handlerMap.put("IS_APPEALER_FEEDBACK", new DictHandler());
        handlerMap.put("IS_RESOLVE", new DictHandler());
        // handlerMap.put("PHONE", "手机号");
        // handlerMap.put("REG_APPEAL_CONTENT", "诉求详情");
        handlerMap.put("REG_MAIN_APPEAL_ONE", new DictHandler());
        handlerMap.put("REG_MAIN_APPEAL_THREE", new DictHandler());
        handlerMap.put("REG_MAIN_APPEAL_TWO", new DictHandler());
        // handlerMap.put("RESOLVE_SITUATION", "解决情况");
        handlerMap.put("RESOLVE_TIME", new TimeHandler());
        // handlerMap.put("STRU_PATH", "组织机构路径");
        handlerMap.put("T_TYPE", new DictHandler());
        handlerMap.put("VISIT_TIME", new TimeHandler());

        List<Map<String, Object>> list = new ArrayList<>();
        for (Map item : data) {
            Map<String, Object> i = Maps.newHashMap(item);
            Map<String, Object> d = new HashMap<>(4);
            for (Map.Entry<String, Object> entry : i.entrySet()) {
                String oldKey = entry.getKey();
                String key = entry.getKey();
                String s = map.get(key);
                if (StrUtil.isNotBlank(s)) {
                    key = s;
                }
                // 值处理
                Object value = entry.getValue();
                if (value != null) {
                    FieldHandler handler = handlerMap.get(oldKey);
                    if (handler != null) {
                        value = handler.handle(entry);
                    }
                }
                d.put(key, value);
            }
            list.add(d);
        }

        String title = FileUtil.mainName(file) + "_" + System.currentTimeMillis();
        ExcelWriter writer = ExcelUtil.getWriter("D:\\data\\" + title + ".xlsx");
        // ExcelWriter sheet1 = writer.setSheet(title);
        writer.write(list);
        writer.flush();
    }


    public interface FieldHandler {
        String handle(Map.Entry<String, Object> entry);
    }

    public static class TimeHandler implements FieldHandler {
        @Override
        public String handle(Map.Entry<String, Object> entry) {
            String v = (String) entry.getValue();
            if (StrUtil.isBlank(v)) {
                return null;
            }
            v = StrUtil.trim(v);
            v = v.replace("'", "");
            return DateUtil.formatDateTime(new Date(Long.parseLong(v)));
        }
    }

    public static class DictHandler implements FieldHandler {
        @Override
        public String handle(Map.Entry<String, Object> entry) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String v = value.toString();
            if (StrUtil.isBlank(v)) {
                return null;
            }
            v = StrUtil.trim(v);
            String s = DICT.get(key, v);
            if (StrUtil.isBlank(s)) {
                log.warn("未匹配到字典值:key:{}, value:{}", key, v);
                return v;
            }
            return s;

        }
    }

}