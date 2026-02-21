package com.yunjin.metric;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.core.response.Result;
import com.yunjin.log.api.ApiCommonLog;
import com.yunjin.log.pojo.ao.LoginLogAO;
import com.yunjin.log.pojo.param.ApiLoginLogQueryParam;
import com.yunjin.org.UserUtil;
import com.yunjin.org.manager.UserManager;
import com.yunjin.org.metric.OrgMetric;
import com.yunjin.org.metric.OrgMetricEnum;
import com.yunjin.org.pojo.ao.UserLoginAO;
import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.pojo.enums.UserStateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 指标服务
 * </p>
 *
 * @author Carlos
 * @date 2025-05-19 23:16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgMetricService {

    private final UserManager userManager;

    /**
     * <p>
     * 获取指标数据
     * </p>
     *
     * @author Carlos
     * @date 2025-05-19 23:19
     */
    public OrgMetric getMetric(Map<OrgMetricEnum, Object> metrics) {
        OrgMetric me = new OrgMetric();
        List<UserDTO> users;
/*        if (UserUtil.isSysAdminGroup()) {
            List<User> list = userManager.lambdaQuery().select(User::getAccount, User::getState, User::getId).list();
            users = UserConvert.INSTANCE.toDTO(list);

        } else {

            // 获取部门及下级的所有用户
            UserLoginAO.Department department = UserUtil.getDepartment();
            String departmentId = department.getId();
            String deptCode = department.getDeptCode();
            users = userManager.listUserByDeptCode(deptCode);
        }*/

        // 根据用户角色获取对应的用户列表
        if (UserUtil.isSysAdminGroup()) {
            // 系统管理员：查询全部用户
            users = userManager.listAllWithSelectedFields();
        } else {
            // 其他用户：查询当前部门及下级部门的用户
            UserLoginAO.Department department = UserUtil.getDepartment();
            if (department != null) {
                users = userManager.listIncludeSubUser(department.getId());
            } else {
                log.error("current user department is empty");
                users = new ArrayList<>();
            }
        }

        if (CollUtil.isEmpty(users)) {
            users = new ArrayList<>();
        }

        // 查询近一个月的登录日志
        ApiCommonLog logApi = SpringUtil.getBean(ApiCommonLog.class);
        ApiLoginLogQueryParam param = new ApiLoginLogQueryParam();
        // 查询进一个月
        param.setStart(LocalDateTime.now().minusMonths(1));
        param.setEnd(LocalDateTime.now());

        Result<List<LoginLogAO>> res = logApi.queryLoginLog(param);
        List<LoginLogAO> logs;
        if (!res.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", res.getMessage(), res.getStack());
            logs = new ArrayList<>();
        } else {
            logs = res.getData();
        }
        Map<String, UserDTO> accountMap = users.stream().collect(Collectors.toMap(UserDTO::getAccount, i -> i, (user1, user2) -> user1));
        Map<String, List<LoginLogAO>> collect = logs.stream().filter(log -> accountMap.get(log.getAccount()) != null).collect(Collectors.groupingBy(LoginLogAO::getAccount));

        for (Map.Entry<OrgMetricEnum, Object> entry : metrics.entrySet()) {
            OrgMetricEnum metric = entry.getKey();
            switch (metric) {
                case registerCount:
                    long registerCount = users.stream()
                            .filter(user -> UserStateEnum.DELETE != user.getState())
                            .count();
                    me.setRegisterCount((int) registerCount);
                    break;
                case disableCount:
                    me.setDisableCount((int) users.stream().filter(user -> UserStateEnum.DISABLE.equals(user.getState()) || UserStateEnum.DELETE.equals(user.getState()) || UserStateEnum.LOCK.equals(user.getState())).count());
                    break;
                case activeCount:
                    me.setActiveCount(collect.size());
                    break;
                case pcActiveCount:
                    Map<String, List<LoginLogAO>> pc = logs.stream().filter(log -> accountMap.get(log.getAccount()) != null && !StrUtil.containsIgnoreCase(log.getClient(), "mobile")).collect(Collectors.groupingBy(LoginLogAO::getAccount));
                    me.setPcActiveCount(pc.size());
                    break;
                case mobileActiveCount:
                    Map<String, List<LoginLogAO>> mobile = logs.stream().filter(log -> accountMap.get(log.getAccount()) != null && StrUtil.containsIgnoreCase(log.getClient(), "mobile")).collect(Collectors.groupingBy(LoginLogAO::getAccount));
                    me.setMobileActiveCount(mobile.size());
                    break;
                case registerCountInOneYear:
                    LocalDate today = LocalDate.now();
                    LocalDate oneYearAgo = today.minusYears(1);
                    me.setRegisterCountInOneYear((int) users.stream()
                            .filter(user -> UserStateEnum.DELETE != user.getState())
                            .filter(user -> {
                                LocalDate createDate = user.getCreateTime().toLocalDate();
                                return createDate.compareTo(oneYearAgo) >= 0 && createDate.compareTo(today) <= 0;
                            })
                            .count());
                    break;
                default:
                    break;
            }
        }
        return me;
    }


}
