package com.carlos.message.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.message.pojo.dto.MessageTemplateDTO;
import com.carlos.message.pojo.entity.MessageTemplate;
import com.carlos.message.pojo.param.MessageTemplatePageParam;
import com.carlos.message.pojo.vo.MessageTemplateVO;

import java.io.Serializable;

/**
 * <p>
 * 消息模板 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月12日 上午11:17:05
 */
public interface MessageTemplateManager extends BaseService<MessageTemplate> {

    /**
     * 新增消息模板
     *
     * @param dto 消息模板数据
     * @return boolean
     * @author Carlos
     * @date 2026年3月12日 上午11:17:05
     */
    boolean add(MessageTemplateDTO dto);

    /**
     * 删除消息模板
     *
     * @param id 消息模板id
     * @return boolean
     * @author Carlos
     * @date 2026年3月12日 上午11:17:05
     */
    boolean delete(Serializable id);

    /**
     * 修改消息模板信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2026年3月12日 上午11:17:05
     */
    boolean modify(MessageTemplateDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.message.pojo.dto.MessageTemplateDTO
     * @author Carlos
     * @date 2026年3月12日 上午11:17:05
     */
    MessageTemplateDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2026年3月12日 上午11:17:05
     */
    Paging<MessageTemplateVO> getPage(MessageTemplatePageParam param);
}
