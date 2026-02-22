package com.carlos.org.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.org.pojo.dto.RoleDTO;
import com.carlos.org.pojo.entity.Role;
import com.carlos.org.pojo.param.RolePageParam;
import com.carlos.org.pojo.vo.RolePageVO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统角色 查询封装接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
public interface RoleManager extends BaseService<Role> {

    /**
     * 新增系统角色
     *
     * @param dto 系统角色数据
     * @return boolean
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    boolean add(RoleDTO dto);

    /**
     * 删除系统角色
     *
     * @param id 系统角色id
     * @return boolean
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    boolean delete(Serializable id);

    /**
     * 修改系统角色信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    boolean modify(RoleDTO dto);


    /**
     * 根据名称获取角色
     *
     * @param name 角色名称
     * @return com.carlos.org.dto.user.RoleDTO
     * @author Carlos
     * @date 2022/12/20 17:40
     */
    RoleDTO getByName(String name);

    /**
     * 根据code获取角色
     *
     * @param code 角色编码
     * @return com.carlos.org.dto.user.RoleDTO
     * @author Carlos
     * @date 2022/12/21 13:49
     */
    RoleDTO getByCode(String code);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.org.dto.user.RoleDTO
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    RoleDTO getDtoById(Serializable id);

    /**
     * 获取数据详情
     *
     * @param ids 主键id
     * @return com.carlos.org.dto.user.RoleDTO
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    List<RoleDTO> getDtoByIds(Set<String> ids);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    Paging<RolePageVO> getPage(RolePageParam param);

    /**
     * 获取所有角色
     *
     * @param name 角色名称
     * @return java.util.List<com.carlos.org.dto.user.RoleDTO>
     * @author Carlos
     * @date 2022/12/21 18:00
     */
    List<RoleDTO> listAll(String name);

    /**
     * 获取最新的一条数据
     *
     * @return com.carlos.org.dto.user.RoleDTO
     * @author Carlos
     * @date 2023/1/16 9:47
     */
    RoleDTO getLatestOne();
}
