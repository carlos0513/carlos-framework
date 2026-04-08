/**
 * <p>
 * 安全告警审计模块
 * </p>
 *
 * <p>本模块按照 Carlos Framework 四层架构规范实现：</p>
 * <ul>
 *   <li><b>Entity</b>：与数据库表结构一一对应 (pojo.entity)</li>
 *   <li><b>DTO</b>：服务层与数据层之间传输的对象 (pojo.dto)</li>
 *   <li><b>VO</b>：显示层对象，响应给前端 (pojo.vo)</li>
 *   <li><b>Param</b>：前端参数接收对象 (pojo.param)</li>
 *   <li><b>Enum</b>：业务枚举类型 (pojo.enums)</li>
 *   <li><b>Mapper</b>：MyBatis Mapper 接口 (mapper)</li>
 *   <li><b>Manager</b>：数据查询封装层，继承 BaseService (manager/)</li>
 *   <li><b>Service</b>：业务逻辑服务层 (service/)</li>
 *   <li><b>Convert</b>：MapStruct 对象转换 (convert/)</li>
 * </ul>
 *
 * <p>功能说明：</p>
 * <ul>
 *   <li>异地登录告警检测</li>
 *   <li>新设备登录告警检测</li>
 *   <li>暴力破解攻击检测</li>
 *   <li>非工作时间登录告警</li>
 *   <li>告警通知（邮件/短信/钉钉）</li>
 *   <li>审计日志同步到 carlos-audit 服务</li>
 * </ul>
 *
 * @author Carlos
 * @date 2026-04-08
 */
package com.carlos.auth.audit;
