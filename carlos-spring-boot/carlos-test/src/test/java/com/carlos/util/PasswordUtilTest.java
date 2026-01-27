package com.carlos.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.hankcs.hanlp.HanLP;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

class PasswordUtilTest {

    @Test
    void randomPassword() {
        Map<String, String> map = new HashMap<>(4);
        map.put("busAdd", "注册地址");
        map.put("currencyKind", "货币类型");
        map.put("regCapital", "注册资本");
        map.put("dishonest", "是否严重违法失信");
        map.put("violateLaw", "是否违法违规");
        map.put("companyName", "企业名称");
        map.put("validStart", "有效期开始");
        map.put("adminPunish", "是否受到行政处罚");
        map.put("uscc", "社会统一信用代码");
        map.put("partDate", "分区日期");
        map.put("prinType", "主体类型");
        map.put("regMngName", "登记管理部门");
        map.put("estabDate", "成立日期");
        map.put("orgType", "机构类型");
        map.put("vaildEnd", "有效期结束");
        map.put("lawSuit", "是否涉案涉诉");
        map.put("abnormalOp", "是否异常经营");
        map.put("lar", "法定代表人");
        map.put("isTax", "是否纳税");
        map.put("busScope", "经营范围");
        String params = HttpUtil.toParams(map);
        System.out.println(params);

        HttpRequest post = HttpUtil.createPost("100.127.0.63:30103/bbt-api/city/task/finishedRefluxData?taskId=2025062412231937365996399730688");
        HttpResponse execute = post.execute();
        System.out.println(execute.body());
    }


    @Test
    void hanlp() {
        final String content = "刘德华演唱会现场，由于天气原因，提前撤场，导致现场人群情绪高涨，需要派遣公安部门进行秩序维持，防止发生踩踏、斗殴、抢劫等事件";
        List<String> keywordList = HanLP.extractKeyword(content, 3);
        System.out.println(JSONUtil.toJsonPrettyStr(keywordList));
    }

    @Test
    void ruanzhu() {
        File end = FileUtil.newFile("D:\\sipu.txt");
        // ThreadPoolExecutor pool = ExecutorUtil.POOL;

        FileUtil.walkFiles(new File("D:\\ide_project\\census-backend"), new Consumer<File>() {
            @Override
            public void accept(File file) {
                // pool.execute(()->{
                if (file.isDirectory()) {
                    return;
                }
                if (!FileUtil.extName(file).equals("java")) {
                    return;
                }

                List<String> list = new LinkedList<>();
                List<String> strings = FileUtil.readUtf8Lines(file);
                for (String string : strings) {
                    String trim = StrUtil.trim(string);
                    if (StrUtil.isBlank(trim)) {
                        continue;
                    }
                    if (trim.startsWith("package")) {
                        continue;
                    }
                    if (trim.startsWith("/*")) {
                        continue;
                    }
                    if (trim.startsWith("*")) {
                        continue;
                    }
                    list.add(string);
                }
                FileUtil.appendUtf8Lines(list, end);
                // });
            }
        });


    }

    @Test
    void sipu() {
        File end = FileUtil.newFile("D:\\sipu_web.txt");
        // ThreadPoolExecutor pool = ExecutorUtil.POOL;

        FileUtil.walkFiles(new File("D:\\ide_project\\census-web\\src"), new Consumer<File>() {
            @Override
            public void accept(File file) {
                // pool.execute(()->{
                if (file.isDirectory()) {
                    return;
                }
                // if (!FileUtil.extName(file).equals("java")) {
                //     return;
                // }

                List<String> list = new LinkedList<>();
                List<String> strings = FileUtil.readUtf8Lines(file);
                for (String string : strings) {
                    String trim = StrUtil.trim(string);
                    if (StrUtil.isBlank(trim)) {
                        continue;
                    }
                    if (trim.startsWith("//")) {
                        continue;
                    }
                    if (trim.startsWith("/*")) {
                        continue;
                    }
                    if (trim.startsWith("*")) {
                        continue;
                    }
                    if (trim.startsWith("<!--")) {
                        continue;
                    }
                    list.add(string);
                }
                FileUtil.appendUtf8Lines(list, end);
                // });
            }
        });


    }
}