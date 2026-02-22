package com.carlos.msg.base.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.msg.base.pojo.dto.MsgMessageTemplateDTO;
import com.carlos.msg.base.pojo.entity.MsgMessageTemplate;
import com.carlos.msg.base.pojo.excel.MsgMessageTemplateExcel;
import com.carlos.msg.base.pojo.param.MsgMessageTemplatePageParam;
import com.carlos.msg.base.pojo.vo.MsgMessageTemplatePageVO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 消息模板 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
public interface MsgMessageTemplateManager extends BaseService<MsgMessageTemplate> {

    /**
     * 新增消息模板
     *
     * @param dto 消息模板数据
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean add(MsgMessageTemplateDTO dto);

    /**
     * 删除消息模板
     *
     * @param id 消息模板id
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean delete(Serializable id);

    /**
     * 修改消息模板信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    boolean modify(MsgMessageTemplateDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.msg.pojo.dto.MsgMessageTemplateDTO
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageTemplateDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    Paging<MsgMessageTemplatePageVO> getPage(MsgMessageTemplatePageParam param);

    /**
     * 根据模板编码获取模板信息
     *
     * @param templateCode
     * @return
     */
    MsgMessageTemplateDTO getByCode(String templateCode);

    /**
     * 获取已激活的消息模板列表
     *
     * @return
     */
    List<MsgMessageTemplateExcel> getByIsActive();
}
