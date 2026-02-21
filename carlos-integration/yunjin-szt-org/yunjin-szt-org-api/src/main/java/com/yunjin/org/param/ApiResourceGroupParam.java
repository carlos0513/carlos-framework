package com.yunjin.org.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author shenyong
 * @e-mail sheny60@chinaunicom.cn
 * @date 2024/10/15 10:02
 **/
@Data
@Accessors(chain = true)
public class ApiResourceGroupParam {
    private Set<String> roleIds;
}
