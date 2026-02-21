package com.carlos.oauth.app.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.core.exception.ServiceException;
import com.carlos.encrypt.EncryptUtil;
import com.carlos.excel.easyexcel.ExcelUtil;
import com.carlos.oauth.app.manager.AppClientManager;
import com.carlos.oauth.app.pojo.dto.AppClientDTO;
import com.carlos.oauth.app.pojo.enums.ClientStateEnum;
import com.carlos.oauth.app.pojo.excel.AppClientExcel;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
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
            throw new ServiceException("应用名称已存在");
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
            // 保存失败的应对措施
            throw new ServiceException("添加失败！");
        }
        Serializable id = dto.getId();
        dto.setAppSecret(secret);
        // 保存完成的后续业务
        log.info("Insert 'AppClient' data: id:{}", id);
        return dto;
    }

    public void deleteAppClient(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = appClientManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    public void updateAppClient(AppClientDTO dto) {
        boolean success = appClientManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
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
            return null;
        }
        AppClientDTO dto = appClientManager.getDtoById(id);
        if (dto == null) {
            throw new ServiceException("未查询到对应的应用信息");
        }
        String secret = RandomUtil.randomString(16);
        dto = new AppClientDTO();
        dto.setId(dto.getId());
        dto.setAppSecret(EncryptUtil.encrypt(secret));
        appClientManager.modify(dto);
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
            throw new ServiceException("client id can't be null");
        }
        AppClientDTO dto = appClientManager.getDtoById(id);
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
            throw new ServiceException("client id can't be null");
        }
        AppClientDTO client = appClientManager.getByAppkey(appKey);
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
        // 表格标题，就是模型的属性名
        String name = "应用导入模板-" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);

        List<AppClientExcel> data = Lists.newArrayList();
        if (!isTemplate) {
            // 获取数据
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
            throw new ServiceException("应用信息导出失败");
        }
    }
}
