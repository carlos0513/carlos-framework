package com.carlos.msg.base.convert;

import com.carlos.msg.base.pojo.dto.MsgMessageReceiverDTO;
import com.carlos.msg.base.pojo.entity.MsgMessageReceiver;
import com.carlos.msg.base.pojo.param.MsgMessageReceiverCreateParam;
import com.carlos.msg.base.pojo.param.MsgMessageReceiverUpdateParam;
import com.carlos.msg.base.pojo.vo.MsgMessageReceiverVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 消息接受者 转换器
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Mapper(uses = {CommonConvert.class})
public interface MsgMessageReceiverConvert {

    MsgMessageReceiverConvert INSTANCE = Mappers.getMapper(MsgMessageReceiverConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageReceiverDTO toDTO(MsgMessageReceiverCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageReceiverDTO toDTO(MsgMessageReceiverUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    List<MsgMessageReceiverDTO> toDTO(List<MsgMessageReceiver> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageReceiverDTO toDTO(MsgMessageReceiver entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageReceiver toDO(MsgMessageReceiverDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageReceiverVO toVO(MsgMessageReceiverDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    List<MsgMessageReceiverVO> toVO(List<MsgMessageReceiver> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageReceiverVO toVO(MsgMessageReceiver entity);
}
