package com.carlos.msg.base.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.msg.base.pojo.dto.MsgMessageSendRecordDTO;
import com.carlos.msg.base.pojo.entity.MsgMessageSendRecord;
import com.carlos.msg.base.pojo.param.MsgMessageSendRecordPageParam;
import com.carlos.msg.base.pojo.vo.MsgMessageSendRecordVO;

import java.io.Serializable;

/**
 * <p>
 * 消息发送记录 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
public interface MsgMessageSendRecordManager extends BaseService<MsgMessageSendRecord> {

    /**
     * 新增消息发送记录
     *
     * @param dto 消息发送记录数据
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean add(MsgMessageSendRecordDTO dto);

    /**
     * 删除消息发送记录
     *
     * @param id 消息发送记录id
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean delete(Serializable id);

    /**
     * 修改消息发送记录信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean modify(MsgMessageSendRecordDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.msg.pojo.dto.MsgMessageSendRecordDTO
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageSendRecordDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    Paging<MsgMessageSendRecordVO> getPage(MsgMessageSendRecordPageParam param);
}
