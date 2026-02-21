package com.yunjin.board.pojo.enums;

import com.yunjin.core.enums.AppEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Title: FrontFixedMenuEnum
 * @Author fxd
 * @Date 2026/1/4 15:21
 * @description: 常用应用，前端维护的枚举类
 */
@AppEnum(code = "FrontFixedMenuEnum")
@Getter
@AllArgsConstructor
public enum FrontFixedMenuEnum {

    REPORT_REGISTER("报表注册", "报表信息的创建管理", "/collection/report-register"),
    MY_TASK("我的任务", "个人任务的全流程管理", "/task/task-own"),
    MY_SCENE("我的场景", "查看有权限的场景和数据", "/scenario/scene-show"),
    DATA_MARKET("数据超市", "通过选取字段获取对应数据", "/field-label-market/field");

    private final String title;
    private final String desc;
    private final String path;


    /**
     * 获取所有title的集合
     * @return Set<String>
     */
    public static Set<String> getAllTitles() {
        return Arrays.stream(values())
                .map(FrontFixedMenuEnum::getTitle)
                .collect(Collectors.toSet());
    }


}