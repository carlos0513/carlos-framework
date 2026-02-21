package com.carlos.msg.base.convert;

import com.carlos.msg.base.pojo.dto.MsgChannelConfigDTO;
import com.carlos.msg.base.pojo.entity.MsgChannelConfig;
import com.carlos.msg.base.pojo.param.MsgChannelConfigCreateParam;
import com.carlos.msg.base.pojo.param.MsgChannelConfigUpdateParam;
import com.carlos.msg.base.pojo.vo.MsgChannelConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 消息渠道配置 转换器
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Mapper(uses = {CommonConvert.class})
public interface MsgChannelConfigConvert {

    MsgChannelConfigConvert INSTANCE = Mappers.getMapper(MsgChannelConfigConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgChannelConfigDTO toDTO(MsgChannelConfigCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgChannelConfigDTO toDTO(MsgChannelConfigUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    List<MsgChannelConfigDTO> toDTO(List<MsgChannelConfig> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgChannelConfigDTO toDTO(MsgChannelConfig entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgChannelConfig toDO(MsgChannelConfigDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgChannelConfigVO toVO(MsgChannelConfigDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    List<MsgChannelConfigVO> toVO(List<MsgChannelConfig> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgChannelConfigVO toVO(MsgChannelConfig entity);
}
