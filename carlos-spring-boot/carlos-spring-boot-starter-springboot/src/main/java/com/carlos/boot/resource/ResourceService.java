package com.carlos.boot.resource;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.boot.resource.bean.ApplicationResource;
import com.carlos.boot.resource.bean.Resource;
import com.carlos.boot.resource.bean.ResourceCategory;
import com.carlos.core.exception.ComponentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 资源服务
 * </p>
 *
 * @author carlos
 * @date 2022/6/24 11:01
 */
@Slf4j
@RequiredArgsConstructor
public class ResourceService {

    private final ApplicationResourceProperties resourceProperties;

    private final ResourceStore resourceStore;

    public ApplicationResource init() {
        try {
            final Set<String> packages = resourceProperties.getScanPackage();
            if (CollectionUtil.isEmpty(packages)) {
                throw new ComponentException("The property 'scanPackage' is null");
            }

            if (log.isDebugEnabled()) {
                log.debug("资源包:{}", packages);
            }

            // 使用反射 扫描工程的基本包路径
            final Reflections reflections = new Reflections(packages);
            final Set<Class<?>> set = reflections.getTypesAnnotatedWith(Tag.class);
            if (CollectionUtil.isEmpty(set)) {
                return null;
            }

            // 循环获取所有的Controller
            final List<ResourceCategory> categories = new LinkedList<>();
            for (final Class<?> clazz : set) {
                // controller相关信息
                final ResourceController resourceController = handleController(clazz);
                final ResourceCategory category = new ResourceCategory();
                category.setName(resourceController.getFeatureName());
                final List<Method> methods = ReflectUtil.getPublicMethods(clazz, method -> method.isAnnotationPresent(Operation.class));
                final List<Resource> list = new LinkedList<>();
                final String[] prefixArray = resourceController.getPath();
                // 同一个controller可以配置多个path
                for (String prefix : prefixArray) {
                    prefix = StrUtil.addPrefixIfNot(prefix, StrUtil.SLASH);
                    for (final Method method : methods) {
                        // controller中的方法
                        final ResourceMethod resourceMethod = handleMethod(method);
                        final String[] paths = resourceMethod.getPath();
                        final RequestMethod[] requestMethods = resourceMethod.getRequestMethods();
                        for (String path : paths) {
                            path = path.length() == 0 ? path : StrUtil.addPrefixIfNot(path, StrUtil.SLASH);
                            path = ReUtil.replaceAll(path, "\\{.*?\\}", "**");
                            for (final RequestMethod requestMethod : requestMethods) {
                                final Resource resource = new Resource();
                                resource.setName(resourceMethod.getResourceName());
                                resource.setPathPrefix(StrUtil.addPrefixIfNot(resourceProperties.getPrefix(), StrUtil.SLASH));
                                resource.setPath(prefix + path);
                                resource.setMethod(requestMethod);
                                resource.setHidden(resourceMethod.isHidden());
                                resource.setDescription(resourceMethod.getResourceNote());
                                list.add(resource);
                            }
                        }
                    }
                }
                category.setResources(list);
                categories.add(category);

            }
            if (log.isDebugEnabled()) {
                log.debug("资源已初始化");
            }
            final ApplicationResource applicationResource = new ApplicationResource();
            applicationResource.setAppName(resourceProperties.getAppName());
            applicationResource.setCategories(categories);
            resourceStore.save(applicationResource);
            return applicationResource;
        } catch (final Exception e) {
            log.error("初始化资源异常", e);
            throw new ComponentException("初始化资源异常");
        }
    }

    /**
     * 处理controller
     *
     * @param clazz 参数0
     * @return com.carlos.boot.resource.ResourceController
     * @author carlos
     * @date 2022/1/11 18:24
     */
    private ResourceController handleController(final Class<?> clazz) {
        final String simpleName = clazz.getSimpleName();
        final ResourceController resourceController = new ResourceController();
        resourceController.setClassName(simpleName);

        final Tag api = clazz.getAnnotation(Tag.class);
        if (api == null) {
            resourceController.setFeatureName(simpleName);
        } else {
            final String tags = api.name();
            if (StrUtil.isNotBlank(tags)) {
                resourceController.setFeatureName(tags);
            }
        }

        final RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
        final String[] paths;
        if (requestMapping == null) {
            paths = new String[]{StrUtil.SLASH};
        } else {
            final String[] value = requestMapping.value();
            if (ArrayUtil.isNotEmpty(value)) {
                paths = value;
            } else {
                paths = requestMapping.path();
            }
        }
        resourceController.setPath(paths);
        return resourceController;
    }

    /**
     * 方法处理
     *
     * @param method 方法
     * @return com.carlos.boot.resource.ResourceMethod
     * @author carlos
     * @date 2022/1/11 16:04
     */
    private ResourceMethod handleMethod(final Method method) {
        final String methodName = method.getName();
        final ResourceMethod resourceMethod = new ResourceMethod();
        resourceMethod.setName(methodName);

        final Operation apiOperation = method.getAnnotation(Operation.class);
        String note = null;
        if (apiOperation == null) {
            resourceMethod.setResourceName(methodName);
        } else {
            resourceMethod.setHidden(apiOperation.hidden());
            resourceMethod.setResourceName(apiOperation.summary());
            note = apiOperation.description();
        }
        if (StrUtil.isBlank(note)) {
            note = method.getDeclaringClass().getSimpleName() + "#" + methodName;
        }
        resourceMethod.setResourceNote(note);

        request:
        {
            final RequestMapping request = method.getAnnotation(RequestMapping.class);
            if (request == null) {
                break request;
            }
            String[] paths;
            final String[] value = request.value();
            if (ArrayUtil.isNotEmpty(value)) {
                paths = value;
            } else {
                paths = request.path();
            }
            if (ArrayUtil.isEmpty(paths)) {
                paths = new String[]{StrUtil.EMPTY};
            }
            resourceMethod.setPath(paths);
            resourceMethod.setRequestMethods(request.method());
            return resourceMethod;
        }

        get:
        {
            final GetMapping request = method.getAnnotation(GetMapping.class);
            if (request == null) {
                break get;
            }
            String[] paths;
            final String[] value = request.value();
            if (ArrayUtil.isNotEmpty(value)) {
                paths = value;
            } else {
                paths = request.path();
            }
            if (ArrayUtil.isEmpty(paths)) {
                paths = new String[]{StrUtil.EMPTY};
            }
            resourceMethod.setPath(paths);
            resourceMethod.setRequestMethods(new RequestMethod[]{RequestMethod.GET});
            return resourceMethod;
        }
        post:
        {
            final PostMapping request = method.getAnnotation(PostMapping.class);
            if (request == null) {
                break post;
            }
            String[] paths;
            final String[] value = request.value();
            if (ArrayUtil.isNotEmpty(value)) {
                paths = value;
            } else {
                paths = request.path();
            }
            if (ArrayUtil.isEmpty(paths)) {
                paths = new String[]{StrUtil.EMPTY};
            }
            resourceMethod.setPath(paths);
            resourceMethod.setRequestMethods(new RequestMethod[]{RequestMethod.POST});
            return resourceMethod;
        }

        put:
        {
            final PutMapping request = method.getAnnotation(PutMapping.class);
            if (request == null) {
                break put;
            }
            String[] paths;
            final String[] value = request.value();
            if (ArrayUtil.isNotEmpty(value)) {
                paths = value;
            } else {
                paths = request.path();
            }
            if (ArrayUtil.isEmpty(paths)) {
                paths = new String[]{StrUtil.EMPTY};
            }
            resourceMethod.setPath(paths);
            resourceMethod.setRequestMethods(new RequestMethod[]{RequestMethod.PUT});
            return resourceMethod;
        }

        delete:
        {
            final DeleteMapping request = method.getAnnotation(DeleteMapping.class);
            if (request == null) {
                break delete;
            }
            String[] paths;
            final String[] value = request.value();
            if (ArrayUtil.isNotEmpty(value)) {
                paths = value;
            } else {
                paths = request.path();
            }
            if (ArrayUtil.isEmpty(paths)) {
                paths = new String[]{StrUtil.EMPTY};
            }
            resourceMethod.setPath(paths);
            resourceMethod.setRequestMethods(new RequestMethod[]{RequestMethod.DELETE});
            return resourceMethod;
        }
        throw new ComponentException(methodName + "can't find any annotation of '@RequestMapping'");
    }
}
