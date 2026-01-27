package com.carlos.boot.request;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.carlos.apm.TraceUtil;
import com.carlos.core.auth.AuthConstant;
import com.carlos.core.auth.UserContext;
import com.carlos.core.constant.CoreConstant;
import com.carlos.core.response.Result;
import com.carlos.core.response.StatusCode;
import com.carlos.json.jackson.JacksonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 获取当前请求的HttpServletRequest对象
 *
 * @author yunjin
 * @date 2020/4/10 16:46
 */
@Slf4j
@Component
public class RequestUtil {

    private static final ThreadLocal<RequestInfo> THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 获取当前请求
     *
     * @author yunjin
     * @date 2020/4/17 11:49
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
    }

    /**
     * 获取当前请求的信息
     *
     * @author yunjin
     * @date 2020/4/14 9:34
     */
    public static RequestInfo getRequestInfo() {
        RequestInfo requestInfo = THREAD_LOCAL.get();
        if (requestInfo == null) {
            HttpServletRequest request = RequestUtil.getRequest();
            requestInfo = new RequestInfo();
            LocalDateTime now = LocalDateTime.now();
            String requestId = TraceUtil.getTraceId();
            if (StrUtil.isBlank(requestId)) {
                requestId = DateUtil.format(now, DatePattern.PURE_DATETIME_MS_PATTERN + RandomUtil.randomInt(3));
            }
            requestInfo.setRequestId(requestId);
            requestInfo.setContextPath(request.getContextPath());
            requestInfo.setRealPath(request.getServletPath());
            requestInfo.setUrl(request.getRequestURL().toString());
            requestInfo.setIp(IpUtil.getRequestIp(request));
            requestInfo.setMethod(request.getMethod());
            requestInfo.setContentType(request.getContentType());
            // requestInfo.setParam(getParams(request));
            requestInfo.setTime(now);
            Map<String, String> headers = JakartaServletUtil.getHeaderMap(request);
            // 请求头中存在feign代表该请求是feign请求
            String rpc = headers.get(CoreConstant.HEADER_NAME_RPC);
            if (rpc != null) {
                requestInfo.setRpc(true);
            }

            // requestInfo.setHeader(headers);
            UserContext context = new UserContext();
            Optional.ofNullable(headers.get(AuthConstant.USER_TOKEN)).ifPresent(context::setToken);
            Optional.ofNullable(headers.get(AuthConstant.USER_ACCOUNT)).ifPresent(context::setAccount);
            Optional.ofNullable(headers.get(AuthConstant.USER_ID)).ifPresent(context::setUserId);
            Optional.ofNullable(headers.get(AuthConstant.DEPT_ID)).ifPresent(context::setDepartmentId);
            Optional.ofNullable(headers.get(AuthConstant.ROLE_ID)).ifPresent(context::setRoleId);
            Optional.ofNullable(headers.get(AuthConstant.TENANT_ID)).ifPresent(context::setTenantId);
            Optional.ofNullable(headers.get(AuthConstant.USER_PHONE)).ifPresent(context::setPhone);
            Optional.ofNullable(headers.get(AuthConstant.ROLE_IDS)).ifPresent(i -> {
                List<Serializable> items = StrUtil.split(i, CharUtil.COMMA, -1, true, s -> s);
                if (CollUtil.isNotEmpty(items)) {
                    context.setRoleIds(new HashSet<>(items));
                }
            });
            Optional.ofNullable(headers.get(AuthConstant.DEPT_IDS)).ifPresent(i -> {
                List<Serializable> items = StrUtil.split(i, CharUtil.COMMA, -1, true, s -> s);
                if (CollUtil.isNotEmpty(items)) {
                    context.setDepartmentIds(new HashSet<>(items));
                }
            });

            requestInfo.setUserContext(context);

            // 新增应用信息
            AppInfo appinfo = new AppInfo();
            Optional.ofNullable(headers.get(AuthConstant.APP_ID)).ifPresent(appinfo::setAppId);
            Optional.ofNullable(headers.get(AuthConstant.APP_KEY)).ifPresent(appinfo::setAppKey);
            Optional.ofNullable(headers.get(AuthConstant.APP_NAME)).ifPresent(appinfo::setAppName);
            requestInfo.setAppInfo(appinfo);
            THREAD_LOCAL.set(requestInfo);
        }
        return requestInfo;
    }

    /**
     * 设置登录用户信息
     *
     * @param userContext 用户信息
     * @author Carlos
     * @date 2023/3/31 18:51
     */
    public static void setUserContext(UserContext userContext) {
        RequestInfo requestInfo = getRequestInfo();
        requestInfo.setUserContext(userContext);
        THREAD_LOCAL.set(requestInfo);

    }

    /**
     * 获取请求的参数
     *
     * @param request 请求对象
     * @author yunjin
     * @date 2020/4/26 23:26
     */
    private static Object getParams(HttpServletRequest request) {
        if (JakartaServletUtil.isMultipart(request)) {
            return null;
        }
        Object param = null;
        if (JakartaServletUtil.isGetMethod(request)) {
            param = JakartaServletUtil.getParamMap(request);
        } else {
            try {
                String body = JakartaServletUtil.getBody(request);
                if (JSONUtil.isTypeJSON(body)) {
                    param = JacksonUtil.string2Obj(body, Object.class);
                }
            } catch (Exception e) {
                param = null;
                log.warn("Get request param failed, message:{}", e.getMessage());
            }
        }
        return ObjectUtils.isNotEmpty(param) ? param : null;
    }

    /**
     * 是否为Delete请求
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return 是否为POST请求
     */
    public static boolean isDeleteMethod(HttpServletRequest request) {
        return JakartaServletUtil.METHOD_DELETE.equalsIgnoreCase(request.getMethod());
    }

    /**
     * 是否为OPTIONS请求
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return 是否为POST请求
     */
    public static boolean isOptionsMethod(HttpServletRequest request) {
        return JakartaServletUtil.METHOD_OPTIONS.equalsIgnoreCase(request.getMethod());
    }

    /**
     * 是否为TRACE请求
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return 是否为POST请求
     */
    public static boolean isTraceMethod(HttpServletRequest request) {
        return JakartaServletUtil.METHOD_TRACE.equalsIgnoreCase(request.getMethod());
    }

    /**
     * 是否为PUT请求
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return 是否为POST请求
     */
    public static boolean isPutMethod(HttpServletRequest request) {
        return JakartaServletUtil.METHOD_PUT.equalsIgnoreCase(request.getMethod());
    }

    /**
     * 从当前线程移除请求信息
     *
     * @author yunjin
     * @date 2020/4/14 9:35
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }

    /**
     * 刷新本地线程中的请求信息
     *
     * @param requestInfo 刷新后的请求信息
     * @author yunjin
     * @date 2020/4/23 14:05
     */
    public static void refreshRequestInfo(RequestInfo requestInfo) {
        THREAD_LOCAL.remove();
        THREAD_LOCAL.set(requestInfo);
    }

    /**
     * 根据配置打印请求信息
     *
     * @author yunjin
     * @date 2020/4/23 14:07
     */
    public static void printRequestInfo() {
        log.info("request info:{}", objectToJson(getRequestInfo()));
    }

    /**
     * 根据配置打印请求信息
     *
     * @author yunjin
     * @date 2020/4/23 14:07
     */
    public static void printResponseInfo(Result<?> result) {
        String response = objectToJson(result);
        if (result.getCode() == StatusCode.SUCCESS.getCode()) {
            log.info("response info:{}", response);
        } else {
            log.info("response info:{}", response);
        }
    }

    /**
     * 将对象转换为json并判断是否需要格式化
     *
     * @author yunjin
     * @date 2020/6/4 14:18
     */
    private static String objectToJson(Object obj) {
        try {
            return JacksonUtil.toJson(obj, false, false);
        } catch (Exception e) {
            log.error("Exception for Object to Json", e);
            return null;
        }
    }

    /**
     * 获取请求id
     *
     * @return java.lang.String
     * @author yunjin
     * @date 2021/12/28 16:43
     */
    public static String getRequestId() {
        return getRequestInfo().getRequestId();
    }

    /**
     * 判断是否是Feign发出的请求
     *
     * @return boolean
     * @author yunjin
     * @date 2022/3/21 13:33
     */
    public static boolean isRPC() {
        return getRequestInfo().isRpc();
    }

    /**
     * 获取应用信息
     *
     * @return com.carlos.boot.request.AppInfo
     * @author Carlos
     * @date 2025-04-15 14:19
     */
    public static AppInfo getAppInfo() {
        return getRequestInfo().getAppInfo();
    }
}
