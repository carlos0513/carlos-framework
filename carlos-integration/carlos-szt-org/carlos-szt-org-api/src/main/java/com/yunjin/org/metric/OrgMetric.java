package com.yunjin.org.metric;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 表单指标数据
 * </p>
 *
 * @author Carlos
 * @date 2025-05-19 23:11
 */
@Data
@Accessors(chain = true)
public class OrgMetric implements Serializable {

    /**
     * 注册用户数
     * 1、展示当前登录用户（系统管理员统计全系统、部门管理员统计当前部门及下级部门）注册用户数据，注意筛选用户状态(计入禁用，不计入逻辑删除)。
     * 2、单位：人。
     */
    private int registerCount;
    /**
     * 禁用用户数
     * 1、展示当前登录用户（系统管理员统计全系统、部门管理员统计当前部门及下级部门）禁用的用户数据。
     * 2、单位：人。
     */
    private int disableCount;
    /**
     * 活跃用户数
     * 1、展示当前登录用户（系统管理员统计全系统、部门管理员统计当前部门及下级部门）活跃用户数据。（①一个月以内登陆过系统的用户数量。③等于移动端+PC端的活跃用户数之和）
     * 2、单位：人。
     */
    private int activeCount;
    /**
     * PC端活跃用户数
     * 1、展示当前登录用户（系统管理员统计全系统、部门管理员统计当前部门及下级部门）PC端活跃用户数据。（①一个月以内登陆过系统的用户数量。）
     * 2、单位：人。
     */
    private int pcActiveCount;
    /**
     * 移动端活跃用户数
     * 1、展示当前登录用户（系统管理员统计全系统、部门管理员统计当前部门及下级部门）移动端活跃用户数据。（①一个月以内登陆过系统的用户数量。）
     * 2、单位：人。
     */
    private int mobileActiveCount;
    /**
     * 近一年注册用户数
     */
    private int registerCountInOneYear;

}
