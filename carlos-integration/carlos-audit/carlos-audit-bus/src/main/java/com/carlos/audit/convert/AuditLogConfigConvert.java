package com.carlos.audit.convert;

import com.carlos.audit.pojo.dto.AuditLogConfigDTO;
import com.carlos.audit.pojo.entity.AuditLogConfig;
import com.carlos.audit.pojo.param.AuditLogConfigCreateParam;
import com.carlos.audit.pojo.param.AuditLogConfigUpdateParam;
import com.carlos.audit.pojo.vo.AuditLogConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 审计日志配置 转换器
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Mapper(uses = {CommonConvert.class})
public interface AuditLogConfigConvert {

    AuditLogConfigConvert INSTANCE = Mappers.getMapper(AuditLogConfigConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogConfigDTO toDTO(AuditLogConfigCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogConfigDTO toDTO(AuditLogConfigUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    List<AuditLogConfigDTO> toDTO(List<AuditLogConfig> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogConfigDTO toDTO(AuditLogConfig entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogConfig toDO(AuditLogConfigDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogConfigVO toVO(AuditLogConfigDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    List<AuditLogConfigVO> toVO(List<AuditLogConfig> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogConfigVO toVO(AuditLogConfig entity);
}
