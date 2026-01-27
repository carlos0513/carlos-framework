package com.carlos.boot.resource;


import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>
 * 接口描述对象
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
public class ResourceMethod {

    /**
     * 方法ming
     */
    private String name;
    /**
     * 请求路径 如果请求路径null 请设置为 ""
     */
    private String[] path = new String[]{StrUtil.EMPTY};
    /**
     * 请求方式
     */
    private RequestMethod[] requestMethods;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * note
     */
    private String resourceNote;

    /**
     * hidden
     */
    private boolean hidden;


}
