package com.carlos.audit.convert;

import com.carlos.audit.pojo.dto.AuditLogAttachmentsDTO;
import com.carlos.audit.pojo.entity.AuditLogAttachments;
import com.carlos.audit.pojo.param.AuditLogAttachmentsCreateParam;
import com.carlos.audit.pojo.param.AuditLogAttachmentsUpdateParam;
import com.carlos.audit.pojo.vo.AuditLogAttachmentsVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 审计日志-附件引用 转换器
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Mapper(uses = {CommonConvert.class})
public interface AuditLogAttachmentsConvert {

    AuditLogAttachmentsConvert INSTANCE = Mappers.getMapper(AuditLogAttachmentsConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogAttachmentsDTO toDTO(AuditLogAttachmentsCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogAttachmentsDTO toDTO(AuditLogAttachmentsUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    List<AuditLogAttachmentsDTO> toDTO(List<AuditLogAttachments> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogAttachmentsDTO toDTO(AuditLogAttachments entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogAttachments toDO(AuditLogAttachmentsDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogAttachmentsVO toVO(AuditLogAttachmentsDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    List<AuditLogAttachmentsVO> toVO(List<AuditLogAttachments> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogAttachmentsVO toVO(AuditLogAttachments entity);
}
