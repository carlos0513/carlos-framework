package com.carlos.org.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.org.pojo.dto.OrgDepartmentDTO;
import com.carlos.org.pojo.dto.OrgDepartmentUserDTO;
import com.carlos.org.pojo.dto.OrgUserDTO;
import com.carlos.org.pojo.entity.OrgDepartment;
import com.carlos.org.pojo.param.OrgDepartmentPageParam;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 部门 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
public interface OrgDepartmentManager extends BaseService<OrgDepartment> {

    /**
     * 新增部门
     *
     * @param dto 部门数据
     * @return boolean
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    boolean add(OrgDepartmentDTO dto);

    /**
     * 删除部门
     *
     * @param id 部门id
     * @return boolean
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    boolean delete(Serializable id);

    /**
     * 修改部门信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    boolean modify(OrgDepartmentDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.org.pojo.dto.OrgDepartmentDTO
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgDepartmentDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @return Paging<OrgDepartmentDTO>
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    Paging<OrgDepartmentDTO> getPage(OrgDepartmentPageParam param);

    /**
     * 获取所有部门
     *
     * @return List<OrgDepartmentDTO>
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    List<OrgDepartmentDTO> listAll();

    /**
     * 根据部门编号获取部门
     *
     * @param deptCode 部门编号
     * @return OrgDepartmentDTO
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgDepartmentDTO getByDeptCode(String deptCode);

    /**
     * 获取子部门列表
     *
     * @param parentId 父部门ID
     * @return List<OrgDepartmentDTO>
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    List<OrgDepartmentDTO> getChildrenByParentId(Serializable parentId);

    /**
     * 获取部门下的用户列表
     *
     * @param deptId 部门ID
     * @return List<OrgUserDTO>
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    List<OrgUserDTO> getUsersByDeptId(Serializable deptId);

    /**
     * 分页获取部门下的用户列表
     *
     * @param deptId 部门ID
     * @param param  分页参数
     * @return Paging<OrgDepartmentUserDTO>
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    Paging<OrgDepartmentUserDTO> getUsersByDeptId(Serializable deptId, OrgDepartmentPageParam param);

    /**
     * 初始化缓存
     *
     * @author Carlos
     * @date 2026-04-29
     */
    void initCache();

    /**
     * 清空缓存
     *
     * @return 删除的key数量
     * @author Carlos
     * @date 2026-04-29
     */
    long clearCache();

    /**
     * 获取指定部门的祖先链（自顶向下）
     *
     * @param id    部门主键
     * @param limit 限制层级，<=0 表示不限制
     * @return 祖先 id 列表
     * @author Carlos
     * @date 2026-04-29
     */
    List<Long> getAncestorIdsFromCache(Long id, long limit);

    /**
     * 获取指定部门的全部子孙 id 集合
     *
     * @param id 部门主键
     * @return 子孙 id 集合
     * @author Carlos
     * @date 2026-04-29
     */
    Set<Long> getDescIdsFromCache(Long id);

    /**
     * 从缓存中批量获取部门对象（按需指定字段）
     *
     * @param ids    部门 id 列表
     * @param fields 指定字段，null 表示全部
     * @return 部门 DTO 列表
     * @author Carlos
     * @date 2026-04-29
     */
    List<OrgDepartmentDTO> listDeptFromCache(List<Long> ids, List<String> fields);

}
