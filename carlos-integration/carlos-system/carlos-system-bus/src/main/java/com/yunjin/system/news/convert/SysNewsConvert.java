package com.carlos.system.news.convert;

import com.carlos.system.news.pojo.dto.SysNewsDTO;
import com.carlos.system.news.pojo.entity.SysNews;
import com.carlos.system.news.pojo.param.SysNewsCreateParam;
import com.carlos.system.news.pojo.param.SysNewsUpdateParam;
import com.carlos.system.news.pojo.vo.SysNewsVO;
import com.carlos.system.pojo.ao.SysNewsAO;
import com.carlos.system.pojo.ao.SysNewsDetailAO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 系统-通知公告 转换器
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
@Mapper(uses = {CommonConvert.class})
public interface SysNewsConvert {

    SysNewsConvert INSTANCE = Mappers.getMapper(SysNewsConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    @Mapping(source = "images", target = "image", qualifiedByName = "objectListToString")
    SysNewsDTO toDTO(SysNewsCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    @Mapping(source = "images", target = "image", qualifiedByName = "objectListToString")
    SysNewsDTO toDTO(SysNewsUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    List<SysNewsDTO> toDTO(List<SysNews> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    SysNewsDTO toDTO(SysNews entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    SysNews toDO(SysNewsDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    @Mapping(target = "images", source = "image", qualifiedByName = "stringToObjectList")
    SysNewsVO toVO(SysNewsDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2022-11-14 23:48:53
     */
    List<SysNewsVO> toVO(List<SysNews> dos);


    List<SysNewsAO> toAOs(List<SysNewsDTO> news);

    SysNewsDetailAO toAO(SysNewsVO vo);

    SysNewsDTO toDTO(SysNewsAO ao);
}
