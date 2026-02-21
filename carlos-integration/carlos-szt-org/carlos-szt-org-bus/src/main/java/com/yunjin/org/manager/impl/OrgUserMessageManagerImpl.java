package com.yunjin.org.manager.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.datasource.pagination.MybatisPage;
import com.yunjin.datasource.pagination.PageInfo;
import com.yunjin.org.convert.OrgUserMessageConvert;
import com.yunjin.org.manager.OrgUserMessageManager;
import com.yunjin.org.manager.UserManager;
import com.yunjin.org.mapper.OrgUserMessageMapper;
import com.yunjin.org.pojo.dto.OrgUserMessageDTO;
import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.pojo.entity.OrgUserMessage;
import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.enums.UserMessageType;
import com.yunjin.org.pojo.param.OrgUserMessagePageParam;
import com.yunjin.org.pojo.vo.OrgUserMessageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户消息表 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2024-2-28 17:39:16
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrgUserMessageManagerImpl extends BaseServiceImpl<OrgUserMessageMapper, OrgUserMessage> implements OrgUserMessageManager {

    private final UserManager userManager;

    @Override
    public boolean add(OrgUserMessageDTO dto) {
        OrgUserMessage entity = OrgUserMessageConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'OrgUserMessage' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'OrgUserMessage' data: id:{}", entity.getId());
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
            log.warn("Remove 'OrgUserMessage' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'OrgUserMessage' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(OrgUserMessageDTO dto) {
        OrgUserMessage entity = OrgUserMessageConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'OrgUserMessage' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'OrgUserMessage' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public OrgUserMessageDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        OrgUserMessage entity = getBaseMapper().selectById(id);
        return OrgUserMessageConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<OrgUserMessageVO> getPage(OrgUserMessagePageParam param) {
        LambdaQueryWrapper<OrgUserMessage> wrapper = queryWrapper();
        wrapper.eq(Objects.nonNull(param.getType()), OrgUserMessage::getType, param.getType());
        wrapper.eq(Objects.nonNull(param.getStatus()), OrgUserMessage::getStatus, param.getStatus());
        wrapper.like(Objects.nonNull(param.getTitle()), OrgUserMessage::getTitle, param.getTitle());
        wrapper.between(Objects.nonNull(param.getStart()) && Objects.nonNull(param.getEnd()), OrgUserMessage::getSendDate, param.getStart(), param.getEnd());
        wrapper.orderByDesc(OrgUserMessage::getSendDate);
        wrapper.le(OrgUserMessage::getSendDate, LocalDateTime.now());
        wrapper.eq(OrgUserMessage::getUserId, param.getUserId());
        wrapper.and(w -> w.eq(OrgUserMessage::getDeptCode, param.getDeptCode()).or().isNull(OrgUserMessage::getDeptCode));
        wrapper.select(
                OrgUserMessage::getId,
                OrgUserMessage::getUserId,
                OrgUserMessage::getMessageId,
                OrgUserMessage::getType,
                OrgUserMessage::getTitle,
                OrgUserMessage::getStatus,
                OrgUserMessage::getCreator,
                OrgUserMessage::getSendDate,
                OrgUserMessage::getContent,
                OrgUserMessage::getDeptCode
        );
        PageInfo<OrgUserMessage> page = page(pageInfo(param), wrapper);
        // 设置创建人
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<UserDTO> userList = userManager.getByIds(page.getRecords().stream().map(OrgUserMessage::getCreator).collect(Collectors.toSet()));
            Map<String, String> userMap = userList.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getRealname));
            for (OrgUserMessage record : page.getRecords()) {
                record.setCreator(userMap.get(record.getCreator()));
            }
        }
        return MybatisPage.convert(page, OrgUserMessageConvert.INSTANCE::toVO);
    }


    @Override
    public List<OrgUserMessage> listByType(UserMessageType type, String userId) {
        LambdaQueryWrapper<OrgUserMessage> wrapper = queryWrapper();
        wrapper.eq(OrgUserMessage::getType, type);
        wrapper.eq(OrgUserMessage::getUserId, userId);
        wrapper.select(
                OrgUserMessage::getId,
                OrgUserMessage::getUserId,
                OrgUserMessage::getMessageId,
                OrgUserMessage::getType,
                OrgUserMessage::getTitle,
                OrgUserMessage::getStatus,
                OrgUserMessage::getCreator,
                OrgUserMessage::getSendDate,
                OrgUserMessage::getContent
        );
        return getBaseMapper().selectList(wrapper);
    }

    @Override
    public void updateState(String id, UserMessageStatus status) {
        LambdaUpdateWrapper<OrgUserMessage> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(OrgUserMessage::getId, id);
        wrapper.set(OrgUserMessage::getStatus, status);
        update(wrapper);

    }
}
