package com.carlos.boot.interceptors;


import com.carlos.boot.request.RequestInfo;
import com.carlos.boot.request.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

/**
 * <p>
 * 请求拦截器
 * </p>
 *
 * @author carlos
 * @date 2020/6/5 10:31
 */
@Slf4j
public class GlobalInterceptor implements AsyncHandlerInterceptor {

    private final GlobalInterceptorProperties properties;

    public GlobalInterceptor(GlobalInterceptorProperties globalInterceptor) {
        properties = globalInterceptor;
    }

    /**
     * 预处理回调方法，实现处理器的预处理 返回值：true表示继续流程；false表示流程中断，不会继续调用其他的拦截器或处理器
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        RequestInfo requestInfo = RequestUtil.getRequestInfo();
        if (log.isDebugEnabled()) {
            log.debug("-----------Request Start[{}]----------", requestInfo.getRequestId());
        }
        // if (requestInfo.getParam() == null) {
        //     requestInfo.setParam(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
        //     RequestUtil.refreshRequestInfo(requestInfo);
        // }

        // 打印请求信息
        if (properties.getPrintType() == GlobalInterceptorProperties.PrintType.REQUEST ||
                properties.getPrintType() == GlobalInterceptorProperties.PrintType.BOTH_ORDER) {
            RequestUtil.printRequestInfo();
        }
        return true;
    }

    /**
     * 方法返回后 整个请求处理完毕回调方法，即在视图渲染完毕时回调， 如性能监控中我们可以在此记录结束时间并输出消耗时间， 还可以进行一些资源清理，类似于try-catch-finally中的finally， 但仅调用处理器执行链中
     */
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler,
                                Exception ex) {
        if (log.isDebugEnabled()) {
            log.debug("-----------Request finish[{}]----------", RequestUtil.getRequestId());
        }
        // 释放本地线程资源
        RequestUtil.remove();
    }
}
