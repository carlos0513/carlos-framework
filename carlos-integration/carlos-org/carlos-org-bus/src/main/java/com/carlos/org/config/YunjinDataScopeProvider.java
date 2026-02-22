package com.carlos.org.config;

import com.carlos.core.interfaces.ApplicationExtend;
import com.carlos.datascope.DataScopeProvider;
import com.carlos.org.service.impl.UserScopeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 权限数据处理器
 * </p>
 *
 * @author Carlos
 * @date 2022/11/22 13:32
 */
//@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(ApplicationExtend.class)
public class carlosDataScopeProvider implements DataScopeProvider {

    private final ApplicationExtend applicationExtend;
    private final UserScopeService scopeService;


    @Override
    public Serializable currentUserId() {
        return applicationExtend.getUserId();
    }

    @Override
    public Set<Serializable> currentRoleUserIds() {
        return scopeService.getCurrentRoleUserId();
    }

    @Override
    public Set<Serializable> currentDeptUserIds() {
        return scopeService.getCurrentDeptUserId();
    }

    @Override
    public Set<Serializable> currentDeptAllUserIds(Serializable departmentId) {
        return scopeService.getCurrentDeptAllUserId(departmentId);
    }

    @Override
    public Set<Serializable> currentDeptAllIds(Serializable departmentId) {

        return scopeService.getCurrentDeptTreeIds(departmentId);
    }

    @Override
    public Set<String> currentRegionAllCodes() {
        return scopeService.getCurrentRegionTreeIds();
    }
}
