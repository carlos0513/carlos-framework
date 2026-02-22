package com.carlos.org.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author zhangpd
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DepartmentCreateOrUpdateParam {
    private String deptId;
    private String name;
    private String parentId;
    private Integer order;
    private Integer level;
    private String tel;
    private String linkman;
    private String address;
    private String remark;
    private String areaValue;
    private String areaText;
    private String targetCode;
}
