package com.carlos.gateway.config;


import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * <p>
 * 统一返回内容
 * </p>
 *
 * @author carlos
 * @date 2021/12/6 17:00
 */
@Slf4j
// @Component
public class ResponseCoverFilter implements WebFilter, Ordered {

    // TODO: carlos 2021/12/6 对通过网关的请求进行统一返回

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                // if (body instanceof Flux) {
                //     if (ContentType.JSON.toString()
                //             .equals(originalResponse.getHeaders().getFirst(Header.CONTENT_TYPE.toString()))) {
                //         Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>)body;
                //         return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                //             DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                //             DataBuffer join = dataBufferFactory.join(dataBuffers);
                //             byte[] content = new byte[join.readableByteCount()];
                //             join.read(content);
                //             // 释放掉内存
                //             DataBufferUtils.release(join);
                //             String responseData = new String(content, Charsets.UTF_8);
                //             GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
                //             if (Objects.nonNull(gatewayContext) && StringUtils.isNotBlank(responseData)) {
                //                 //此处拿到参数转为JSONObject 处理参数内容
                //                 JSONObject jsonObject = JSON.parseObject(responseData);
                //                 //TODO 对返回值JSONObject进行put remove get等操作
                //
                //                 responseData = JSONObject.toJSONString(jsonObject, SerializerFeature.MapSortField);
                //             }
                //             byte[] uppedContent = responseData.getBytes(Charsets.UTF_8);
                //             return bufferFactory.wrap(uppedContent);
                //         }));
                //     }
                // }
                return super.writeWith(body);
            }

        };
        // replace response with decorator
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return 101;
    }
}
