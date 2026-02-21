package com.carlos.msg.base.convert;

import com.carlos.msg.base.pojo.dto.MsgMessageTypeDTO;
import com.carlos.msg.base.pojo.entity.MsgMessageType;
import com.carlos.msg.base.pojo.param.MsgMessageTypeCreateParam;
import com.carlos.msg.base.pojo.param.MsgMessageTypeUpdateParam;
import com.carlos.msg.base.pojo.param.MsgMessageTypeUpdateStateParam;
import com.carlos.msg.base.pojo.vo.MsgMessageTypeBaseVO;
import com.carlos.msg.base.pojo.vo.MsgMessageTypeVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 消息类型 转换器
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Mapper(uses = {CommonConvert.class})
public interface MsgMessageTypeConvert {

    MsgMessageTypeConvert INSTANCE = Mappers.getMapper(MsgMessageTypeConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageTypeDTO toDTO(MsgMessageTypeCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageTypeDTO toDTO(MsgMessageTypeUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    List<MsgMessageTypeDTO> toDTO(List<MsgMessageType> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageTypeDTO toDTO(MsgMessageType entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageType toDO(MsgMessageTypeDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageTypeVO toVO(MsgMessageTypeDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    List<MsgMessageTypeVO> toVO(List<MsgMessageType> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageTypeVO toVO(MsgMessageType entity);

    /**
     * 状态修改参数转换
     *
     * @param param 参数0
     * @return com.carlos.msg.pojo.dto.MsgMessageTypeDTO
     * @author Carlos
     * @date 2025-03-10 17:17
     */
    MsgMessageTypeDTO toDTO(MsgMessageTypeUpdateStateParam param);

    /**
     * 持久化对象转消息类型基础对象
     *
     * @param vos
     * @return
     */
    List<MsgMessageTypeBaseVO> toBaseVO(List<MsgMessageType> vos);
}
