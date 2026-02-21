package com.yunjin.org.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.org.convert.OrgComplaintLogConvert;
import com.yunjin.org.manager.OrgComplaintLogManager;
import com.yunjin.org.mapper.OrgComplaintLogMapper;
import com.yunjin.org.pojo.dto.OrgComplaintLogDTO;
import com.yunjin.org.pojo.emuns.ComplainActionEnum;
import com.yunjin.org.pojo.entity.OrgComplaintLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 投诉建议处理节点日志 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrgComplaintLogManagerImpl extends BaseServiceImpl<OrgComplaintLogMapper, OrgComplaintLog> implements OrgComplaintLogManager {

    @Override
    public boolean add(OrgComplaintLogDTO dto) {
        OrgComplaintLog entity = OrgComplaintLogConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'OrgComplaintLog' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'OrgComplaintLog' data: id:{}", entity.getId());
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
            log.warn("Remove 'OrgComplaintLog' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'OrgComplaintLog' data by id:{}", id);
        }
        return true;
    }


    @Override
    public OrgComplaintLogDTO getLastAcceptedLog(String complaintId) {
        return OrgComplaintLogConvert.INSTANCE.toDTO(getOne(
                new LambdaQueryWrapper<OrgComplaintLog>()
                        .eq(OrgComplaintLog::getComplaintId, complaintId)
                        .eq(OrgComplaintLog::getHandleType, ComplainActionEnum.ACCEPT.getCode())
                        .orderByDesc(OrgComplaintLog::getCreateTime)
                        .last("limit 1")
        ));
    }

    @Override
    public List<OrgComplaintLogDTO> getDtoByComplaintId(String complaintId) {
        List<OrgComplaintLog> orgComplaintLogs = baseMapper.selectList(new LambdaQueryWrapper<OrgComplaintLog>().eq(OrgComplaintLog::getComplaintId, complaintId).orderByAsc(OrgComplaintLog::getCreateTime));
        return Optional.ofNullable(orgComplaintLogs).orElse(Collections.emptyList()).stream().map(OrgComplaintLogConvert.INSTANCE::toDTO).collect(Collectors.toList());
    }


}
