package com.carlos.org.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户岗位关联实体
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Data
@TableName("org_user_position")
public class OrgUserPosition {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long positionId;

    private Integer isMain;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
