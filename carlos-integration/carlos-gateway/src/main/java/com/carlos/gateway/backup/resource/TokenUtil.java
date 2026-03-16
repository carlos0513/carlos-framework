package com.carlos.gateway.resource;

import com.carlos.core.auth.AuthConstant;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.text.ParseException;

/**
 * <p>
 * token操作工具
 * </p>
 *
 * @author carlos
 * @date 2021/12/23 17:23
 */
public class TokenUtil {


    /**
     * 从请求头中获取token
     *
     * @param request 请求
     * @return java.lang.String
     * @author carlos
     * @date 2021/12/23 17:24
     */
    public static String getFromHeader(ServerHttpRequest request) {
        return request.getHeaders().getFirst(AuthConstant.TOKEN_HEADER);
    }

    /**
     * 获取token中的自定义信息
     *
     * @param token token
     * @param claim 信息载体
     * @throws ParseException token解析异常
     * @author carlos
     * @date 2021/12/23 17:28
     */
    // public static JwtClaims getTokenClaims(String token, Class<? extends JwtClaims> claim) throws ParseException {
    //     token = token.replace(AuthConstant.TOKEN_PREFIX, StrUtil.EMPTY);
    //     SignedJWT jwt = SignedJWT.parse(token);
    //     Payload payload = jwt.getPayload();
    //     return payload.toType(payload1 -> {
    //         String s = payload1.toString();
    //         return JSONUtil.toBean(s, claim);
    //     });
    // }


}
