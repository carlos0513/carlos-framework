package com.carlos.org.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 部门关系枚举
 */
@Getter
@AllArgsConstructor
public enum DeptRelationEnum  {

    /**
     * 关系类型
     */
    ALL(1, "所有部门"),
    CurrentAndSubset(2, "当前部门和子集"),
    CurrentAndPeerLevel(3, "当前部门和平级"),
    CurrentAndPeerLevelAndSubset(4, "当前部门和平级和子集"),
    CurrentAndPeerLevelAndSuperior(5, "当前部门和平级和上级"),
    SuperiorPeerLevelAndSubset(6, "上级的平级的下级"),
    ;

    private final Integer code;

    private final String desc;

}
