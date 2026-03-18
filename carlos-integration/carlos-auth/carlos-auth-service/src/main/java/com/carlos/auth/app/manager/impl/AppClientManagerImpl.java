package com.carlos.auth.app.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.auth.app.convert.AppClientConvert;
import com.carlos.auth.app.manager.AppClientManager;
import com.carlos.auth.app.mapper.AppClientMapper;
import com.carlos.auth.app.pojo.dto.AppClientDTO;
import com.carlos.auth.app.pojo.entity.AppClient;
import com.carlos.auth.app.pojo.enums.ClientStateEnum;
import com.carlos.auth.app.pojo.param.AppClientPageParam;
import com.carlos.auth.app.pojo.vo.AppClientPageVO;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.redis.ICacheManager;
import com.carlos.redis.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;

/**
 * <p>
 * 应用信息 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2025-3-12 14:00:14
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AppClientManagerImpl extends BaseServiceImpl<AppClientMapper, AppClient> implements AppClientManager, ICacheManager<AppClientDTO> {
    /**
     * 第一个参数 id 第二个参数 appKey
     */
    public static final String CACHE_KEY = "oauth2:client:%s:%s";

    @Override
    public boolean add(AppClientDTO dto) {
        AppClient entity = AppClientConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'AppClient' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        putCache(dto);
        if (log.isDebugEnabled()) {
            log.debug("Insert 'AppClient' data: id:{}", entity.getId());
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
            log.warn("Remove 'AppClient' data fail, id:{}", id);
            return false;
        }
        RedisUtil.delete(this.generateKey(id, RedisUtil.ALL));
        if (log.isDebugEnabled()) {
            log.debug("Remove 'AppClient' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(AppClientDTO dto) {
        AppClient entity = AppClientConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'AppClient' data fail, entity:{}", entity);
            return false;
        }
        updateCache(dto);
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'AppClient' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public AppClientDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }

        AppClientDTO client = getCache(id, RedisUtil.ALL);
        if (client == null) {
            AppClient entity = getBaseMapper().selectById(id);
            client = AppClientConvert.INSTANCE.toDTO(entity);
            putCache(client);
        }
        return client;
    }

    @Override
    public Paging<AppClientPageVO> getPage(AppClientPageParam param) {
        LambdaQueryWrapper<AppClient> wrapper = queryWrapper();
        wrapper.select(
            AppClient::getId,
            AppClient::getAppKey,
            AppClient::getAppName,
            AppClient::getAppLogo,
            AppClient::getClientSecretExpiresAt,
            AppClient::getClientIssuedAt,
            AppClient::getAuthenticationMethods,
            AppClient::getAuthorizationGrantTypes,
            AppClient::getScopes,
            AppClient::getRedirectUris,
            AppClient::getState,
            AppClient::getCreateBy,
            AppClient::getCreateTime,
            AppClient::getUpdateBy,
            AppClient::getUpdateTime
        );
        wrapper.eq(StrUtil.isNotBlank(param.getAppKey()), AppClient::getAppKey, param.getAppKey());
        wrapper.like(StrUtil.isNotBlank(param.getAppName()), AppClient::getAppName, param.getAppName());
        wrapper.eq(ObjectUtil.isNotNull(param.getClientSecretExpiresAt()), AppClient::getClientSecretExpiresAt, param.getClientSecretExpiresAt());
        wrapper.eq(ObjectUtil.isNotNull(param.getClientIssuedAt()), AppClient::getClientIssuedAt, param.getClientIssuedAt());
        PageInfo<AppClient> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, AppClientConvert.INSTANCE::toVO);
    }

    @Override
    public List<AppClientDTO> listByKeyword(String keyword) {
        LambdaQueryWrapper<AppClient> wrapper = queryWrapper();
        wrapper.select(
            AppClient::getId,
            AppClient::getAppKey,
            AppClient::getAppSecret,
            AppClient::getAppName,
            AppClient::getAppLogo,
            AppClient::getClientSecretExpiresAt,
            AppClient::getClientIssuedAt,
            AppClient::getAuthenticationMethods,
            AppClient::getAuthorizationGrantTypes,
            AppClient::getState
        );
        wrapper.like(StrUtil.isNotBlank(keyword), AppClient::getAppName, keyword);
        wrapper.like(StrUtil.isNotBlank(keyword), AppClient::getAppKey, keyword);

        return AppClientConvert.INSTANCE.toDTO(this.list(wrapper));
    }

    @Override
    public AppClientDTO getByAppkey(String appKey) {
        if (appKey == null) {
            log.warn("appKey is null");
            return null;
        }
        AppClientDTO client = getCache(RedisUtil.ALL, appKey);
        if (client == null) {
            AppClient entity = lambdaQuery().eq(AppClient::getAppKey, appKey).one();
            client = AppClientConvert.INSTANCE.toDTO(entity);
            putCache(client);
        }
        return client;
    }

    @Override
    public AppClientDTO getByClientName(String appName) {
        if (appName == null) {
            log.warn("appName is null");
            return null;
        }
        List<AppClient> entity = lambdaQuery().eq(AppClient::getAppName, appName).list();
        if (CollUtil.isNotEmpty(entity)) {
            return AppClientConvert.INSTANCE.toDTO(entity.get(0));
        }
        return null;
    }

    @Override
    public void initCache() {
    }

    @Override
    public void putCache(AppClientDTO bean) {
        RedisUtil.setValue(this.generateKey(bean.getId(), bean.getAppKey()), bean, Duration.ofDays(1L));
    }

    @Override
    public void updateCache(AppClientDTO bean) {
        RedisUtil.setValue(this.generateKey(bean.getId(), bean.getAppKey()), bean);
    }

    @Override
    public AppClientDTO getCache(Serializable... keys) {
        List<AppClientDTO> clients = RedisUtil.getValueListByPattern(this.generateKey(keys));
        if (clients == null || clients.isEmpty()) {
            return null;
        }
        return clients.get(0);
    }

    @Override
    public void deleteCache(AppClientDTO bean) {
        RedisUtil.delete(this.generateKey(bean.getId(), bean.getAppKey()));
    }

    @Override
    public String generateKey(Serializable... keys) {
        return String.format(CACHE_KEY, keys);
    }

    @Override
    public List<AppClientDTO> listAll() {
        LambdaQueryWrapper<AppClient> wrapper = queryWrapper();
        wrapper.select(
            AppClient::getId,
            AppClient::getAppKey,
            AppClient::getAppName,
            AppClient::getAppLogo,
            AppClient::getClientSecretExpiresAt,
            AppClient::getClientIssuedAt,
            AppClient::getAuthenticationMethods,
            AppClient::getAuthorizationGrantTypes,
            AppClient::getState
        );
        wrapper.eq(AppClient::getState, ClientStateEnum.ENABLE);
        return AppClientConvert.INSTANCE.toDTO(this.list(wrapper));
    }
}
