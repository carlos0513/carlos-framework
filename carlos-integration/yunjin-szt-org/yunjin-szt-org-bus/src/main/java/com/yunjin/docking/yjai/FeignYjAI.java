package com.yunjin.docking.yjai;

import com.yunjin.docking.yjai.param.YjAITextParam;
import com.yunjin.docking.yjai.result.YjAIResult;
import com.yunjin.docking.yjai.result.YjAITextResultContent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

/**
 * <p>
 * 元景大模型接口
 * </p>
 *
 * @author Carlos
 * @date 2022-1-17 13:46:52
 */
public interface FeignYjAI {


    /**
     * 文本处理
     *
     * @param param   请求参数
     * @param headers 请求头信息
     * @return com.yunjin.docking.yjai.result.YjAIResult<com.yunjin.docking.yjai.result.YjAITextResultContent>
     * @author Carlos
     * @date 2024/4/16 11:02
     */
    @PostMapping("/sidp/v1/text")
    YjAIResult<YjAITextResultContent> text(@RequestBody YjAITextParam param, @RequestHeader Map<String, String> headers);


}
