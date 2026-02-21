package com.carlos.msg.base.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.msg.base.pojo.dto.MsgMessageReceiverDTO;
import com.carlos.msg.base.pojo.entity.MsgMessageReceiver;
import com.carlos.msg.base.pojo.param.MsgMessageReceiverPageParam;
import com.carlos.msg.base.pojo.vo.MsgMessageReceiverVO;

import java.io.Serializable;

/**
 * <p>
 * 消息接受者 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
public interface MsgMessageReceiverManager extends BaseService<MsgMessageReceiver> {

    /**
     * 新增消息接受者
     *
     * @param dto 消息接受者数据
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean add(MsgMessageReceiverDTO dto);

    /**
     * 删除消息接受者
     *
     * @param id 消息接受者id
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean delete(Serializable id);

    /**
     * 修改消息接受者信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean modify(MsgMessageReceiverDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.msg.pojo.dto.MsgMessageReceiverDTO
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageReceiverDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    Paging<MsgMessageReceiverVO> getPage(MsgMessageReceiverPageParam param);
}
