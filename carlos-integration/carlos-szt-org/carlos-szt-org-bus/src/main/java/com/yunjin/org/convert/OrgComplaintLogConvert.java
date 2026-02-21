package com.yunjin.org.convert;


import com.yunjin.org.pojo.dto.OrgComplaintLogDTO;
import com.yunjin.org.pojo.entity.OrgComplaintLog;
import com.yunjin.org.pojo.vo.OrgComplaintLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 投诉建议处理节点日志 转换器
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
@Mapper(uses = {CommonConvert.class})
public interface OrgComplaintLogConvert {

    OrgComplaintLogConvert INSTANCE = Mappers.getMapper(OrgComplaintLogConvert.class);


    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    List<OrgComplaintLogDTO> toDTO(List<OrgComplaintLog> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    OrgComplaintLogDTO toDTO(OrgComplaintLog entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    OrgComplaintLog toDO(OrgComplaintLogDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    OrgComplaintLogVO toVO(OrgComplaintLogDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    List<OrgComplaintLogVO> toVO(List<OrgComplaintLogDTO> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-9-23 16:01:35
     */
    OrgComplaintLogVO toVO(OrgComplaintLog entity);
}
