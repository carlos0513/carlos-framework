package com.carlos.system.configration.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.core.exception.BusinessException;
import com.carlos.system.configration.manager.SysConfigManager;
import com.carlos.system.configration.pojo.dto.SysConfigDTO;
import com.carlos.system.configration.pojo.vo.SysConfigLoginPageVO;
import com.carlos.system.configration.service.SysConfigService;
import com.carlos.system.enums.ConfigValueType;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 系统配置 业务接口实现类
 * </p>
 *
 * @author carlos
 * @date 2022-11-3 13:47:55
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigManager configManager;

    @Override
    public void addSysConfig(SysConfigDTO dto) {
        // 检查code是否重复
        List<SysConfigDTO> config = configManager.listByCodes(Sets.newHashSet(dto.getConfigCode()));
        if (CollUtil.isNotEmpty(config)) {
            throw new BusinessException("配置键已存在");
        }
        boolean success = configManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    @Override
    public void deleteSysConfig(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = configManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    @Override
    public void updateSysConfig(SysConfigDTO dto) {
        boolean success = configManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }

    @Override
    public SysConfigDTO getByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        List<SysConfigDTO> dto = configManager.listByCodes(Sets.newHashSet(code));
        if (CollUtil.isEmpty(dto)) {
            return null;
        }
        SysConfigDTO sysConfigDTO = dto.get(0);
        String valueType = sysConfigDTO.getValueType();
        ConfigValueType type = ConfigValueType.ofCode(Integer.valueOf(valueType));
        if (type != null) {
            if (type == ConfigValueType.FILE) {
                // TODO: Carlos 2022/12/22  如果是文件类型，将文件转换成地址
            }
        }
        return sysConfigDTO;
    }

    private static final String LOGIN_TITLE = "login_page_title";
    private static final String LOGO = "home_logo_image";
    private static final String LOGIN_BACKGROUND_IMG = "login_backgroundImg";
    private static final String TITLE = "browser_title";
    private static final String MAIN_TITLE = "system_main_title";
    private static final String SUB_TITLE = "system_sub_title";
    private static final String VERSION = "system_version";
    private static final String SUPPLIER = "system_supplier";
    private static final String ALL_KEY = "config";
    private static final Set<String> HOME = Sets.newHashSet(LOGIN_TITLE, LOGO, LOGIN_BACKGROUND_IMG, TITLE, MAIN_TITLE, SUB_TITLE, VERSION, SUPPLIER);
    private static final Cache<String, SysConfigLoginPageVO> HOME_CONFIG = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(30L, TimeUnit.MINUTES).build();
    private static final Cache<String, List<SysConfigDTO>> ALL_CONFIG = CacheBuilder.newBuilder().maximumSize(10).expireAfterAccess(30L, TimeUnit.MINUTES).build();


    @Override
    public SysConfigLoginPageVO getHomePageConfig() {
        SysConfigLoginPageVO login = HOME_CONFIG.getIfPresent(LOGIN_TITLE);
        if (login != null) {
            return login;
        }
        List<SysConfigDTO> config = configManager.listByCodes(HOME);
        if (CollUtil.isEmpty(config)) {
            return null;
        }

        login = new SysConfigLoginPageVO();
        for (SysConfigDTO item : config) {
            switch (item.getConfigCode()) {
                case LOGIN_TITLE:
                    login.setLoginTitle(item.getConfigValue());
                    break;
                case LOGO:
                    login.setLogo(item.getConfigValue());
                    break;
                case LOGIN_BACKGROUND_IMG:
                    login.setBackgroundImg(item.getConfigValue());
                    break;
                case TITLE:
                    login.setTitle(item.getConfigValue());
                    break;
                case MAIN_TITLE:
                    login.setMainTitle(item.getConfigValue());
                    break;
                case SUB_TITLE:
                    login.setSubTitle(item.getConfigValue());
                    break;
                case VERSION:
                    login.setVersion(item.getConfigValue());
                    break;
                case SUPPLIER:
                    login.setSupplier(item.getConfigValue());
                    break;
                default:
                    break;
            }
        }
        HOME_CONFIG.put(LOGIN_TITLE, login);
        return login;
    }

    @Override
    public List<SysConfigDTO> listConfig() {
        List<SysConfigDTO> configs = ALL_CONFIG.getIfPresent(ALL_KEY);
        if (CollUtil.isNotEmpty(configs)) {
            return configs;
        }
        Set<String> sets = new HashSet<>(HOME);
        sets.remove(TITLE);
        configs = configManager.getAllConfig(sets);
        ALL_CONFIG.put(ALL_KEY, configs);
        return configs;
    }
}
