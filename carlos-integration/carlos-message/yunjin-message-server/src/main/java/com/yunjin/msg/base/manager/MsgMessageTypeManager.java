package com.carlos.msg.base.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.msg.base.pojo.dto.MsgMessageTypeDTO;
import com.carlos.msg.base.pojo.entity.MsgMessageType;
import com.carlos.msg.base.pojo.param.MsgMessageTypePageParam;
import com.carlos.msg.base.pojo.vo.MsgMessageTypeBaseVO;
import com.carlos.msg.base.pojo.vo.MsgMessageTypeVO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 消息类型 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
public interface MsgMessageTypeManager extends BaseService<MsgMessageType> {

    /**
     * 新增消息类型
     *
     * @param dto 消息类型数据
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean add(MsgMessageTypeDTO dto);

    /**
     * 删除消息类型
     *
     * @param id 消息类型id
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean delete(Serializable id);

    /**
     * 修改消息类型信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean modify(MsgMessageTypeDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.msg.pojo.dto.MsgMessageTypeDTO
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageTypeDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    Paging<MsgMessageTypeVO> getPage(MsgMessageTypePageParam param);

    /**
     * 根据类型名称获取消息数据
     *
     * @param typeName
     * @return
     */
    MsgMessageTypeDTO getByName(String typeName);

    /**
     * 根据类型编码获取消息数据
     *
     * @param typeCode
     * @return
     */
    MsgMessageTypeDTO getByCode(String typeCode);

    /**
     * 根据关键字获取已启动的消息类型列表
     *
     * @param keyword
     * @return
     */
    List<MsgMessageTypeBaseVO> getEnabledPage(String keyword);
}
