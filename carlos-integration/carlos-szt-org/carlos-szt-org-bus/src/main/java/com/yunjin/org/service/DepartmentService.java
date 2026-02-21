package com.yunjin.org.service;

import com.yunjin.core.base.DepartmentInfo;
import com.yunjin.core.pagination.Paging;
import com.yunjin.org.enums.DeptRelationEnum;
import com.yunjin.org.param.DepartmentCreateOrUpdateParam;
import com.yunjin.org.pojo.ao.CommonCustomAO;
import com.yunjin.org.pojo.dto.DepartmentDTO;
import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.pojo.dto.UserDepartmentDTO;
import com.yunjin.org.pojo.param.*;
import com.yunjin.org.pojo.vo.DepartmentBaseVO;
import com.yunjin.org.pojo.vo.DepartmentStepTreeVO;
import com.yunjin.org.pojo.vo.ThirdDepartmentVO;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 部门 业务接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
public interface DepartmentService {

    /**
     * 新增部门
     *
     * @param dto 部门数据
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    void saveOrUpdate(DepartmentDTO dto);

    /**
     * 删除部门
     *
     * @param ids 部门id
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    void deleteDepartment(Set<Serializable> ids);

    /**
     * 递归删除
     *
     * @param id 父级部门id
     * @author Carlos
     * @date 2023/1/17 13:42
     */
    void deleteRecursion(Serializable id);

    /**
     * 修改部门信息
     *
     * @param dto 对象信息
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    void updateDepartment(DepartmentDTO dto);

    /**
     * getDepartmentById
     *
     * @param id 参数0
     * @return com.yunjin.org.dto.user.DepartmentDTO
     * @author Carlos
     * @date 2022/11/14 21:50
     */
    DepartmentDTO getDepartmentById(String id);


    /**
     * 获取部门树形列表
     *
     * @return java.util.List<com.yunjin.org.pojo.vo.DepartmentVO>
     * @author Carlos
     * @date 2022/11/23 14:44
     */
    List<DepartmentDTO> departmentTree(Serializable departmentId, boolean userFlag);

    /**
     * 获取平级部门
     *
     * @param departmentId        部门id
     * @param dataStewardUserFlag
     * @return java.util.List<com.yunjin.org.dto.user.DepartmentDTO>
     * @author Carlos
     * @date 2022/12/20 9:10
     */
    List<DepartmentDTO> getSameLevelDepartment(Serializable departmentId, boolean userFlag, boolean dataStewardUserFlag);

    /**
     * 根据父级部门以及部门区域 获取同层级部门
     *
     * @param departmentId 部门id
     * @return java.util.List<com.yunjin.org.dto.user.DepartmentDTO>
     * @author Carlos
     * @date 2022/12/20 9:10
     */
    List<DepartmentDTO> getPeerLevelDepartment(Serializable departmentId);

    /**
     * 预览部门名称
     *
     * @param id    部门id
     * @param limit 显示级数
     * @return java.util.List<java.lang.String>
     * @author Carlos
     * @date 2022/12/20 13:48
     */
    List<String> previewDepartmentName(String id, Integer limit);

    /**
     * 批量获取当前部门和上级部门
     *
     * @param ids   部门ID列表
     * @param limit 限制层级
     * @return Map<部门ID, 上级部门列表>
     */
    Map<String, List<DepartmentDTO>> getParentDepartmentMap(Set<String> ids, Integer limit);

    /**
     * 获取用户部门
     *
     * @param userId 参数0
     * @return com.yunjin.org.dto.user.DepartmentDTO
     * @author Carlos
     * @date 2022/12/21 14:18
     */
    List<DepartmentDTO> getByUserId(String userId);

    /**
     * 获取部门信息
     *
     * @param departmentId 部门id
     * @param limit        参数1
     * @return com.yunjin.common.core.base.DepartmentInfo
     * @author Carlos
     * @date 2022/12/30 14:08
     */
    DepartmentInfo getDepartmentInfo(Serializable departmentId, Integer limit);

    /**
     * 获取部门信息
     *
     * @param deptCode 部门编码
     * @param limit    参数1
     * @return com.yunjin.common.core.base.DepartmentInfo
     * @author Carlos
     * @date 2022/12/30 14:08
     */
    DepartmentInfo getDepartmentInfo(String deptCode, Integer limit);

    /**
     * @Title: getDepartments
     * @Description: 获取所有部门
     * @Date: 2022/12/30 20:49
     * @Parameters: []
     * @Return java.util.List<com.yunjin.org.dto.user.DepartmentDTO>
     */
    List<DepartmentDTO> getDepartments();

    /**
     * 获取所有的子部门id
     *
     * @param departmentId 当前部门id
     * @return java.util.Set<java.io.Serializable>
     * @author Carlos
     * @date 2023/1/31 13:34
     */
    Set<Serializable> getAllSubDepartmentId(Serializable departmentId);


    /**
     * 获取所有子部门ids
     * @param ids
     * @return
     */
    Set<String> getAllSubDeptByIds (Set<String> ids);

    /**
     * 获取部门详情信息 包含基本信息和用户信息
     *
     * @param id    部门id
     * @param param
     * @return com.yunjin.org.pojo.dto.DepartmentDTO
     * @author Carlos
     * @date 2023/4/2 15:28
     */
    DepartmentDTO getDepartmentDetail(String id, UserPageParam param);

    /**
     * @Title: getUser
     * @Description: 根据部门id获取用户
     * @Date: 2023/4/7 17:53
     * @Parameters: [id]
     * @Return java.util.List<com.yunjin.org.pojo.dto.UserDepartmentDTO>
     */
    List<UserDepartmentDTO> getUser(String id);

    /**
     * @Title: listByIds
     * @Description: 根据ids获取列表
     * @Date: 2023/4/8 10:56
     * @Parameters: [departmentId]
     * @Return java.util.List<com.yunjin.org.pojo.dto.DepartmentDTO>
     */
    List<DepartmentDTO> listByIds(Set<String> departmentId);

    /**
     * 根据code获取部门信息
     *
     * @param parentCode 参数0
     * @return com.yunjin.org.pojo.dto.DepartmentDTO
     * @author Carlos
     * @date 2023/4/15 10:56
     */
    DepartmentDTO getDepartmentByCode(String parentCode);

    /**
     * @Title: getNotInDeptUser
     * @Description: 根据部门id获取不在部门下的用户
     * @Date: 2023/5/10 15:02
     * @Parameters: [id]
     * @Return java.util.List<com.yunjin.org.pojo.dto.UserDTO>
     */
    Paging<UserDTO> getNotInDeptUser(UserExcludeDeptPageParam param);

    /**
     * 为部门用户设置管理员权限
     */
    boolean setDeptAdmin(DepartmentAdminParam param);

    /**
     * 获取部门信息
     *
     * @param parentId 参数0
     * @param deptName 参数1
     * @return com.yunjin.org.pojo.dto.DepartmentDTO
     * @author Carlos
     * @date 2023/5/30 0:18
     */
    DepartmentDTO getDepartment(String parentId, String deptName);

    /**
     * 获取所有子部门
     *
     * @param parentId 参数0
     * @return java.util.List<com.yunjin.org.pojo.dto.DepartmentDTO>
     * @author Carlos
     * @date 2023/6/1 20:41
     */
    List<DepartmentDTO> getSubDepartment(String parentId);

    /**
     * 获取第n层级的部门
     *
     * @param level 参数0
     * @return java.util.List<com.yunjin.org.pojo.dto.DepartmentDTO>
     * @author Carlos
     * @date 2023/6/2 0:44
     */
    List<DepartmentDTO> getDepartmentByLevel(int level);

    /**
     * 获取当前登录用户下级部门列表
     *
     * @return
     */
    List<DepartmentBaseVO> getCurrDepartments();

    /**
     * 递归获取部门id及其下级id列表
     *
     * @param root
     * @return
     */
    Set<String> getDepartmentIdsRecurById(String root);

    /**
     * @param
     * @return java.util.List<com.yunjin.org.pojo.dto.DepartmentDTO>
     * @description: 当前用户下级网格以及微网格
     * @author: gule
     * @date: 2023-08-09 10:15
     */
    List<DepartmentDTO> currentGrid();

    /**
     * @Title: subTree
     * @Description: 获取下级部门树
     * @Date: 2023/11/27 16:01
     * @Parameters: [userFlag]
     * @Return java.util.List<com.yunjin.org.pojo.dto.DepartmentDTO>
     */
    List<DepartmentDTO> subTree(Boolean userFlag);

    /***
     * @desc: 用户当前部门及下级部门
     * @author: GaoQiao
     * @date: 2023/12/6 9:53
     * @param: [userId]
     * @return: java.util.List<com.yunjin.org.pojo.dto.DepartmentDTO>
     **/
    DepartmentDTO getCurrentAndSubLeveDept();

    /***
     * @desc: 获取同级部门及下级部门
     * @author: GaoQiao
     * @date: 2023/12/13 10:15
     * @return: java.util.List<com.yunjin.org.pojo.dto.DepartmentDTO>
     **/
    List<DepartmentDTO> getSameAndSubLeveDept();

    /***
     * @desc: 获取上级部门
     * @author: GaoQiao
     * @date: 2023/12/13 15:34
     * @param: [deptCode]
     * @return: com.yunjin.org.pojo.dto.DepartmentDTO
     **/
    DepartmentDTO parentDepartment(String deptCode);

    /**
     * 通过部门code获取本级及下级部门id
     *
     * @param deptCode
     * @return
     */
    Set<String> getCurrentAndAllSubDepartmentId(String deptCode);

    /**
     * 修改部门区域信息
     *
     * @param regionName 参数0
     * @throws
     * @author Carlos
     * @date 2024/4/8 15:26
     */
    void changeRegion(String regionName);

    /**
     * 缓存初始化
     *
     * @author Carlos
     * @date 2024/4/10 17:16
     */
    void initCache();

    /**
     * 获取一段时间内部门人员活动率排名列表
     *
     * @param startTime
     * @param endTime
     * @param deptIds
     * @return
     */
    List<CommonCustomAO> getAactivityRatio(String startTime, String endTime, List<String> deptIds);

    Set<String> allSubDepartmentCode(String id, boolean addSelf);

    DepartmentDTO getDepartmentByRegionCode(String regionCode);

    /**
     * @Title: allDepartmentByName
     * @Description: 名称模糊查询获取所有部门
     * @Date: 2022/12/30 20:49
     * @Parameters: []
     * @Return java.util.List<com.yunjin.org.dto.user.DepartmentDTO>
     */
    List<DepartmentDTO> allDepartmentByName(String name);

    List<DepartmentDTO> getDepartmentByCodes(List<String> codes);

    /**
     * @Title: treeAndUser
     * @Description: 获取所有部门树及对应数据专员
     * @Date: 2024/9/26 17:17
     * @Parameters: []
     * @Return java.util.List<com.yunjin.org.pojo.dto.DepartmentDTO>
     */
    List<DepartmentDTO> treeAndUser();

    List<DepartmentDTO> getMeToFourthLevelDept();

    /**
     * 获取多个层级的部门
     *
     * @param level
     * @return
     */
    List<DepartmentDTO> getDepartmentByLevels(List<Integer> level);

    Paging<UserDepartmentDTO> getCurSubUser(CurSubExecutorPageParam param);

    void saveOrUpdateForThird(DepartmentDTO dto);

    void deleteForThird(String deptId);

    void batchSaveOrUpdateForThird(Set<DepartmentCreateOrUpdateParam> param);

    /**
     * 绵阳定制需求 优化部门组织树查询速度
     *
     * @param departmentId
     * @param userFlag
     * @return
     */
    List<DepartmentDTO> departmentTreeMianYang(Serializable departmentId, boolean userFlag);


    /**
     * 部门组织树导出
     * @param departmentId 部门id
     * @param userFlag 是否导出用户
     * @param response 响应
     */
    void treeExport(String departmentId, Boolean userFlag, HttpServletResponse response);


    /**
     * @Title: listByThirdIds
     * @Description: 根据三方ids获取
     * @Date: 2025/6/23 14:19
     * @Parameters: [thirdIds]
     * @Return java.util.List<com.yunjin.org.pojo.dto.DepartmentDTO>
     */
    List<DepartmentDTO> listByThirdIds(Set<String> thirdIds);

    /**
     * 三方部门组织树
     * @return list
     */
    List<DepartmentDTO> thirdDeptTree();

    /**
     * 本系统部门组织树
     * @return list
     */
    List<DepartmentDTO> systemDeptTree();

    /**
     * 组织机构
     *
     * @param
     * @return
     */
    Paging<ThirdDepartmentVO> thirdPage(DepartmentPageParam param);

    /**
     * 组织机构
     *
     * @param
     * @return
     */
    Paging<ThirdDepartmentVO> deptInfoPage(DepartmentPageParam param);

    Paging<UserDepartmentDTO> getUserPageByDeptId(TaskExecutorPageMianYangParam param);

    List<DepartmentDTO> subscribingDepartment();

    List<String> previewDepartmentNameByCode(String code, Integer limit);

    List<DepartmentDTO> listThirdInfoByIds(Set<String> ids);

    /**
     * @Title: sameAndSuperiorDept
     * @Description: 当前层级及同层级上级部门树
     * @Date: 2025/7/3 16:43
     * @Parameters: []
     * @Return java.util.List<com.yunjin.org.pojo.dto.DepartmentDTO>
     */
    List<DepartmentDTO> sameAndSuperiorDept();

    Paging<UserDepartmentDTO> getCurDeptUser(CurDeptExecutorPageParam param);

    List<DepartmentDTO> getFullDepartment(String departmentId);

    /**
     * 部门组织树
     * @param deptRelationEnum 部门关系枚举
     * @param level 要获取的上级级别（1表示直接上级，2表示上级的上级，以此类推）
     * @return list
     */
    List<DepartmentDTO> deptTree(DeptRelationEnum deptRelationEnum, Integer level);

    /**
     * 一键下派部门
     * @return list
     */
    List<DepartmentDTO> autoDispatchDept();

    /**
     * 查询部门树-部门逐级加载
     * @param deptRelationEnum 部门关系
     * @return list
     */
    List<DepartmentDTO> deptTreeLoad(DeptRelationEnum deptRelationEnum, Integer level);

    /**
     * 逐级加载部门
     *
     * @param departmentId 部门id
     * @return java.util.List<com.yunjin.org.pojo.dto.DepartmentDTO>
     * @author Carlos
     * @date 2025-09-23 16:57
     */
    List<DepartmentDTO> getByParentId(String departmentId,Boolean thirdFlag);

    /**
     * 获取当前部门的所有子部门
     * @param deptCode
     * @return
     */
    List<DepartmentDTO> getAllSubDepartment(String deptCode);

    /**
     * 获取当前传入部门，获取所有父部门id，并按照A、B、C有序返回
     *
     * @param deptCode code
     * @return list
     */
    List<String> getAllParentDepartmentIds(String deptCode);

    /**
     * 获取当前传入部门id，获取所有父部门id，并按照A、B、C有序返回
     *
     * @param deptId id
     * @return list
     */
    List<String> getAllParentDepartmentIdsById(String deptId);

    /**
     * 获取部门上级部门
     * @param deptCodes
     * @param limit
     * @return
     */
    Map<String, List<DepartmentDTO>> getParentMapByCodes(Set<String> deptCodes, Integer limit);

    /**
     * 根据code，获取上级部门,传入C,并按照A、B、C有序返回
     *
     * @param deptCode code
     * @return list
     */
    List<DepartmentDTO> getAllParentDepartments(String deptCode, String deptId);

    /**
     * 根据当前传入部门的id，获取当前及下面所有子部门的deptcode
     *
     * @param parentId
     * @return
     */
    Set<String> getAllDepartCodes(String parentId);

    /**
     * 获取当前及所有子部门
     * @return
     */
    List<DepartmentDTO> getCurrentAndAllSubset();

    DepartmentStepTreeVO getTreeStep(String departmentId);
}
