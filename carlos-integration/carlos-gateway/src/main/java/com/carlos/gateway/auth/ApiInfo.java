package com.carlos.gateway.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpMethod;

import java.io.Serializable;

/**
 * <p>
 *   用户菜单信息
 * </p>
 *
 * @author Carlos
 * @date 2025-01-13 11:52
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ApiInfo {
    private Serializable id;

    private String url;

    private String name;

    private HttpMethod method;
}
