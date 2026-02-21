package com.yunjin.org.pojo.dto;
import com.yunjin.org.pojo.enums.UserMessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

// 接受各类型消息的抽象类

@Data
@Accessors(chain = true)
public class OrgUserMessageDetailDTO<T extends AbstractMessageDetailDTO> {
    @Schema("消息通知的具体类型")
    private UserMessageType type;
    @Schema("消息通知的具体内容")
    private T data;
}

