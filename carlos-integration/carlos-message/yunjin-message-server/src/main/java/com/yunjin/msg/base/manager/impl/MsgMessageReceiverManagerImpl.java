package com.carlos.msg.base.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.msg.base.convert.MsgMessageReceiverConvert;
import com.carlos.msg.base.manager.MsgMessageReceiverManager;
import com.carlos.msg.base.mapper.MsgMessageReceiverMapper;
import com.carlos.msg.base.pojo.dto.MsgMessageReceiverDTO;
import com.carlos.msg.base.pojo.entity.MsgMessageReceiver;
import com.carlos.msg.base.pojo.param.MsgMessageReceiverPageParam;
import com.carlos.msg.base.pojo.vo.MsgMessageReceiverVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 消息接受者 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MsgMessageReceiverManagerImpl extends BaseServiceImpl<MsgMessageReceiverMapper, MsgMessageReceiver> implements MsgMessageReceiverManager {

    @Override
    public boolean add(MsgMessageReceiverDTO dto) {
        MsgMessageReceiver entity = MsgMessageReceiverConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'MsgMessageReceiver' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'MsgMessageReceiver' data: id:{}", entity.getId());
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
            log.warn("Remove 'MsgMessageReceiver' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'MsgMessageReceiver' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(MsgMessageReceiverDTO dto) {
        MsgMessageReceiver entity = MsgMessageReceiverConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'MsgMessageReceiver' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'MsgMessageReceiver' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public MsgMessageReceiverDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        MsgMessageReceiver entity = getBaseMapper().selectById(id);
        return MsgMessageReceiverConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<MsgMessageReceiverVO> getPage(MsgMessageReceiverPageParam param) {
        LambdaQueryWrapper<MsgMessageReceiver> wrapper = queryWrapper();
        wrapper.select(
                MsgMessageReceiver::getId,
                MsgMessageReceiver::getMessageId,
                MsgMessageReceiver::getReceiverId,
                MsgMessageReceiver::getReceiverNumber,
                MsgMessageReceiver::getReceiverAudience,
                MsgMessageReceiver::getRead,
                MsgMessageReceiver::getSuccess,
                MsgMessageReceiver::getCreateBy,
                MsgMessageReceiver::getCreateTime
        );
        PageInfo<MsgMessageReceiver> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, MsgMessageReceiverConvert.INSTANCE::toVO);
    }

}
