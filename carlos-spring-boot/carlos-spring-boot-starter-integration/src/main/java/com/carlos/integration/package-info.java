/**
 * <p>
 * Carlos 第三方系统对接模�?
 * </p>
 * <p>
 * 本模块提供与钉钉、企业微信等第三方平台的集成功能�?
 * 基于 Spring RestClient 实现，完全隔离于项目 Feign 配置�?
 * </p>
 *
 * <h2>模块结构</h2>
 * <ul>
 *     <li><b>common</b> - 公共层，提供跨模块共享的异常�?/li>
 *     <li><b>core</b> - 核心框架层，提供 RestClient 封装和动态注册能�?/li>
 *     <li><b>module</b> - 业务模块层，按第三方系统划分</li>
 * </ul>
 *
 * <h2>快速开�?/h2>
 * <pre>
 * // 1. 配置钉钉
 * carlos:
 *   integration:
 *     dingtalk:
 *       enabled: true
 *       host: https://oapi.dingtalk.com
 *       appkey: ${DINGTALK_APPKEY}
 *       appsecret: ${DINGTALK_APPSECRET}
 *
 * // 2. 注入使用
 * &#64;Autowired
 * private DingtalkRestClientService dingtalkService;
 *
 * // 3. 调用 API
 * var user = dingtalkService.getUserInfo("user123");
 * </pre>
 *
 * @author Carlos
 * @since 1.0.0
 */
package com.carlos.integration;
