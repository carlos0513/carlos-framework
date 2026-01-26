package com.yunjin.core.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 公用ID参数
 * </p>
 *
 * @author yunjin
 * @date 2020/4/14 11:41
 */
@Data
@Schema(description = "List集合参数")
public class ParamIdList<T> implements Param {

    private List<T> ids;
}
