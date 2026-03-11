package com.carlos.message.api;

import com.carlos.core.pojo.response.Paging;
import com.carlos.core.response.Result;
import com.carlos.message.pojo.ao.MessageRecordAO;
import com.carlos.message.pojo.ao.MessageReceiverAO;
import com.carlos.message.pojo.param.MessagePageParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 消息查询服务（Feign接口定义）
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@FeignClient(
        name = "carlos-message",
        contextId = "messageQueryApi",
        path = "/api/message",
        fallbackFactory = MessageQueryApiFallbackFactory.class
)
public interface MessageQueryApi {

    /**
     * 查询消息记录详情
     *
     * @param messageId 消息ID
     * @return 消息记录
     */
    @GetMapping("/{messageId}")
    Result<MessageRecordAO> getById(@PathVariable String messageId);

    /**
     * 分页查询消息记录
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    Result<Paging<MessageRecordAO>> page(@RequestBody @Valid MessagePageParam param);

    /**
     * 查询消息接收人列表
     *
     * @param messageId 消息ID
     * @return 接收人列表
     */
    @GetMapping("/{messageId}/receivers")
    Result<List<MessageReceiverAO>> getReceivers(@PathVariable String messageId);

    /**
     * 查询用户的未读消息
     *
     * @param userId 用户ID
     * @return 未读消息列表
     */
    @GetMapping("/unread/{userId}")
    Result<List<MessageRecordAO>> getUnread(@PathVariable String userId);
}
