package com.yunjin.docking.yjai;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.json.JSONUtil;
import com.yunjin.docking.config.FeignBaseProperties;
import com.yunjin.docking.exception.DockingException;
import com.yunjin.docking.yjai.config.YjAIProperties;
import com.yunjin.docking.yjai.param.YjAITextParam;
import com.yunjin.docking.yjai.result.YjAIResult;
import com.yunjin.docking.yjai.result.YjAITextResultContent;
import com.yunjin.docking.yjai.result.YjaiWsResultContext;
import com.yunjin.socket.WebSocketYjAIQACallback;
import com.yunjin.socket.message.YjAIQaResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 元景大模型相关服务
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:05
 */
@Slf4j
@RequiredArgsConstructor
public class YjAIService {


    private final FeignYjAI feignYjAI;
    private final YjAIProperties properties;
    private Map<String, WebSocketClient> clientMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {

    }

    /**
     * 生成及时头部
     *
     * @return org.springframework.util.MultiValueMap<java.lang.String, java.lang.String>
     * @author Carlos
     * @date 2024/4/16 11:13
     */
    private Map<String, String> generateHeader() {
        Map<String, String> headers = new HashMap<>();
        String host = properties.getRegisterIp();
        String tenantId = properties.getTenantId();
        String agentId = properties.getAgentId();
        String timestamps = String.valueOf(DateUtil.currentSeconds());
        String nonce = RandomUtil.randomInt(100000, 999999) + "";
        headers.put("X-Bmp-Host", host);
        headers.put("X-Bmp-TenantID", tenantId);
        headers.put("X-Bmp-AgentID", agentId);
        headers.put("X-Bmp-Timestamp", timestamps);
        headers.put("X-Bmp-Nonce", nonce);
        String signature = host + "\n" + tenantId + "\n" + agentId + "\n" + timestamps + "\n" + nonce;
        HMac hMac = SecureUtil.hmacSha256(properties.getSecretKey());
        String sign = hMac.digestHex(signature);
        headers.put("X-Bmp-Signature", sign);
        return headers;
    }

    /**
     * 文本处理
     *
     * @param text     文本信息
     * @param clientId 参数客户端信息1
     * @param callback
     * @return com.yunjin.docking.yjai.result.YjAITextResultContent
     * @author Carlos
     * @date 2024/4/16 11:44
     */
    public YjAITextResultContent text(String clientId, String text, WebSocketYjAIQACallback callback) {
        YjAITextParam param = buildParam(clientId, text);
        try {

            Map<String, String> httpHeaders = generateHeader();
            log.info("Yjai text param:{}, httpHeaders:{}", JSONUtil.toJsonStr(param), JSONUtil.toJsonStr(httpHeaders));
            YjAIResult<YjAITextResultContent> result = feignYjAI.text(param, httpHeaders);
            log.info("yjai text success: result:{}", JSONUtil.toJsonStr(result));
            return checkResult(result);
        } catch (Exception e) {
            log.error("yjai text error: message:{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 构建基础参数
     *
     * @param clientId 客户端id
     * @param text     文本内容
     * @return com.yunjin.docking.yjai.param.YjAITextParam
     * @author Carlos
     * @date 2024/4/19 13:44
     */
    private YjAITextParam buildParam(String clientId, String text) {
        YjAITextParam param = new YjAITextParam();
        param.setClientId(clientId);
        param.setChannelId(properties.getChannelId());
        param.setRequestId(IdUtil.simpleUUID());
        param.setQuery(text);
        return param;
    }

    public void wsQa(String clientId, String text, WebSocketYjAIQACallback callback) {
        YjAITextParam param = buildParam(clientId, text);
        // 判断客户端是否存在
        WebSocketClient client = clientMap.get(clientId);
        if (client != null) {
            client.close();
        }
        FeignBaseProperties api = properties.getApi();
        String url = api.getHost() + "/ws/sidp/v1/ws_text?clientId=" + clientId;
        try {
            URI uri = new URI(url);
            client = new WebSocketClient(uri, generateHeader()) {
                private String lastConten;

                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    log.info("websocket已连接");
                }

                @Override
                public void onMessage(String resultJson) {
                    YjAIResult<?> result = JSONUtil.toBean(resultJson, YjAIResult.class);
                    String text = null;
                    boolean endFlag = false;
                    if (result == null) {
                        text = "暂时无法识别您的问题";
                        endFlag = true;
                    } else {
                        try {
                            checkResult(result);
                        } catch (Exception e) {
                            // text.
                        }
                        YjaiWsResultContext response = BeanUtil.toBean(result.getResponse(), YjaiWsResultContext.class);
                        text = Optional.ofNullable(response.getAnswer()).map(YjaiWsResultContext.Answer::getShowText).orElse("暂时无法识别您的问题");
                        String action = response.getAction();
                        switch (action) {
                            case "error":
                                endFlag = true;
                                break;
                            case "end":
                                endFlag = true;
                                break;
                            case "result":
                                endFlag = false;
                                break;
                            case "start":
                                endFlag = false;
                                break;
                            default:
                                endFlag = true;
                        }
                    }

                    YjAIQaResult wsResult = new YjAIQaResult();
                    lastConten = text;
                    wsResult.setContent(text);
                    wsResult.setEndFlag(endFlag);
                    callback.callback(wsResult);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    log.info("websocket已关闭");
                    callback.callback(new YjAIQaResult().setEndFlag(true).setContent(lastConten));
                }

                @Override
                public void onError(Exception e) {
                    log.info("websocket异常", e);
                }
            };
            clientMap.put(clientId, client);
            //建立连接 只到连接打开才往下面走
            client.connect();
            while (!client.getReadyState().equals(ReadyState.OPEN)) {
                // 等待连接
            }
            //发送请求
            if (client.getReadyState().equals(ReadyState.OPEN)) {
                client.send(JSONUtil.toJsonStr(param));
            }
        } catch (URISyntaxException e) {
            log.info("websocket异常", e);
        }
    }


    private <T> T checkResult(YjAIResult<T> result) {
        String code = result.getCode();
        if (StrUtil.isNotBlank(code)) {
            String errmsg = result.getMsg();

            if (code != null) {
                if (!code.equals("0")) {
                    log.error("YjAI result error: message:{}", errmsg);
                    throw new DockingException(errmsg);
                }
            } else {
                log.error("YjAI Service response error: errorCode:{}, errMsg:{}", code, errmsg);
                throw new DockingException(errmsg);
            }
        }
        return result.getResponse();
    }

    /**
     * 停止问答
     *
     * @param clientId 客户端id
     * @author Carlos
     * @date 2024/4/19 13:48
     */

    public void wsQaStop(String clientId, WebSocketYjAIQACallback callback) {
        WebSocketClient client = clientMap.get(clientId);
        if (client != null) {
            client.close();
        }
    }
}
