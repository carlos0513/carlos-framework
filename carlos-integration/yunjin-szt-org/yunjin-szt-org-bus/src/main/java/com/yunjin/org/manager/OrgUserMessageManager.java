package com.yunjin.org.manager;

import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseService;
import com.yunjin.org.pojo.dto.OrgUserMessageDTO;
import com.yunjin.org.pojo.entity.OrgUserMessage;
import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.enums.UserMessageType;
import com.yunjin.org.pojo.param.OrgUserMessagePageParam;
import com.yunjin.org.pojo.vo.OrgUserMessageVO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户消息表 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2024-2-28 17:39:16
 */
public interface OrgUserMessageManager extends BaseService<OrgUserMessage> {

    /**
     * 新增用户消息表
     *
     * @param dto 用户消息表数据
     * @return boolean
     * @author yunjin
     * @date 2024-2-28 17:39:16
     */
    boolean add(OrgUserMessageDTO dto);

    /**
     * 删除用户消息表
     *
     * @param id 用户消息表id
     * @return boolean
     * @author yunjin
     * @date 2024-2-28 17:39:16
     */
    boolean delete(Serializable id);

    /**
     * 修改用户消息表信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2024-2-28 17:39:16
     */
    boolean modify(OrgUserMessageDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.yunjin.org.pojo.dto.OrgUserMessageDTO
     * @author yunjin
     * @date 2024-2-28 17:39:16
     */
    OrgUserMessageDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author yunjin
     * @date 2024-2-28 17:39:16
     */
    Paging<OrgUserMessageVO> getPage(OrgUserMessagePageParam param);

    List<OrgUserMessage> listByType(UserMessageType type, String userId);

    /**
     * updateState
     *
     * @param id 参数0
     * @param status 参数1
     * @throws
     * @author Carlos
     * @date 2025-10-04 22:57
     */
    void updateState(String id, UserMessageStatus status);
}
