package com.carlos.tool.gitlab.enums;

import com.carlos.tool.gitlab.ui.CreateMergeRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;

/**
 * <p>
 * 工具类型
 * </p>
 *
 * @author Carlos
 * @date 2020/8/18 12:09
 * @since 3.0
 */
@AllArgsConstructor
@Getter
public enum GitlabToolType {
    /**
     * 工具类型
     */
    CREATE_MERGE_REQUEST("创建合并请求", new CreateMergeRequest()),
    ;


    /**
     * 描述
     */
    private final String desc;
    /**
     * 描述
     */
    private final JFrame frame;


}
