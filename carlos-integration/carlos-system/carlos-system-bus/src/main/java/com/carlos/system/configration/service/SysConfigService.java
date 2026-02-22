package com.carlos.system.configration.service;

import com.carlos.system.configration.pojo.dto.SysConfigDTO;
import com.carlos.system.configration.pojo.vo.SysConfigLoginPageVO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统配置 业务接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-3 13:47:55
 */
public interface SysConfigService {

    /**
     * 新增系统配置
     *
     * @param dto 系统配置数据
     * @author yunjin
     * @date 2022-11-3 13:47:55
     */
    void addSysConfig(SysConfigDTO dto);

    /**
     * 删除系统配置
     *
     * @param ids 系统配置id
     * @author yunjin
     * @date 2022-11-3 13:47:55
     */
    void deleteSysConfig(Set<Serializable> ids);

    /**
     * 修改系统配置信息
     *
     * @param dto 对象信息
     * @author yunjin
     * @date 2022-11-3 13:47:55
     */
    void updateSysConfig(SysConfigDTO dto);

    /**
     * 通过code获取配置项
     *
     * @param code 参数0
     * @return com.carlos.system.configration.pojo.dto.SysConfigDTO
     * @author Carlos
     * @date 2022/12/22 13:57
     */
    SysConfigDTO getByCode(String code);

    /**
     * 获取系统配置首页配置
     *
     * @return com.carlos.system.configration.pojo.vo.SysConfigLoginPageVO
     * @author yunjin
     * @date 2022-11-3 13:47:55
     */
    SysConfigLoginPageVO getHomePageConfig();

    /**
     * 获取系统配置列表
     *
     * @return java.util.List<com.carlos.system.configration.pojo.dto.SysConfigDTO>
     * @throws
     * @author Carlos
     * @date 2025-12-24 12:57
     */
    List<SysConfigDTO> listConfig();
}
