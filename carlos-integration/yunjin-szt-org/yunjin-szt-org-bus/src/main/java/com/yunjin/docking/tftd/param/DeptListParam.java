package com.yunjin.docking.tftd.param;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunjin.core.param.Param;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 翻译返回结果
 * </p>
 *
 * @author Carlos
 * @date 2022/3/8 12:18
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class DeptListParam implements Param {


    @JsonProperty("deptName")
    private String deptName;
}
