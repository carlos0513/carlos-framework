package com.carlos.org.apiimpl;


import cn.hutool.core.util.StrUtil;
import com.carlos.core.response.Result;
import com.carlos.org.api.ApiOrgUser;
import com.carlos.org.api.pojo.ao.OrgUserAO;
import com.carlos.org.convert.OrgUserConvert;
import com.carlos.org.manager.OrgUserManager;
import com.carlos.org.pojo.dto.OrgUserDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统用户 api接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/user")
@Tag(name = "系统用户Feign接口")
public class ApiOrgUserImpl implements ApiOrgUser {

    private final OrgUserManager orgUserManager;


    @Override
    public Result<OrgUserAO> getUserByIdentifier(String identifier) {
        if (StrUtil.isBlank(identifier)) {
            return Result.success(null);
        }

        OrgUserDTO user = orgUserManager.getUserByAccount(identifier);
        if (user == null) {
            user = orgUserManager.getUserByPhone(identifier);
        }

        return Result.success(OrgUserConvert.INSTANCE.toAO(user));
    }

}
