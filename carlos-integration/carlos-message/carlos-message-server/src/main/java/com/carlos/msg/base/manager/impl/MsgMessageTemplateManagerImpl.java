package com.carlos.msg.base.manager.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.msg.base.convert.MsgMessageTemplateConvert;
import com.carlos.msg.base.manager.MsgMessageTemplateManager;
import com.carlos.msg.base.mapper.MsgMessageTemplateMapper;
import com.carlos.msg.base.pojo.dto.MsgMessageTemplateDTO;
import com.carlos.msg.base.pojo.entity.MsgMessageTemplate;
import com.carlos.msg.base.pojo.entity.MsgMessageType;
import com.carlos.msg.base.pojo.excel.MsgMessageTemplateExcel;
import com.carlos.msg.base.pojo.param.MsgMessageTemplatePageParam;
import com.carlos.msg.base.pojo.vo.MsgMessageTemplatePageVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 消息模板 查询封装实现类
 * </p>
 *
 * @author Carlos
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MsgMessageTemplateManagerImpl extends BaseServiceImpl<MsgMessageTemplateMapper, MsgMessageTemplate> implements MsgMessageTemplateManager {

    @Override
    public boolean add(MsgMessageTemplateDTO dto) {
        MsgMessageTemplate entity = MsgMessageTemplateConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'MsgMessageTemplate' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'MsgMessageTemplate' data: id:{}", entity.getId());
        }
        return true;
    }

    @Override
    public boolean delete(Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        boolean success = removeById(id);
        if (!success) {
            log.warn("Remove 'MsgMessageTemplate' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'MsgMessageTemplate' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(MsgMessageTemplateDTO dto) {
        MsgMessageTemplate entity = MsgMessageTemplateConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'MsgMessageTemplate' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'MsgMessageTemplate' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public MsgMessageTemplateDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        MsgMessageTemplate entity = getBaseMapper().selectById(id);
        return MsgMessageTemplateConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<MsgMessageTemplatePageVO> getPage(MsgMessageTemplatePageParam param) {
        LambdaQueryWrapper<MsgMessageTemplate> wrapper = queryWrapper();
        wrapper.select(
            MsgMessageTemplate::getId,
            MsgMessageTemplate::getTypeId,
            MsgMessageTemplate::getTemplateCode,
            MsgMessageTemplate::getTemplateContent,
            MsgMessageTemplate::getChannelConfig,
            MsgMessageTemplate::getActive,
            MsgMessageTemplate::getCreateBy,
            MsgMessageTemplate::getCreateTime,
            MsgMessageTemplate::getUpdateBy,
            MsgMessageTemplate::getUpdateTime
        );
        PageInfo<MsgMessageTemplate> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, MsgMessageTemplateConvert.INSTANCE::toVO);
    }

    @Override
    public MsgMessageTemplateDTO getByCode(String templateCode) {
        if (StrUtil.isBlank(templateCode)) {
            return null;
        }
        LambdaQueryChainWrapper<MsgMessageTemplate> wrapper = lambdaQuery();
        wrapper.select(
                MsgMessageTemplate::getId,
                MsgMessageTemplate::getTypeId,
                MsgMessageTemplate::getTemplateCode,
                MsgMessageTemplate::getTemplateContent,
                MsgMessageTemplate::getChannelConfig,
                MsgMessageTemplate::getActive
            )
            .eq(MsgMessageTemplate::getTemplateCode, templateCode);
        return MsgMessageTemplateConvert.INSTANCE.toDTO(wrapper.one());
    }

    @Override
    public List<MsgMessageTemplateExcel> getByIsActive() {
        List<MsgMessageTemplateDTO> messageTemplates = getBaseMapper().selectJoinList(MsgMessageTemplateDTO.class,
            new MPJLambdaWrapper<MsgMessageTemplate>().selectAll(MsgMessageTemplate.class)
                .selectAs(MsgMessageType::getTypeName, MsgMessageTemplateDTO::getTypeName)
                .leftJoin(MsgMessageType.class, MsgMessageType::getId, MsgMessageTemplate::getTypeId)
        );
        return MsgMessageTemplateConvert.INSTANCE.toExcels(messageTemplates);
    }

}
