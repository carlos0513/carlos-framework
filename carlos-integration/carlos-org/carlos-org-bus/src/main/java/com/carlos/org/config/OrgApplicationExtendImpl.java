package com.carlos.org.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.carlos.boot.request.RequestUtil;
import com.carlos.core.auth.UserContext;
import com.carlos.core.base.DepartmentInfo;
import com.carlos.core.base.RegionInfo;
import com.carlos.core.base.UserInfo;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.interfaces.ApplicationExtend;
import com.carlos.core.response.Result;
import com.carlos.org.api.ApiDepartment;
import com.carlos.org.api.ApiUser;
import com.carlos.org.pojo.ao.DepartmentAO;
import com.carlos.system.api.ApiRegion;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 自定义系统扩展实现
 * </p>
 *
 * @author Carlos
 * @date 2023-7-18 23:37:15
 */
@Slf4j
@AllArgsConstructor
public class OrgApplicationExtendImpl implements ApplicationExtend {


    private final ApiUser apiUser;

    private final ApiRegion apiRegion;

    @Override
    public String getUserId() {
        UserContext userContext = getUserContext();
        if (userContext == null) {
            return null;
        }
        Serializable userId = userContext.getUserId();
        if (userId == null) {
            return null;
        }
        return (String) userId;
    }

    @Override
    public @Nullable UserContext getUserContext() {
        try {
            return RequestUtil.getRequestInfo().getUserContext();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public DepartmentInfo getDepartmentByCode(String code, int limit) {
        ApiDepartment api = SpringUtil.getBean(ApiDepartment.class);
        Result<DepartmentAO> result = api.getDepartmentByCode(code);


        if (!result.getSuccess() || result.getData() == null) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            return null;
        }
        DepartmentInfo departmentInfo = new DepartmentInfo();
        DepartmentAO data = result.getData();
        if (limit >= 1) {
            Result<List<String>> listResult = api.previewDepartmentName(data.getId(), limit);
            if (listResult.getSuccess()) {
                departmentInfo.setFullName(listResult.getData());
            }
        } else {
            departmentInfo.setFullName(CollUtil.newArrayList(data.getDeptName()));
        }
        departmentInfo.setId(data.getId());
        departmentInfo.setCode(data.getDeptCode());
        departmentInfo.setName(data.getDeptName());

        return departmentInfo;
    }


    @Override
    public UserInfo getUserById(Serializable userId) {
        if (userId == null) {
            return null;
        }
        Result<UserInfo> result = apiUser.getUserById(String.valueOf(userId));

        if (!result.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        UserInfo userInfo = result.getData();
        if (userInfo == null) {
            log.warn("用户不存在： id:{}", userId);
            return null;
        }
        return userInfo;
    }

    @Override
    public RegionInfo getRegionInfo(String regionCode, Integer limit) {
        if (Objects.isNull(regionCode)) {
            return null;
        }
        Result<RegionInfo> result = apiRegion.getRegionInfo(regionCode, limit);

        if (!result.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        RegionInfo regionInfo = result.getData();
        if (Objects.isNull(regionInfo)) {
            log.warn("区域不存在： regionCode:{}", regionCode);
            return null;
        }
        return regionInfo;
    }

}
