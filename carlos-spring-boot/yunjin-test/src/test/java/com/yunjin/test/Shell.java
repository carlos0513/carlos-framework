package com.yunjin.test;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Shell {


    @Test
    public void createDepartment() {
        HashSet<String> items = Sets.newHashSet(
                "住建交局",
                "市场监管局",
                "综合执法局",
                "医保局",
                "发改局",
                "人社局",
                "组织部",
                "卫健委",
                "投促局",
                "智慧治理中心",
                "区社治委",
                "教育局",
                "城市更新局",
                "统计局",
                "网信办",
                "商务局",
                "民政局",
                "卫健局",
                "消防大队",
                "司法局",
                "东郊艺术区管委会",
                "国资和金融局",
                "残联",
                "区公安分局",
                "区税务局",
                "规划和自然资源局",
                "行政审批局",
                "新经济和科技局",
                "农业和水务局",
                "区生态环境局",
                "治理中心",
                "区政法委",
                "区总值班室",
                "信访局",
                "应急局",
                "审计局",
                "退役军人局",
                "区发改局",
                "文体旅局",
                "区地志办",
                "城东供电公司"

        );
        for (String dept : items) {
            HttpRequest post = HttpRequest.post(
                    "https://bbt.pdqcyzx.cn/mode-pro/bbt-api/department");
            Map<String, Object> map = new HashMap<>(4);
            map.put("parentId", "20230529191058176091845021561272");
            map.put("address", dept);
            map.put("deptName", dept);
            post.body(JSONUtil.toJsonStr(map));
            post.header("Authorization", "232a1757-1868-459f-9381-0c0917c4054c");
            HttpResponse response = post.execute();
            System.out.println(response.body());
        }


    }
}