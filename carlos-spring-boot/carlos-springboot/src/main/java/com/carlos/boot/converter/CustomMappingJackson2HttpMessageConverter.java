package com.carlos.boot.converter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.carlos.core.constant.CoreConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * <p>
 * 自定义消息转换器 内部提供的openfeign对象不进行特殊字段转换 因为全局地对所有的long转string的粒度太粗了，我们需要对不同的接口进行区分，比如限定只对web前端的接口需要转换，但对于内部微服务之间的调用或者第三方接口等则不需要进行转换。
 * </p>
 *
 * @author yunjin
 * @date 2022/3/18 9:55
 */
@Slf4j
public class CustomMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public CustomMappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    /**
     * 判断该转换器是否能将请求内容转换成 Java 对象
     */
    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        // 不需要反序列化
        return false;
    }

    /**
     * 判断该转换器是否可以将 Java 对象转换成返回内容. 匹配web api(形如/web/xxxx)中的接口方法的返回参数
     */
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        if (!super.canWrite(clazz, mediaType)) {
            return false;
        }
        // 浏览器调用，进行自定义转换，如果是非浏览器调用，如http工具调用，rpc调用，则不进行处理

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return false;
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) attributes;
        HttpServletRequest request = requestAttributes.getRequest();
        // 如果是rpc请求则不进行转换
        String rpcFeign = JakartaServletUtil.getHeaderIgnoreCase(request, CoreConstant.HEADER_NAME_RPC);
        if (StrUtil.isNotBlank(rpcFeign)) {
            if (rpcFeign.equals(CoreConstant.HEADER_RPC_VALUE_FEIGN)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 读取传参
     */
    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return super.read(type, contextClass, inputMessage);
    }

    /**
     * 后台数据写出
     */
    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        super.writeInternal(object, type, outputMessage);
    }
}
