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

}
