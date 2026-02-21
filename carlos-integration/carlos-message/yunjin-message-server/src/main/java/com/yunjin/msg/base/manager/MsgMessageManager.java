package com.carlos.msg.base.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.msg.base.pojo.dto.MsgMessageDTO;
import com.carlos.msg.base.pojo.entity.MsgMessage;
import com.carlos.msg.base.pojo.param.MsgMessagePageParam;
import com.carlos.msg.base.pojo.vo.MsgMessageVO;

import java.io.Serializable;

/**
 * <p>
 * 消息 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
public interface MsgMessageManager extends BaseService<MsgMessage> {

    /**
     * 新增消息
     *
     * @param dto 消息数据
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean add(MsgMessageDTO dto);

    /**
     * 删除消息
     *
     * @param id 消息id
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean delete(Serializable id);

    /**
     * 修改消息信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean modify(MsgMessageDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.msg.pojo.dto.MsgMessageDTO
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    Paging<MsgMessageVO> getPage(MsgMessagePageParam param);
}
