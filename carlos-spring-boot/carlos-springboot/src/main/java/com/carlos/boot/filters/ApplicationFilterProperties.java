package com.carlos.boot.filters;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import com.carlos.boot.BootConstant;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 过滤器属性配置
 * </p>
 *
 * @author yunjin
 * @date 2020/4/14 11:15
 */
@Data
@ConfigurationProperties(prefix = "carlos.boot.filters")
public class ApplicationFilterProperties {

    /**
     * RequestWrapper Filter配置
     */
    private final FilterProperties requestWrapper = new FilterProperties();

    /**
     * XSS Filter配置
     */
    private final FilterProperties xss = new FilterProperties();

    @Data
    public static class FilterProperties {


        private static final String[] DEFAULT_URL_MAPPINGS = {"/*"};

        /**
         * 是否启用 默认启用
         */
        private boolean enable = true;

        /**
         * 过滤器名称
         */
        private String name;

        /**
         * 过滤的路径
         */
        private String[] urlPatterns = DEFAULT_URL_MAPPINGS;

        /**
         * 排除过滤的路径
         */
        private Set<String> excludes;

        /**
         * 排序
         */
        private int order = 1;

        /**
         * 是否支持异步 默认为 true  支持
         */
        private boolean async = true;


        /**
         * 初始化过滤器参数
         *
         * @param filter 需要注册的过滤器
         * @return org.springframework.boot.web.servlet.FilterRegistrationBean<?>
         * @author yunjin
         * @date 2021/10/4 1:56
         */
        public <T extends Filter> FilterRegistrationBean<T> initFilterRegistrationBean(final FilterRegistrationBean<T> filter) {
            filter.setDispatcherTypes(DispatcherType.REQUEST);
            filter.setEnabled(this.isEnable());
            filter.addUrlPatterns(this.getUrlPatterns());
            if (CharSequenceUtil.isNotBlank(this.getName())) {
                filter.setName(this.getName());
            }
            filter.setOrder(this.getOrder());
            filter.setAsyncSupported(this.isAsync());
            Set<String> excludeUris = this.getExcludes();
            if (CollUtil.isEmpty(excludeUris)) {
                excludeUris = new HashSet<>(BootConstant.COMMON_EXCLUDE_PATH);
            }
            final Map<String, String> initParameters = new HashMap<>(1);
            initParameters.put(BootConstant.FILTER_INIT_PARAM_EXCLUDES_URIS_, CharSequenceUtil.join(StrPool.COMMA, excludeUris));
            filter.setInitParameters(initParameters);
            return filter;
        }

    }
}
