package com.carlos.audit.convert;

import com.carlos.audit.pojo.dto.AuditLogFieldChangeDTO;
import com.carlos.audit.pojo.entity.AuditLogFieldChange;
import com.carlos.audit.pojo.param.AuditLogFieldChangeCreateParam;
import com.carlos.audit.pojo.param.AuditLogFieldChangeUpdateParam;
import com.carlos.audit.pojo.vo.AuditLogFieldChangeVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 审计日志-字段级变更明细 转换器
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Mapper(uses = {CommonConvert.class})
public interface AuditLogFieldChangeConvert {

    AuditLogFieldChangeConvert INSTANCE = Mappers.getMapper(AuditLogFieldChangeConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogFieldChangeDTO toDTO(AuditLogFieldChangeCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogFieldChangeDTO toDTO(AuditLogFieldChangeUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    List<AuditLogFieldChangeDTO> toDTO(List<AuditLogFieldChange> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogFieldChangeDTO toDTO(AuditLogFieldChange entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogFieldChange toDO(AuditLogFieldChangeDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogFieldChangeVO toVO(AuditLogFieldChangeDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    List<AuditLogFieldChangeVO> toVO(List<AuditLogFieldChange> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogFieldChangeVO toVO(AuditLogFieldChange entity);
}
