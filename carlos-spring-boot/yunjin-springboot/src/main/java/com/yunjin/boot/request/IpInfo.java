package com.yunjin.boot.request;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 请求IP相关信息
 * </p>
 *
 * @author yunjin
 * @date 2020/4/20 16:50
 */
@Data
@Accessors(chain = true)
public class IpInfo implements Serializable {

    private String ipStart;

    private String ipEnd;

    private String area;

    private String operator;

    private Long ipStartNum;

    private Long ipEndNum;
}
