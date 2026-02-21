package com.carlos.system.configration.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.system.configration.pojo.dto.SysConfigDTO;
import com.carlos.system.configration.pojo.entity.SysConfig;
import com.carlos.system.configration.pojo.param.SysConfigPageParam;
import com.carlos.system.configration.pojo.vo.SysConfigVO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统配置 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-3 13:47:54
 */
public interface SysConfigManager extends BaseService<SysConfig> {

    /**
     * 新增系统配置
     *
     * @param dto 系统配置数据
     * @return boolean
     * @author yunjin
     * @date 2022-11-3 13:47:54
     */
    boolean add(SysConfigDTO dto);

    /**
     * 删除系统配置
     *
     * @param id 系统配置id
     * @return boolean
     * @author yunjin
     * @date 2022-11-3 13:47:54
     */
    boolean delete(Serializable id);

    /**
     * 修改系统配置信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2022-11-3 13:47:54
     */
    boolean modify(SysConfigDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.system.configration.pojo.dto.SysConfigDTO
     * @author yunjin
     * @date 2022-11-3 13:47:54
     */
    SysConfigDTO getDtoById(String id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author yunjin
     * @date 2022-11-3 13:47:54
     */
    Paging<SysConfigVO> getPage(SysConfigPageParam param);

    /**
     * 获取全部配置
     *
     * @return com.carlos.system.configration.pojo.dto.SysConfigDTO
     * @author yunjin
     * @date 2023/05/31 11:00
     */
    List<SysConfigDTO> getAllConfig(Set<String> excludes);

    /**
     * 根据code批量获取配置
     *
     * @return com.carlos.system.configration.pojo.dto.SysConfigDTO
     * @author yunjin
     * @date 2023/05/31 11:00
     */
    List<SysConfigDTO> listByCodes(Set<String> codes);
}
