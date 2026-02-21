package com.carlos.msg.base.convert;

import com.carlos.msg.api.pojo.param.ApiMsgMessageParam;
import com.carlos.msg.base.pojo.dto.MsgMessageDTO;
import com.carlos.msg.base.pojo.entity.MsgMessage;
import com.carlos.msg.base.pojo.param.MsgMessageCreateParam;
import com.carlos.msg.base.pojo.param.MsgMessageUpdateParam;
import com.carlos.msg.base.pojo.vo.MsgMessageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 消息 转换器
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Mapper(uses = {CommonConvert.class})
public interface MsgMessageConvert {

    MsgMessageConvert INSTANCE = Mappers.getMapper(MsgMessageConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageDTO toDTO(MsgMessageCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageDTO toDTO(MsgMessageUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    List<MsgMessageDTO> toDTO(List<MsgMessage> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageDTO toDTO(MsgMessage entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessage toDO(MsgMessageDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageVO toVO(MsgMessageDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    List<MsgMessageVO> toVO(List<MsgMessage> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageVO toVO(MsgMessage entity);

    /***
     * api消息传递参数转数据持久化对象
     *
     * @param param 参数0
     * @return com.carlos.msg.pojo.dto.MsgMessageDTO
     * @throws
     * @author chentao
     * @date 2025/4/3 15:56
     */
    MsgMessageDTO toDTO(ApiMsgMessageParam param);


}
