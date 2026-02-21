package com.yunjin.org.pojo.dto;

import com.yunjin.org.pojo.emuns.HelpFileEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class HelpFileDTO {
    /** 主键ID */
    private String id;
    /** 文件描述 */
    private String fileContent;
    /** 文件名称 */
    private String fileName;

    /** 排序 */
    private String sort;
    /** 文件类型 */
    private HelpFileEnum fileType;
    /** 创建人 */
    private String createBy;
    /** 更新人 */
    private String updateBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 文件样例 */
    private String fileSample;


}
