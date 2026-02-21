package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgComplaintReportDetailVO extends OrgComplaintReportVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(value = "处理记录")
    private List<OrgComplaintLogVO> handleList;

}
