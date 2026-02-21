package com.yunjin.org.convert;

import com.yunjin.org.pojo.param.LabelTypeCreateParam;
import com.yunjin.org.pojo.param.LabelTypeUpdateParam;
import com.yunjin.org.pojo.entity.LabelType;
import com.yunjin.org.pojo.vo.LabelTypeVO;
import com.yunjin.org.pojo.dto.LabelTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * 标签分类 转换器
 * </p>
 *
 * @author yunjin
 * @date 2024-3-22 15:07:09
 */
@Mapper(uses = {CommonConvert.class})
public interface LabelTypeConvert {

    LabelTypeConvert INSTANCE = Mappers.getMapper(LabelTypeConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-3-22 15:07:09
     */
    LabelTypeDTO toDTO(LabelTypeCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-3-22 15:07:09
     */
    LabelTypeDTO toDTO(LabelTypeUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-3-22 15:07:09
     */
    List<LabelTypeDTO> toDTO(List<LabelType> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-3-22 15:07:09
     */
    LabelTypeDTO toDTO(LabelType entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2024-3-22 15:07:09
     */
    LabelType toDO(LabelTypeDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-3-22 15:07:09
     */
    LabelTypeVO toVO(LabelTypeDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-3-22 15:07:09
     */
    List<LabelTypeVO> toVO(List<LabelType> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-3-22 15:07:09
     */
    LabelTypeVO toVO(LabelType entity);
}
