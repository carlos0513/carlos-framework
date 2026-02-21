package com.yunjin.org.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.yunjin.core.enums.AppEnum;
import com.yunjin.core.enums.BaseEnum;
import com.yunjin.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AppEnum(code = "UserMessageType")
@Getter
@AllArgsConstructor
public enum UserMessageType implements BaseEnum {
    /**
     * 数据显示
     */
    MESSAGE(5, "系统公告", "message"),
    COMPLAINTS(6, "投诉建议", "complaints"),
    COLLECTION(7, "采集任务", "collection"),
    CHECK(8, "核查任务", "check"),
    APPLY(9, "需求申请", "apply"),
    WAREHOUS(10, "数据入仓", "warehous"),
    RELEASE_WITHOUT_REVIEW(11, "未审即发", "releaseWithoutReview"),
    PROVINCIAL(20, "省级任务", "provincial"),
    CITY(30, "市级任务", "city"),
    THIRD(40, "三方任务", "third"),
    ;
    @EnumValue
    private final Integer code;
    private final String desc;
    @Getter
    private final String type;


    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    public static UserMessageType getByName(final String name) {
        UserMessageType[] values = UserMessageType.values();
        for (final UserMessageType value : values) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        throw new ServiceException("消息类型不合法");
    }

}
