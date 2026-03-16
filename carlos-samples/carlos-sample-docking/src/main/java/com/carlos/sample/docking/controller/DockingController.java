package com.carlos.sample.docking.controller;

import com.carlos.docking.dingtalk.DingtalkService;
import com.carlos.docking.dingtalk.DingtalkUtil;
import com.carlos.docking.dingtalk.SendMessageRequest;
import com.carlos.docking.dingtalk.msg.ActionCardMsg;
import com.carlos.docking.dingtalk.msg.LinkMsg;
import com.carlos.docking.dingtalk.msg.MarkdownMsg;
import com.carlos.docking.dingtalk.msg.TextMsg;
import com.dingtalk.api.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 钉钉对接示例 Controller
 * </p>
 *
 * <p>
 * 演示如何使用 carlos-spring-boot-starter-docking 模块进行钉钉平台对接，包括：
 * <ul>
 *     <li>发送钉钉工作通知消息（文本、Markdown、ActionCard 等）</li>
 *     <li>获取用户信息</li>
 *     <li>获取部门信息</li>
 *     <li>同步组织架构</li>
 * </ul>
 * </p>
 *
 * @author Carlos
 * @version 3.0.0
 * @since 2025-03-15
 */
@Slf4j
@RestController
@RequestMapping("/docking")
@RequiredArgsConstructor
@Tag(name = "钉钉对接示例", description = "演示 carlos-spring-boot-starter-docking 模块的钉钉对接功能")
public class DockingController {

    private final DingtalkService dingtalkService;

    /**
     * 发送文本消息
     */
    @GetMapping("/dingtalk/send-text")
    @Operation(summary = "发送文本消息", description = "向指定手机号用户发送钉钉文本工作通知消息")
    public OapiMessageCorpconversationAsyncsendV2Response sendTextMessage(
        @Parameter(description = "接收消息的手机号") @RequestParam String phone,
        @Parameter(description = "消息内容") @RequestParam String content) {

        log.info("发送文本消息到手机号: {}, 内容: {}", phone, content);

        SendMessageRequest request = new SendMessageRequest();
        request.setPushPhoneList(Collections.singletonList(phone));

        TextMsg textMsg = new TextMsg();
        textMsg.setContent(content);
        request.setMsg(textMsg);

        return DingtalkUtil.send(request);
    }

    /**
     * 发送 Markdown 消息
     */
    @GetMapping("/dingtalk/send-markdown")
    @Operation(summary = "发送 Markdown 消息", description = "向指定手机号用户发送钉钉 Markdown 格式工作通知消息")
    public OapiMessageCorpconversationAsyncsendV2Response sendMarkdownMessage(
        @Parameter(description = "接收消息的手机号") @RequestParam String phone,
        @Parameter(description = "消息标题") @RequestParam String title,
        @Parameter(description = "Markdown 内容") @RequestParam String markdown) {

        log.info("发送 Markdown 消息到手机号: {}, 标题: {}", phone, title);

        SendMessageRequest request = new SendMessageRequest();
        request.setPushPhoneList(Collections.singletonList(phone));

        MarkdownMsg markdownMsg = new MarkdownMsg();
        markdownMsg.setTitle(title);
        markdownMsg.setText(markdown);
        request.setMsg(markdownMsg);

        return DingtalkUtil.send(request);
    }

    /**
     * 发送 ActionCard 消息
     */
    @GetMapping("/dingtalk/send-action-card")
    @Operation(summary = "发送 ActionCard 消息", description = "向指定手机号用户发送钉钉 ActionCard 类型工作通知消息")
    public OapiMessageCorpconversationAsyncsendV2Response sendActionCardMessage(
        @Parameter(description = "接收消息的手机号") @RequestParam String phone,
        @Parameter(description = "标题") @RequestParam String title,
        @Parameter(description = "Markdown 内容") @RequestParam String markdown,
        @Parameter(description = "单个按钮标题") @RequestParam String singleTitle,
        @Parameter(description = "单个按钮跳转链接") @RequestParam String singleUrl) {

        log.info("发送 ActionCard 消息到手机号: {}", phone);

        SendMessageRequest request = new SendMessageRequest();
        request.setPushPhoneList(Collections.singletonList(phone));

        ActionCardMsg actionCardMsg = new ActionCardMsg();
        actionCardMsg.setTitle(title);
        actionCardMsg.setMarkdown(markdown);
        actionCardMsg.setSingleTitle(singleTitle);
        actionCardMsg.setSingleUrl(singleUrl);
        actionCardMsg.setBtnOrientation("0");
        request.setMsg(actionCardMsg);

        return DingtalkUtil.send(request);
    }

    /**
     * 发送链接消息
     */
    @GetMapping("/dingtalk/send-link")
    @Operation(summary = "发送链接消息", description = "向指定手机号用户发送钉钉链接类型工作通知消息")
    public OapiMessageCorpconversationAsyncsendV2Response sendLinkMessage(
        @Parameter(description = "接收消息的手机号") @RequestParam String phone,
        @Parameter(description = "消息标题") @RequestParam String title,
        @Parameter(description = "消息内容") @RequestParam String content,
        @Parameter(description = "跳转链接") @RequestParam String url,
        @Parameter(description = "图片链接") @RequestParam(required = false) String picUrl) {

        log.info("发送链接消息到手机号: {}", phone);

        SendMessageRequest request = new SendMessageRequest();
        request.setPushPhoneList(Collections.singletonList(phone));

        LinkMsg linkMsg = new LinkMsg();
        linkMsg.setTitle(title);
        linkMsg.setText(content);
        linkMsg.setMessageUrl(url);
        linkMsg.setPicUrl(picUrl);
        request.setMsg(linkMsg);

        return DingtalkUtil.send(request);
    }

    /**
     * 批量发送消息
     */
    @PostMapping("/dingtalk/send-batch")
    @Operation(summary = "批量发送消息", description = "向多个手机号用户批量发送钉钉工作通知消息")
    public OapiMessageCorpconversationAsyncsendV2Response sendBatchMessage(
        @Parameter(description = "接收消息的手机号列表") @RequestBody List<String> phones,
        @Parameter(description = "消息内容") @RequestParam String content) {

        log.info("批量发送消息到 {} 个手机号", phones.size());

        SendMessageRequest request = new SendMessageRequest();
        request.setPushPhoneList(phones);

        TextMsg textMsg = new TextMsg();
        textMsg.setContent(content);
        request.setMsg(textMsg);

        return DingtalkUtil.send(request);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/dingtalk/user/{userId}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取钉钉用户信息")
    public OapiV2UserGetResponse getUserInfo(
        @Parameter(description = "钉钉用户ID") @PathVariable String userId) {

        log.info("获取用户信息: {}", userId);
        return dingtalkService.getUserInfo(userId);
    }

    /**
     * 根据手机号获取用户ID
     */
    @GetMapping("/dingtalk/user/by-mobile")
    @Operation(summary = "根据手机号获取用户ID", description = "根据手机号查询钉钉用户ID")
    public OapiV2UserGetbymobileResponse getUserByMobile(
        @Parameter(description = "手机号") @RequestParam String mobile) {

        log.info("根据手机号查询用户: {}", mobile);
        return dingtalkService.getByMobile(mobile);
    }

    /**
     * 根据 Code 获取用户ID（用于前端免登）
     */
    @GetMapping("/dingtalk/user/by-code")
    @Operation(summary = "根据 Code 获取用户ID", description = "根据免登授权码获取钉钉用户ID（用于前端免登场景）")
    public OapiV2UserGetuserinfoResponse.UserGetByCodeResponse getUserIdByCode(
        @Parameter(description = "免登授权码") @RequestParam String code) {

        log.info("根据 Code 获取用户ID: {}", code);
        return dingtalkService.getUserId(code);
    }

    /**
     * 获取部门信息
     */
    @GetMapping("/dingtalk/dept/{deptId}")
    @Operation(summary = "获取部门信息", description = "根据部门ID获取钉钉部门详细信息")
    public OapiV2DepartmentGetResponse.DeptGetResponse getDeptInfo(
        @Parameter(description = "部门ID") @PathVariable Long deptId) {

        log.info("获取部门信息: {}", deptId);
        return dingtalkService.getDeptInfo(deptId);
    }

    /**
     * 获取子部门列表
     */
    @GetMapping("/dingtalk/dept/{deptId}/children")
    @Operation(summary = "获取子部门列表", description = "获取指定部门的子部门列表")
    public List<OapiV2DepartmentListsubResponse.DeptBaseResponse> listSubDepts(
        @Parameter(description = "部门ID") @PathVariable Long deptId) {

        log.info("获取部门 {} 的子部门列表", deptId);
        return dingtalkService.listDeptSub(deptId);
    }

    /**
     * 获取部门用户列表
     */
    @GetMapping("/dingtalk/dept/{deptId}/users")
    @Operation(summary = "获取部门用户列表", description = "获取指定部门的用户列表（分页）")
    public OapiV2UserListResponse.PageResult listDeptUsers(
        @Parameter(description = "部门ID") @PathVariable Long deptId,
        @Parameter(description = "分页游标") @RequestParam(required = false, defaultValue = "0") Long cursor,
        @Parameter(description = "分页大小") @RequestParam(required = false, defaultValue = "100") Long size) {

        log.info("获取部门 {} 的用户列表, cursor: {}, size: {}", deptId, cursor, size);
        return dingtalkService.listUser(deptId, cursor, size);
    }

    /**
     * 同步组织架构示例
     */
    @PostMapping("/dingtalk/sync-organization")
    @Operation(summary = "同步组织架构", description = "从钉钉同步组织架构到本地系统（示例）")
    public String syncOrganization(
        @Parameter(description = "根部门ID") @RequestParam(required = false, defaultValue = "1") Long rootDeptId) {

        log.info("开始同步组织架构，根部门ID: {}", rootDeptId);

        // 获取根部门信息
        OapiV2DepartmentGetResponse.DeptGetResponse rootDept = dingtalkService.getDeptInfo(rootDeptId);
        log.info("根部门: {}", rootDept.getName());

        // 获取子部门
        List<OapiV2DepartmentListsubResponse.DeptBaseResponse> subDepts = dingtalkService.listDeptSub(rootDeptId);
        log.info("子部门数量: {}", subDepts.size());

        // 获取部门用户
        OapiV2UserListResponse.PageResult users = dingtalkService.listUser(rootDeptId, 0L, 100L);
        log.info("部门用户数量: {}", users.getList().size());

        return String.format("同步完成：根部门[%s]，子部门[%d]个，用户[%d]个",
            rootDept.getName(), subDepts.size(), users.getList().size());
    }

    /**
     * 获取 AccessToken（调试用）
     */
    @GetMapping("/dingtalk/access-token")
    @Operation(summary = "获取 AccessToken", description = "获取钉钉 AccessToken（仅用于调试）")
    public String getAccessToken() {
        String token = dingtalkService.getToken();
        log.info("获取 AccessToken: {}...", token.substring(0, Math.min(10, token.length())));
        return token;
    }
}
