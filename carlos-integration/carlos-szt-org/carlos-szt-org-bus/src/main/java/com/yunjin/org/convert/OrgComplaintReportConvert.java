package com.yunjin.org.convert;


import com.yunjin.org.pojo.dto.OrgComplaintReportDTO;
import com.yunjin.org.pojo.entity.OrgComplaintReport;
import com.yunjin.org.pojo.param.OrgComplaintReportCreateParam;
import com.yunjin.org.pojo.vo.OrgComplaintReportVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
@Mapper(uses = {CommonConvert.class})
public interface OrgComplaintReportConvert {

    OrgComplaintReportConvert INSTANCE = Mappers.getMapper(OrgComplaintReportConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    OrgComplaintReportDTO toDTO(OrgComplaintReportCreateParam param);


    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    List<OrgComplaintReportDTO> toDTO(List<OrgComplaintReport> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    OrgComplaintReportDTO toDTO(OrgComplaintReport entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    OrgComplaintReport toDO(OrgComplaintReportDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    OrgComplaintReportVO toVO(OrgComplaintReportDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    List<OrgComplaintReportVO> toVO(List<OrgComplaintReport> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    OrgComplaintReportVO toVO(OrgComplaintReport entity);
}
