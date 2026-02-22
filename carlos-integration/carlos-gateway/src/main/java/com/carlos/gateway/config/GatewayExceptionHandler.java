package com.carlos.gateway.config;

import com.carlos.core.exception.GlobalException;
import com.carlos.core.response.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>
 * 自定义异常处理器
 * </p>
 *
 * @author yunjin
 * @date 2022/4/11 11:45
 */
@Slf4j
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("gateway exception", ex);

        //1.获取响应对象
        ServerHttpResponse response = exchange.getResponse();
        //2.response是否结束  用于多个异常处理时候
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        //2.设置响应头类型
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        //3.设置响应状态吗
        if (ex instanceof GlobalException) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
        } else {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //4.设置响应内容
        return response
                .writeWith(Mono.fromSupplier(() -> {
                    DataBufferFactory bufferFactory = response.bufferFactory();
                    Result<?> result = Result.fail("请求异常", ex.getMessage());
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        //设置响应到response的数据
                        return bufferFactory.wrap(objectMapper.writeValueAsBytes(result));
                    } catch (Exception e) {
                        log.error("GatewayExceptionHandler handel exception failed, message:{}", e.getMessage(), e);
                        return null;
                    }
                }));
    }


}
