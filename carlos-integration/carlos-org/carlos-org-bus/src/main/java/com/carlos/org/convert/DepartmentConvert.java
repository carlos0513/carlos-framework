package com.carlos.org.convert;

import com.carlos.core.pagination.Paging;
import com.carlos.org.param.DepartmentCreateOrUpdateParam;
import com.carlos.org.pojo.ao.DepartmentAO;
import com.carlos.org.pojo.ao.DepartmentBaseAO;
import com.carlos.org.pojo.ao.DepartmentUserAO;
import com.carlos.org.pojo.ao.UserDepartmentVO;
import com.carlos.org.pojo.dto.DepartmentDTO;
import com.carlos.org.pojo.dto.UserDTO;
import com.carlos.org.pojo.dto.UserDepartmentDTO;
import com.carlos.org.pojo.entity.Department;
import com.carlos.org.pojo.excel.DepartmentExcel;
import com.carlos.org.pojo.param.DepartmentCreateParam;
import com.carlos.org.pojo.param.DepartmentUpdateParam;
import com.carlos.org.pojo.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 部门 转换器
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Mapper(uses = {CommonConvert.class})
public interface DepartmentConvert {

    DepartmentConvert INSTANCE = Mappers.getMapper(DepartmentConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    DepartmentDTO toDTO(DepartmentCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    DepartmentDTO toDTO(DepartmentUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    List<DepartmentDTO> toDTO(List<Department> dos);

    List<DepartmentStepTreeVO.DeptInfo> toStepTreeVO (List<DepartmentDTO> dtos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    DepartmentDTO toDTO(Department entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    Department toDO(DepartmentDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    DepartmentVO toVO(DepartmentDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    List<DepartmentVO> toVO(List<Department> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    List<ThirdDepartmentVO> toThirdDeptVO(List<Department> dos);

    DepartmentVO toVO(Department dos);

    List<DepartmentVO> dto2vo(List<DepartmentDTO> dtos);

    List<DepartmentTreeVO> toTreeVO(List<DepartmentDTO> departments);

    /**
     * @Title: excelToDTOS
     * @Description: 导入数据对象转数据传输对象
     * @Date: 2023/2/21 15:46
     * @Parameters: [cachedDataList]
     * @Return java.util.List<com.carlos.org.dto.user.DepartmentDTO>
     */
    List<DepartmentDTO> excelToDTOS(List<DepartmentExcel> cachedDataList);

    /**
     * @Title: toDOS
     * @Description: 数据传输对象转数据持久对象
     * @Date: 2023/2/21 16:45
     * @Parameters: [departments]
     * @Return java.util.List<com.carlos.org.dto.user.DepartmentDTO>
     */
    List<Department> toDOS(List<DepartmentDTO> departments);

    List<Department> toDOS(Set<DepartmentDTO> departments);

    /**
     * @Title: dtoToexcel
     * @Description: 数据传输对象转数据导入导出对象
     * @Date: 2023/2/22 10:36
     * @Parameters: [list]
     * @Return java.util.List<com.carlos.org.pojo.excel.UserExcel>
     */
    List<DepartmentExcel> dtoToexcel(List<DepartmentDTO> list);

    /**
     * 部门基本信息转换
     *
     * @param dto 参数0
     * @return com.carlos.org.pojo.vo.DepartmentBaseVO
     * @author Carlos
     * @date 2023/4/2 14:35
     */
    DepartmentBaseVO toBaseVO(DepartmentDTO dto);

    @Mapping(source = "userPages", target = "userPages", qualifiedByName = "toUserVO")
    DepartmentDetailVO toDetailVO(DepartmentDTO departmentDetail);

    List<UserDepartmentVO> toDetailVO(List<UserDepartmentDTO> departmentDetail);

    List<DepartmentUserVO> toUserVO(List<UserDepartmentDTO> users);

    Paging<DepartmentUserNotInVO> toDepartmentUserVO(Paging<UserDTO> users);

    DepartmentBaseInfoVO toBaseInfoVO(DepartmentDTO detail);

    List<DepartmentAO> toAOS(List<DepartmentDTO> dtos);

    DepartmentAO toAO(DepartmentDTO department);

    @Mapping(target = "admin", source = "isAdmin")
    List<DepartmentUserAO> toUserAO(List<UserDepartmentDTO> users);


    DepartmentTreeVO toTreeVO(DepartmentDTO dto);

    @Mappings({
            @Mapping(source = "deptId", target = "thirdDeptId"),
            @Mapping(source = "name", target = "deptName"),
            @Mapping(source = "order", target = "sort"),
            @Mapping(source = "parentId", target = "parentId"),
            @Mapping(source = "areaValue", target = "regionCode"),
    })
    DepartmentDTO paramToDTO(DepartmentCreateOrUpdateParam param);

    List<DepartmentBaseAO> toBaseAOS(List<DepartmentDTO> dtos);

    Map<String, List<DepartmentAO>> toAOS(Map<String, List<DepartmentDTO>> superDepartmentName);
}
