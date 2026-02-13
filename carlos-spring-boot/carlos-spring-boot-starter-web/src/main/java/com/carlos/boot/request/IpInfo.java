package com.carlos.boot.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 请求IP相关信息
 * </p>
 *
 * @author carlos
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
