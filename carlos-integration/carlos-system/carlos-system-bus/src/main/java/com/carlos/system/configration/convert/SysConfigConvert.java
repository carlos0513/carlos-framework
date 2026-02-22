package com.carlos.system.configration.convert;

import com.carlos.system.config.CommonConvert;
import com.carlos.system.configration.pojo.dto.SysConfigDTO;
import com.carlos.system.configration.pojo.entity.SysConfig;
import com.carlos.system.configration.pojo.param.SysConfigCreateParam;
import com.carlos.system.configration.pojo.param.SysConfigUpdateParam;
import com.carlos.system.configration.pojo.vo.SysConfigVO;
import com.carlos.system.pojo.ao.SysConfigAO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 系统配置 转换器
 * </p>
 *
 * @author yunjin
 * @date 2022-11-3 13:47:54
 */
@Mapper(uses = {CommonConvert.class})
public interface SysConfigConvert {

    SysConfigConvert INSTANCE = Mappers.getMapper(SysConfigConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2022-11-3 13:47:54
     */
    SysConfigDTO toDTO(SysConfigCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2022-11-3 13:47:54
     */
    SysConfigDTO toDTO(SysConfigUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-3 13:47:54
     */
    List<SysConfigDTO> toDTO(List<SysConfig> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-3 13:47:54
     */
    SysConfigDTO toDTO(SysConfig entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2022-11-3 13:47:54
     */
    SysConfig toDO(SysConfigDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-3 13:47:54
     */
    SysConfigVO toVO(SysConfigDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-3 13:47:54
     */
    List<SysConfigVO> toVO(List<SysConfig> dos);

    SysConfigAO toAO(SysConfigDTO config);
}
