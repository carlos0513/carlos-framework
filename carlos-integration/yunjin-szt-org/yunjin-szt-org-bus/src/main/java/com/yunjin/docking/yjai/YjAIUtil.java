package com.yunjin.docking.yjai;

import cn.hutool.core.util.StrUtil;
import com.yunjin.docking.yjai.exception.DockingYjAIException;
import com.yunjin.socket.WebSocketYjAIQACallback;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 元景大模型工具类
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 9:39
 */
@Slf4j
public class YjAIUtil {

    private static YjAIService yjAIService;


    public YjAIUtil(YjAIService yjAIService) {
        YjAIUtil.yjAIService = yjAIService;
    }

    /**
     * websocket智能问答
     *
     * @param clientId 客户端id
     * @param text     问答内容
     * @param callback 回调接口
     * @author Carlos
     * @date 2024/4/19 13:40
     */
    public static void wsQa(String clientId, String text, WebSocketYjAIQACallback callback) {
        if (StrUtil.isBlank(clientId)) {
            throw new DockingYjAIException("不合法的客户端ID");
        }
        if (StrUtil.isBlank(text)) {
            throw new DockingYjAIException("问题内容不能为空");
        }
        yjAIService.wsQa(clientId, text, callback);
    }


    /**
     * 停止问答
     *
     * @param clientId 客户端id
     * @author Carlos
     * @date 2024/4/19 13:40
     */
    public static void wsQaStop(String clientId, WebSocketYjAIQACallback callback) {
        if (StrUtil.isBlank(clientId)) {
            throw new DockingYjAIException("不合法的客户端ID");
        }
        yjAIService.wsQaStop(clientId, callback);
    }
}
