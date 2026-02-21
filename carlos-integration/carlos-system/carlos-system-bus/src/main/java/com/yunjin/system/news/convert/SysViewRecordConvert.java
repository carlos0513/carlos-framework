package com.carlos.system.news.convert;

import com.carlos.system.news.pojo.dto.SysViewRecordDTO;
import com.carlos.system.news.pojo.entity.SysViewRecord;
import com.carlos.system.news.pojo.param.SysViewRecordCreateParam;
import com.carlos.system.news.pojo.param.SysViewRecordUpdateParam;
import com.carlos.system.news.pojo.vo.SysViewRecordVO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * 浏览记录 转换器
 * </p>
 *
 * @author yunjin
 * @date 2023-1-13 16:31:50
 */
@Mapper(uses = {CommonConvert.class})
public interface SysViewRecordConvert {

    SysViewRecordConvert INSTANCE = Mappers.getMapper(SysViewRecordConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    SysViewRecordDTO toDTO(SysViewRecordCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    SysViewRecordDTO toDTO(SysViewRecordUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    List<SysViewRecordDTO> toDTO(List<SysViewRecord> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    SysViewRecordDTO toDTO(SysViewRecord entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    SysViewRecord toDO(SysViewRecordDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    SysViewRecordVO toVO(SysViewRecordDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    List<SysViewRecordVO> toVO(List<SysViewRecord> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    SysViewRecordVO toVO(SysViewRecord entity);
}
