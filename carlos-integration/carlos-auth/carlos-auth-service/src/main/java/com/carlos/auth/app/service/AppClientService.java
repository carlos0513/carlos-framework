package com.carlos.auth.app.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.auth.api.enums.AuthErrorCode;
import com.carlos.auth.app.manager.AppClientManager;
import com.carlos.auth.app.pojo.dto.AppClientDTO;
import com.carlos.auth.app.pojo.enums.ClientStateEnum;
import com.carlos.auth.app.pojo.excel.AppClientExcel;
import com.carlos.encrypt.EncryptUtil;
import com.carlos.util.easyexcel.ExcelUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 应用信息 业务
 * </p>
 *
 * @author Carlos
 * @date 2025-3-12 14:00:14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppClientService {

    private final AppClientManager appClientManager;

    public AppClientDTO addAppClient(AppClientDTO dto) {
        // 检查名称是否重复
        AppClientDTO client = appClientManager.getByClientName(dto.getAppName());
        if (client != null) {
            throw AuthErrorCode.AUTH_CLIENT_NAME_EXISTS.exception();
        }

        // 生成appKey和秘钥
        dto.setAppKey(IdUtil.randomUUID());
        String secret = RandomUtil.randomString(16);
        dto.setAppSecret(EncryptUtil.encrypt(secret));
        dto.setState(ClientStateEnum.ENABLE);

        if (CollUtil.isEmpty(dto.getAuthenticationMethods())) {
            dto.setAuthenticationMethods(Sets.newHashSet(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()));
        }
        if (CollUtil.isEmpty(dto.getAuthorizationGrantTypes())) {
            dto.setAuthorizationGrantTypes(Sets.newHashSet(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()));
        }
        boolean success = appClientManager.add(dto);
        if (!success) {
            throw AuthErrorCode.AUTH_CLIENT_ADD_FAILED.exception();
        }
        Serializable id = dto.getId();
        dto.setAppSecret(secret);
        log.info("Insert 'AppClient' data: id:{}", id);
        return dto;
    }

    public void deleteAppClient(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = appClientManager.delete(id);
            if (!success) {
                log.warn("Failed to delete AppClient, id: {}", id);
                continue;
            }
            log.info("Delete 'AppClient' data: id:{}", id);
        }
    }

    public void updateAppClient(AppClientDTO dto) {
        if (dto.getId() == null) {
            throw AuthErrorCode.AUTH_PARAM_ID_MISSING.exception();
        }
        boolean success = appClientManager.modify(dto);
        if (!success) {
            throw AuthErrorCode.AUTH_CLIENT_UPDATE_FAILED.exception();
        }
        log.info("Update 'AppClient' data: id:{}", dto.getId());
    }

    /**
     * 重置秘钥
     *
     * @param id id
     * @return java.lang.String
     * @author Carlos
     * @date 2025-03-14 18:16
     */
    public String resetSecret(Serializable id) {
        if (id == null) {
            throw AuthErrorCode.AUTH_PARAM_ID_MISSING.exception();
        }
        AppClientDTO dto = appClientManager.getDtoById(id);
        if (dto == null) {
            throw AuthErrorCode.AUTH_CLIENT_NOT_FOUND.exception();
        }
        String secret = RandomUtil.randomString(16);
        AppClientDTO updateDto = new AppClientDTO();
        updateDto.setId(dto.getId());
        updateDto.setAppSecret(EncryptUtil.encrypt(secret));
        boolean success = appClientManager.modify(updateDto);
        if (!success) {
            throw AuthErrorCode.AUTH_CLIENT_UPDATE_FAILED.exception("重置密钥失败");
        }
        log.info("Reset client secret success, appKey:{}", dto.getAppKey());
        return secret;
    }


    /**
     * 根据id获取应用id
     *
     * @param id        应用id
     * @param deEncrypt 是否解密  解密：true
     * @return com.carlos.oauth.client.pojo.dto.AppClientDTO
     * @author Carlos
     * @date 2025-03-12 14:20
     */
    public AppClientDTO findById(Serializable id, boolean deEncrypt) {
        if (ObjectUtil.isEmpty(id)) {
            throw AuthErrorCode.AUTH_PARAM_ID_MISSING.exception();
        }
        AppClientDTO dto = appClientManager.getDtoById(id);
        if (dto == null) {
            throw AuthErrorCode.AUTH_CLIENT_NOT_FOUND.exception();
        }
        if (deEncrypt) {
            dto.setAppSecret(EncryptUtil.decrypt(dto.getAppSecret()));
        }
        return dto;
    }


    /**
     * 根据appKey获取应用信息
     *
     * @param appKey    key
     * @param deEncrypt 是否解密  解密：true
     * @return com.carlos.oauth.app.pojo.dto.AppClientDTO
     * @author Carlos
     * @date 2025-04-15 15:14
     */
    public AppClientDTO findByAppkey(String appKey, boolean deEncrypt) {
        if (ObjectUtil.isEmpty(appKey)) {
            throw AuthErrorCode.AUTH_PARAM_CLIENT_ID_MISSING.exception();
        }
        AppClientDTO client = appClientManager.getByAppkey(appKey);
        if (client == null) {
            throw AuthErrorCode.AUTH_CLIENT_NOT_FOUND.exception();
        }
        if (deEncrypt) {
            client.setAppSecret(EncryptUtil.decrypt(client.getAppSecret()));
        }
        return client;
    }

    /**
     * 获取应用列表
     *
     * @param keyword 查询关键字
     * @return java.util.List<com.carlos.oauth.client.pojo.dto.AppClientDTO>
     * @author Carlos
     * @date 2025-03-26 10:33
     */
    public List<AppClientDTO> list(String keyword) {
        List<AppClientDTO> apps = appClientManager.listByKeyword(keyword);
        apps.forEach(item -> {
            item.setAppSecret(EncryptUtil.decrypt(item.getAppSecret()));
        });
        return apps;
    }


    /**
     * 导出
     *
     * @param response   响应
     * @param isTemplate 是否仅导出模板
     * @author Carlos
     * @date 2025-03-27 13:42
     */
    public void export(HttpServletResponse response, boolean isTemplate) {
        String name = "应用导入模板-" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);

        List<AppClientExcel> data = Lists.newArrayList();
        if (!isTemplate) {
            List<AppClientDTO> list = appClientManager.listAll();
            for (AppClientDTO dto : list) {
                AppClientExcel excel = new AppClientExcel();
                excel.setAppKey(dto.getAppKey());
                excel.setAppName(dto.getAppName());
                excel.setAppLogo(dto.getAppLogo());
                excel.setAppSecret(dto.getAppSecret());
                excel.setClientSecretExpiresAt(dto.getClientSecretExpiresAt());
                excel.setClientIssuedAt(dto.getClientIssuedAt());
                excel.setAuthenticationMethods(StrUtil.join(StrUtil.COMMA, dto.getAuthenticationMethods()));
                excel.setAuthorizationGrantTypes(StrUtil.join(StrUtil.COMMA, dto.getAuthorizationGrantTypes()));
                excel.setScopes(StrUtil.join(StrUtil.COMMA, dto.getScopes()));
                excel.setRedirectUris(StrUtil.join(StrUtil.COMMA, dto.getRedirectUris()));
                data.add(excel);
            }
        }
        try {
            ExcelUtil.download(response, name, AppClientExcel.class, data);
        } catch (Exception e) {
            log.error("导出应用客户端失败", e);
            throw AuthErrorCode.AUTH_CLIENT_EXPORT_FAILED.exception();
        }
    }
}
