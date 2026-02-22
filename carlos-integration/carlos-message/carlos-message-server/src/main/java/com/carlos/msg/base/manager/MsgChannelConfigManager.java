package com.carlos.msg.base.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.msg.api.pojo.enums.ChannelType;
import com.carlos.msg.base.pojo.dto.MsgChannelConfigDTO;
import com.carlos.msg.base.pojo.entity.MsgChannelConfig;
import com.carlos.msg.base.pojo.param.MsgChannelConfigPageParam;
import com.carlos.msg.base.pojo.vo.MsgChannelConfigVO;

import java.io.Serializable;

/**
 * <p>
 * 消息渠道配置 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
public interface MsgChannelConfigManager extends BaseService<MsgChannelConfig> {

    /**
     * 新增消息渠道配置
     *
     * @param dto 消息渠道配置数据
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean add(MsgChannelConfigDTO dto);

    /**
     * 删除消息渠道配置
     *
     * @param id 消息渠道配置id
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean delete(Serializable id);

    /**
     * 修改消息渠道配置信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean modify(MsgChannelConfigDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.msg.pojo.dto.MsgChannelConfigDTO
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgChannelConfigDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    Paging<MsgChannelConfigVO> getPage(MsgChannelConfigPageParam param);

    /**
     * 根据渠道编码获取消息渠道配置
     *
     * @param channelType
     * @return
     */
    MsgChannelConfigDTO getByType(ChannelType channelType);


    /**
     * 修改状态
     *
     * @param id
     * @param enabled
     */
    boolean updateState(Serializable id, Boolean enabled);
}
