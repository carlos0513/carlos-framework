package com.carlos.msg.base.convert;

import com.carlos.msg.base.pojo.dto.MsgMessageSendRecordDTO;
import com.carlos.msg.base.pojo.entity.MsgMessageSendRecord;
import com.carlos.msg.base.pojo.param.MsgMessageSendRecordCreateParam;
import com.carlos.msg.base.pojo.param.MsgMessageSendRecordUpdateParam;
import com.carlos.msg.base.pojo.vo.MsgMessageSendRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 消息发送记录 转换器
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Mapper(uses = {CommonConvert.class})
public interface MsgMessageSendRecordConvert {

    MsgMessageSendRecordConvert INSTANCE = Mappers.getMapper(MsgMessageSendRecordConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageSendRecordDTO toDTO(MsgMessageSendRecordCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageSendRecordDTO toDTO(MsgMessageSendRecordUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    List<MsgMessageSendRecordDTO> toDTO(List<MsgMessageSendRecord> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageSendRecordDTO toDTO(MsgMessageSendRecord entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageSendRecord toDO(MsgMessageSendRecordDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageSendRecordVO toVO(MsgMessageSendRecordDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    List<MsgMessageSendRecordVO> toVO(List<MsgMessageSendRecord> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-3-10 10:53:03
     */
    MsgMessageSendRecordVO toVO(MsgMessageSendRecord entity);
}
