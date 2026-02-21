package com.yunjin.org.pojo.ao;

import com.yunjin.org.pojo.vo.OrgUserMessageVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/*
    单个消息详情AO
 */
@Data
@Accessors(chain = true)
public class OrgUserMessageDetailAO extends OrgUserMessageVO {
    @Schema("系统公告图片")
    private Object images;
}
