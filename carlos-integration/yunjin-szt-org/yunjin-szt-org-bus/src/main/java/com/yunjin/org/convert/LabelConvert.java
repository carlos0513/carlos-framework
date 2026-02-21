package com.yunjin.org.convert;

import com.yunjin.org.pojo.ao.LabelAO;
import com.yunjin.org.pojo.param.LabelCreateParam;
import com.yunjin.org.pojo.param.LabelUpdateParam;
import com.yunjin.org.pojo.entity.Label;
import com.yunjin.org.pojo.vo.LabelVO;
import com.yunjin.org.pojo.dto.LabelDTO;
import com.yunjin.org.convert.CommonConvert;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * 标签 转换器
 * </p>
 *
 * @author yunjin
 * @date 2024-3-23 12:31:52
 */
@Mapper(uses = {CommonConvert.class})
public interface LabelConvert {

    LabelConvert INSTANCE = Mappers.getMapper(LabelConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-3-23 12:31:52
     */
    LabelDTO toDTO(LabelCreateParam param);

    LabelAO toAO(LabelDTO labelDTO);

    List<LabelAO> toAOS(List<LabelDTO> labelDTOs);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-3-23 12:31:52
     */
    LabelDTO toDTO(LabelUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-3-23 12:31:52
     */
    List<LabelDTO> toDTO(List<Label> dos);

    List<Label> toDO(List<LabelDTO> dtos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-3-23 12:31:52
     */
    LabelDTO toDTO(Label entity);

    List<LabelDTO> toDTOS(List<Label> entitys);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2024-3-23 12:31:52
     */
    Label toDO(LabelDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-3-23 12:31:52
     */
    LabelVO toVO(LabelDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-3-23 12:31:52
     */
    List<LabelVO> toVO(List<Label> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-3-23 12:31:52
     */
    LabelVO toVO(Label entity);
}
