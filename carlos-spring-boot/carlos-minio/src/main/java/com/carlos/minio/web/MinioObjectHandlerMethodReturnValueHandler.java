package com.carlos.minio.web;

import com.carlos.minio.utils.ObjectOptUtil;
import java.io.InputStream;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * <p>
 * 如果接口返回的是Minio接口返回值，则返回对应的文件
 * </p>
 *
 * @author yunjin
 * @date 2021/6/10 13:36
 */
public class MinioObjectHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {


    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getParameterType() == MinioObject.class;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) {

        final MinioObject minioObject = (MinioObject) returnValue;

        final String bucket = minioObject.getBucket();
        final String object = minioObject.getObject();
        final InputStream inputStream = ObjectOptUtil.getObject(bucket, object);

        mavContainer.setView(new MinioObjectView(inputStream, minioObject.getAttachmentName()));
    }

}
