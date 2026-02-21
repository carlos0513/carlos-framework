package com.carlos.gateway.swagger;

import com.google.common.collect.Sets;
import com.carlos.core.util.PathMatchUtil;
import com.carlos.json.jackson.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 网关聚合swagger后，给swagger添加basePath，添加统一前缀
 *
 * @author shenyong
 * @e-mail sheny60@chinaunicom.cn
 * @date 2024/7/12 14:21
 **/
@Component
@Slf4j
public class AddSwaggerBasePath implements WebFilter, Ordered {

    @Value("${spring.cloud.gateway.api-prefix:/bbt-api}")
    private String apiPrefix;

    Set<String> PATTENS = Sets.newHashSet(
            "/**/v2/api-docs/**",
            "/**/v3/api-docs"
    );

    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getRawPath();
        if (PathMatchUtil.antMatch(PATTENS, path)) {
            // 给swagger的 api-docs response，添加统一前缀
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                @NotNull
                @Override
                @SuppressWarnings("unchecked")
                public Mono<Void> writeWith(@NotNull Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            byte[] newContent;
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            join.read(content);
                            DataBufferUtils.release(join);
                            // 获取响应数据
                            String responseStr = new String(content, StandardCharsets.UTF_8);
                            // 修改响应数据
                            Map<String, Object> responseMap = JacksonUtil.readMap(responseStr);
                            if (Objects.nonNull(responseMap) && responseMap.containsKey("paths")) {
                                Map<String, Object> pathMap = (Map<String, Object>) responseMap.get("paths");
                                Map<String, Object> newPathMap = new HashMap<>();
                                pathMap.forEach((key, value) -> newPathMap.put(apiPrefix + key, value));
                                responseMap.put("paths", newPathMap);
                            }

                            String message = JacksonUtil.toJson(responseMap);
                            newContent = message.getBytes(StandardCharsets.UTF_8);
                            originalResponse.getHeaders().setContentLength(newContent.length);
                            return bufferFactory.wrap(newContent);
                        }));
                    }
                    return super.writeWith(body);
                }

                @NotNull
                @Override
                public Mono<Void> writeAndFlushWith(@NotNull Publisher<? extends Publisher<? extends DataBuffer>> body) {
                    return writeWith(Flux.from(body).flatMapSequential(p -> p));
                }
            };
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -98;
    }
}
