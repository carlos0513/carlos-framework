package com.yunjin.resource.convert;

import com.yunjin.resource.pojo.dto.MetricsManagementDTO;
import com.yunjin.resource.pojo.entity.MetricsManagement;
import com.yunjin.resource.pojo.param.MetricsManagementCreateParam;
import com.yunjin.resource.pojo.param.MetricsManagementUpdateParam;
import com.yunjin.resource.pojo.vo.MetricsManagementVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 系统指标管理 转换器
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Mapper(uses = {CommonConvert.class})
public interface MetricsManagementConvert {

    MetricsManagementConvert INSTANCE = Mappers.getMapper(MetricsManagementConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    MetricsManagementDTO toDTO(MetricsManagementCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    MetricsManagementDTO toDTO(MetricsManagementUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    List<MetricsManagementDTO> toDTO(List<MetricsManagement> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    MetricsManagementDTO toDTO(MetricsManagement entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    MetricsManagement toDO(MetricsManagementDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    MetricsManagementVO toVO(MetricsManagementDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    List<MetricsManagementVO> toVO(List<MetricsManagement> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    MetricsManagementVO toVO(MetricsManagement entity);
}
