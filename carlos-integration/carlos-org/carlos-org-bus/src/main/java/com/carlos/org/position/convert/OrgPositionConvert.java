package com.carlos.org.position.convert;

import com.carlos.org.position.pojo.dto.OrgPositionDTO;
import com.carlos.org.position.pojo.entity.OrgPosition;
import com.carlos.org.position.pojo.param.OrgPositionCreateParam;
import com.carlos.org.position.pojo.param.OrgPositionUpdateParam;
import com.carlos.org.position.pojo.vo.OrgPositionVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 岗位表 转换器
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@Mapper(uses = {CommonConvert.class})
public interface OrgPositionConvert {

    OrgPositionConvert INSTANCE = Mappers.getMapper(OrgPositionConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    OrgPositionDTO toDTO(OrgPositionCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    OrgPositionDTO toDTO(OrgPositionUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    List<OrgPositionDTO> toDTO(List<OrgPosition> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    OrgPositionDTO toDTO(OrgPosition entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    OrgPosition toDO(OrgPositionDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    OrgPositionVO toVO(OrgPositionDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    List<OrgPositionVO> toVO(List<OrgPosition> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    OrgPositionVO toVO(OrgPosition entity);
}
