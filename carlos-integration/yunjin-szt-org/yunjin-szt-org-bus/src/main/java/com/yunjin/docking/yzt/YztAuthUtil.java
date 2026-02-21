package com.yunjin.docking.yzt;

import com.yunjin.core.exception.ServiceException;
import com.yunjin.docking.yzt.result.R;
import com.yunjin.docking.yzt.result.YztUserDetail;
import com.yunjin.docking.yzt.result.YztUserInfo;
import com.yunjin.docking.yzt.service.YztService;
import com.yunjin.org.pojo.dto.OrgDockingMappingDTO;
import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.pojo.enums.OrgDockingTypeEnum;
import com.yunjin.org.service.OrgDockingMappingService;
import com.yunjin.org.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class YztAuthUtil {

    private static YztService yztService;

    private static OrgDockingMappingService orgDockingMappingService;

    private static UserService userService;


    public YztAuthUtil(
            YztService yztService,
            OrgDockingMappingService orgDockingMappingService,
            UserService userService
    ) {
        YztAuthUtil.yztService = yztService;
        YztAuthUtil.orgDockingMappingService = orgDockingMappingService;
        YztAuthUtil.userService = userService;
    }

    public static String auth(String sourceType, String code) {
        // 获取用户信息
        R<YztUserInfo> response = yztService.getUserInfo(sourceType, code);
        if (!response.isSuccess()) throw new ServiceException(response.getMsg());

        YztUserInfo yztUserInfo = response.getData();
        String userId = yztUserInfo.getUserId();

        // 先判断是否存在映射关系
        OrgDockingMappingDTO orgDockingMappingDTO = orgDockingMappingService.getDockingMappingByTargetId(userId);
        if (Objects.isNull(orgDockingMappingDTO)) {
            // 获取用户详情
            R<YztUserDetail> detailResponse = yztService.getUserDetail(sourceType, userId);
            if (!detailResponse.isSuccess()) throw new ServiceException(detailResponse.getMsg());
            YztUserDetail userDetail = detailResponse.getData();
            // 获取用户手机号
            String mobile = userDetail.getMobile();
            // 没有就先根据手机号查询用户
            UserDTO user = userService.getUserByPhone(mobile);
            if (Objects.isNull(user)) {
                throw new ServiceException("用户未注册");
            }
            OrgDockingMappingDTO dto = new OrgDockingMappingDTO();
            dto.setSystemId(user.getId());
            dto.setTargetId(userId);
            dto.setTargetCode(userId);
            dto.setDockingType(OrgDockingTypeEnum.USER);
            orgDockingMappingService.addOrgDockingMapping(dto);

            return mobile;
        } else {
            // 存在的话查询映射关系
            UserDTO user = userService.selectUserById(orgDockingMappingDTO.getSystemId());
            if (Objects.isNull(user)) {
                throw new ServiceException("用户未注册");
            }
            return user.getPhone();
        }
    }

}
