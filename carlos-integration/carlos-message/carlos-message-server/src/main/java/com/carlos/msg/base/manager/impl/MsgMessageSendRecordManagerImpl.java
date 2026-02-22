package com.carlos.msg.base.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.msg.base.convert.MsgMessageSendRecordConvert;
import com.carlos.msg.base.manager.MsgMessageSendRecordManager;
import com.carlos.msg.base.mapper.MsgMessageSendRecordMapper;
import com.carlos.msg.base.pojo.dto.MsgMessageSendRecordDTO;
import com.carlos.msg.base.pojo.entity.MsgMessageSendRecord;
import com.carlos.msg.base.pojo.param.MsgMessageSendRecordPageParam;
import com.carlos.msg.base.pojo.vo.MsgMessageSendRecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 消息发送记录 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MsgMessageSendRecordManagerImpl extends BaseServiceImpl<MsgMessageSendRecordMapper, MsgMessageSendRecord> implements MsgMessageSendRecordManager {

    @Override
    public boolean add(MsgMessageSendRecordDTO dto) {
        MsgMessageSendRecord entity = MsgMessageSendRecordConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'MsgMessageSendRecord' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'MsgMessageSendRecord' data: id:{}", entity.getId());
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
            log.warn("Remove 'MsgMessageSendRecord' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'MsgMessageSendRecord' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(MsgMessageSendRecordDTO dto) {
        MsgMessageSendRecord entity = MsgMessageSendRecordConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'MsgMessageSendRecord' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'MsgMessageSendRecord' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public MsgMessageSendRecordDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        MsgMessageSendRecord entity = getBaseMapper().selectById(id);
        return MsgMessageSendRecordConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<MsgMessageSendRecordVO> getPage(MsgMessageSendRecordPageParam param) {
        LambdaQueryWrapper<MsgMessageSendRecord> wrapper = queryWrapper();
        wrapper.select(
                MsgMessageSendRecord::getId,
                MsgMessageSendRecord::getMessageId,
                MsgMessageSendRecord::getRetryCount,
                MsgMessageSendRecord::getSendTime,
                MsgMessageSendRecord::getRequestParam,
                MsgMessageSendRecord::getResponseData,
                MsgMessageSendRecord::getPushChannel,
                MsgMessageSendRecord::getSuccess,
                MsgMessageSendRecord::getCreateBy,
                MsgMessageSendRecord::getCreateTime
        );
        PageInfo<MsgMessageSendRecord> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, MsgMessageSendRecordConvert.INSTANCE::toVO);
    }

}
