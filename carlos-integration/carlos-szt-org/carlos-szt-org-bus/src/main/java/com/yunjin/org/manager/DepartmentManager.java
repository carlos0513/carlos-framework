package com.yunjin.org.manager;

import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseService;
import com.yunjin.org.pojo.dto.DepartmentDTO;
import com.yunjin.org.pojo.dto.UserDepartmentDTO;
import com.yunjin.org.pojo.entity.Department;
import com.yunjin.org.pojo.param.DepartmentPageParam;
import com.yunjin.org.pojo.vo.DepartmentVO;
import com.yunjin.org.pojo.vo.ThirdDepartmentVO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 部门 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
public interface DepartmentManager extends BaseService<Department> {

    /**
     * 新增部门
     *
     * @param dto 部门数据
     * @return boolean
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    boolean addOrUpdate(DepartmentDTO dto);

    /**
     * 删除部门
     *
     * @param id 部门id
     * @return boolean
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    boolean delete(Serializable id);

    /**
     * 批量删除
     *
     * @param departmentIds 参数0
     * @return boolean
     * @author Carlos
     * @date 2023/1/17 13:27
     */
    boolean deleteByIds(Set<Serializable> departmentIds);

    /**
     * 修改部门信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    boolean modify(DepartmentDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.yunjin.org.dto.user.DepartmentDTO
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    DepartmentDTO getDtoById(String id);


    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    Paging<DepartmentVO> getPage(DepartmentPageParam param);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    Paging<ThirdDepartmentVO> getThirdPage(DepartmentPageParam param,Set<String> ids);

    /**
     * 根据部门名称获取部门信息
     *
     * @param parentId 上级部门id
     * @param deptName 部门名称
     * @author yunjin
     * @date 2022-11-11 19:01:17
     */
    DepartmentDTO getDepartmentByName(String parentId, String deptName);

    /**
     * 根据部门ids获取部门信息
     *
     * @param deptIds 部门ids
     * @author yunjin
     * @date 2022-11-11 19:01:17
     */
    List<DepartmentVO> getDepartments(List<String> deptIds);

    /**
     * 根据父级id获取部门信息
     *
     * @param parentId 部门父级id
     * @author yunjin
     * @date 2022-11-14 17:01:17
     */
    List<DepartmentDTO> getDepartmentsByParentId(String parentId);

    /**
     * 列出该部门及所有子部门
     *
     * @param id 部门id
     * @author Carlos
     * @date 2022/11/23 13:03
     */
    void listAllDepartment(Serializable id, Set<Serializable> ids);


    /**
     * 根据code获取部门信息
     *
     * @param deptCode 参数1
     * @return com.yunjin.org.dto.user.DepartmentDTO
     * @author Carlos
     * @date 2022/11/30 23:18
     */
    DepartmentDTO getDepartmentByCode(String deptCode);

    /**
     * 获取部门父级
     *
     * @param id    参数0
     * @param limit 参数1
     * @return java.util.List<com.yunjin.org.dto.user.DepartmentDTO>
     * @author Carlos
     * @date 2022/12/20 15:26
     */
    List<DepartmentDTO> getParentDepartment(String id, Integer limit);

    /**
     * 批量获取当前部门和上级部门
     *
     * @param ids   部门ID列表
     * @param limit 限制层级
     * @return Map<部门ID, 上级部门列表>
     */
    Map<String, List<DepartmentDTO>> getParentDepartmentMap(Set<String> ids, Integer limit);

    /**
     * 获取部门父级
     *
     * @param code    参数0
     * @param limit 参数1
     * @return java.util.List<com.yunjin.org.dto.user.DepartmentDTO>
     * @author Carlos
     * @date 2022/12/20 15:26
     */
    List<DepartmentDTO> getParentDepartmentByCode(String code, Integer limit);

    /**
     * 批量获取部门信息
     *
     * @param ids 参部门id数0
     * @return java.util.List<com.yunjin.org.dto.user.DepartmentDTO>
     * @author Carlos
     * @date 2022/12/21 16:04
     */
    List<DepartmentDTO> getByIds(Set<String> ids);

    /**
     * 获取所有部门
     *
     * @return java.util.List<com.yunjin.org.dto.user.DepartmentDTO>
     * @author Carlos
     * @date 2022/12/22 17:11
     */
    List<DepartmentDTO> listAll();

    /**
     * 获取所有部门(查数据库)
     * @return
     */
    List<DepartmentDTO> getAllDepartment();

    /**
     * @Title: addBatch
     * @Description: TODO
     * @Date: 2023/2/21 16:43
     * @Parameters: [departments]
     * @Return void
     */
    boolean addBatch(List<DepartmentDTO> departments);

    /**
     * 初始化缓存
     *
     * @author Carlos
     * @date 2023/2/26 11:06
     */
    void initCache();

    /**
     * 获取子级部门总数
     *
     * @param parentId 父id
     * @author Carlos
     * @date 2023/4/2 14:43
     */
    Long getSubCount(String parentId);

    Long getDeptCodeCountWithDeleted(Integer level,String parentCode);

    /**
     * 获取部门名称
     *
     * @param id 参数0
     * @return java.lang.String
     * @author Carlos
     * @date 2023/4/4 14:43
     */
    String getDepartmentName(String id);

    /**
     * 获取某个层级的部门
     *
     * @param level 参数0
     * @return java.util.List<com.yunjin.org.pojo.dto.DepartmentDTO>
     * @author Carlos
     * @date 2023/6/2 0:45
     */
    List<DepartmentDTO> listByLevel(int level);

    /**
     * @param
     * @return java.util.List<com.yunjin.org.pojo.dto.DepartmentDTO>
     * @description: 获取当前用户下级网格以及微网格
     * @author: gule
     * @date: 2023-08-09 10:17
     */
    List<DepartmentDTO> getCurrentGridByDeptCode(Set<String> deptCodes);

    Set<String> getCurrentAndAllSubDepartmentId(String deptCode);

    /**
     * 获取缓存部门
     *
     * @param parentId 父级id
     * @return java.util.List<com.yunjin.org.dto.user.DepartmentDTO>
     * @author Carlos
     * @date 2023/6/2 0:45
     */
    List<DepartmentDTO> getCacheByParentId(Serializable parentId);

    /**
     * 获取子级部门总数
     *
     * @param parentId 父id
     * @return java.lang.String
     * @author Carlos
     * @date 2023/6/2 0:45
     */
    int getSubCountParentId(Serializable parentId);

    Set<DepartmentDTO> getAllSubDeptById(String departmentId);

    Set<DepartmentDTO> getAllSubDeptByIds(Set<String> departmentIds);

    List<DepartmentDTO> getAllSubDeptByDeptCode(String deptCode);

    DepartmentDTO getDepartmentByRegionCode(String regionCode);

    /**
     * 获取所有部门
     *
     * @return java.util.List<com.yunjin.org.dto.user.DepartmentDTO>
     * @author Carlos
     * @date 2022/12/22 17:11
     */
    List<DepartmentDTO> listAllByName(String name);

    /**
     * 根据机构类型获取部门（模糊匹配，左like）
     *
     * @param deptTypeListStr 机构类型查询串
     * @return List<DepartmentDTO>
     */
    List<DepartmentDTO> getSubDepartmentByTypeLike(String deptTypeListStr);

    /**
     * 获取多个层级的部门
     *
     * @param level
     * @return
     */
    List<DepartmentDTO> listByLevels(List<Integer> level);


    String add(DepartmentDTO dto);

    /**
     * 递归获取所有父级部门code
     * @param deptCode
     * @return
     */
    List<String> getAllParentDeptCodeByRecursive(String deptCode);

    /**
     * 根据区域编码获取部门
     * @param regionCode
     * @return
     */
    List<DepartmentDTO> listDepartmentByRegionCode(String regionCode);

    /**
     * 获取当前部门的所有子部门
     * @param deptCode
     * @return
     */
    List<DepartmentDTO> getAllSubDepartment(String deptCode);

    /**
     * 获取同层级部门
     * @param parentId 父级id
     * @param regionCode 区域编码
     * @param level 层级
     * @return
     */
    List<DepartmentDTO> getDepartmentsByParentId(String parentId,String regionCode,Integer level);

    /**
     * 初始化缓存hash
     */
    void initCacheHash();

    /**
     * 初始化缓存set，某部门所有子部门
     */
    void initCacheChildrenSet();

    /**
     * 初始化缓存list，某部门所有父部门
     */
    void initCacheParentList();


    /**
     * 根据父部门ID、区域编码和层级获取部门及其所有子部门的树形结构
     *
     * @param parentId 父部门ID
     * @param regionCode 区域编码
     * @param level 部门层级
     * @return 部门树形结构列表
     */
    List<DepartmentDTO> getDepartmentsWithChildrenByParentId(String parentId, String regionCode, Integer level);

    /**
     * 获取当前传入部门，获取所有父部门id，并按照A、B、C有序返回
     *
     * @param deptCode 部门code
     * @return list
     */
    List<String> getAllParentDepartmentIds(String deptCode);
    /**
     * 批量获取当前部门和上级部门
     *
     * @param deptCodes 部门编码
     * @return 部门树形结构列表
     */
    Map<String, List<DepartmentDTO>> getParentMapByCodes(Set<String> deptCodes, Integer limit);

    /**
     * 根据顶级父部门code获取该部门及所有子部门的层级编码
     * @param deptCode
     * @return
     */
    Set<String> listLevelCodeByTopParentDeptCode(String deptCode);

    /**
     * 获取当前传入部门，获取所有父部门id，并按照A、B、C有序返回
     *
     * @param deptId 部门id
     * @return list
     */
    List<String> getAllParentDepartmentIdsById(String deptId);


    /**
     * 根据部门id,获取当前部门及下面所有用户信息或者其他组织没有部门的用户，并剔除已在当前部门的用户
     *
     * @param id
     * @return
     */
    List<UserDepartmentDTO> getCurrentAndChildrenDepartmentUserIds(String id);
}
