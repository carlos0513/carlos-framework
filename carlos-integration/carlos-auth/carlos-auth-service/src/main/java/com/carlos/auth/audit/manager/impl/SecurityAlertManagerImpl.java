package com.carlos.auth.audit.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carlos.auth.audit.manager.SecurityAlertManager;
import com.carlos.auth.audit.mapper.SecurityAlertMapper;
import com.carlos.auth.audit.pojo.entity.SecurityAlert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 安全告警 Manager 实现
 * </p>
 *
 * <p>数据查询封装层，与 Mapper 层交互，处理数据持久化</p>
 *
 * @author Carlos
 * @date 2026-04-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityAlertManagerImpl extends ServiceImpl<SecurityAlertMapper, SecurityAlert> implements SecurityAlertManager {

    private final SecurityAlertMapper securityAlertMapper;

    @Override
    public List<SecurityAlert> selectByUserId(Long userId) {
        return securityAlertMapper.selectByUserId(userId);
    }

    @Override
    public List<SecurityAlert> selectUnhandled() {
        return securityAlertMapper.selectUnhandled();
    }
}
