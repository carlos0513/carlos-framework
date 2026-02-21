package com.yunjin.board.data.result;


import com.yunjin.json.jackson.annotation.EnumField;
import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.enums.UserMessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 看板数据
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class BoardNoticeMessageDataResult extends BoardDataResult {


    private int total;
    private int size;
    private int pages;
    private int current;
    private List<Item> records;

    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    public static class Item implements Serializable {


        /**
         * 主键
         */
        private String id;
        /**
         * 用户id
         */
        private String userId;
        /**
         * 消息id
         */
        private String messageId;
        /**
         * 消息类型
         */

        // TODO: Carlos 2025-05-16 不同类型颜色处理
        @EnumField(type = EnumField.SerializerType.FULL)
        private UserMessageType type;
        /**
         * 标题
         */
        private String title;
        /**
         * 读取状态
         */
        @EnumField(type = EnumField.SerializerType.FULL)
        private UserMessageStatus status;
        /**
         * 创建人
         */
        private String creator;
        /**
         * 发布日期
         */
        private LocalDateTime sendDate;
        /**
         * 内容
         */
        private String content;
        /**
         * 部门code
         */
        private String deptCode;
        /**
         * 创建人姓名
         */
        private String creatorName;
    }

}
