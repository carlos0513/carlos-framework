package com.yunjin.magicapi.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ssssssss.magicapi.core.context.RequestEntity;
import org.ssssssss.magicapi.core.interceptor.ResultProvider;
import org.ssssssss.magicapi.modules.db.model.Page;

/**
 * <p>
 * 自定义响应结果
 * </p>
 *
 * @author Carlos
 * @date 2023/8/20 14:41
 */
public class YunjinResultProvider implements ResultProvider {

    /**
     * 定义返回结果，默认返回JsonBean
     */
    @Override
    public Object buildResult(RequestEntity requestEntity, int code, String message, Object data) {
        // 如果对分页格式有要求的话，可以对data的类型进行判断，进而返回不同的格式
        return new HashMap<String, Object>() {
            {
                put("status", code);
                put("msg", message);
                put("body", data);
            }
        };
    }


    /**
     * 定义分页返回结果，该项会被封装在Json结果内， 此方法可以不覆盖，默认返回PageResult
     */
    @Override
    public Object buildPageResult(RequestEntity requestEntity, Page page, long total, List<Map<String, Object>> data) {
        return new HashMap<String, Object>() {
            {
                put("total", total);
                put("list", data);
            }
        };
    }


}
