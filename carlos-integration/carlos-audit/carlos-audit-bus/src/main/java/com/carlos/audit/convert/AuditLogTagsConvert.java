package com.carlos.audit.convert;

import com.carlos.audit.pojo.dto.AuditLogTagsDTO;
import com.carlos.audit.pojo.entity.AuditLogTags;
import com.carlos.audit.pojo.param.AuditLogTagsCreateParam;
import com.carlos.audit.pojo.param.AuditLogTagsUpdateParam;
import com.carlos.audit.pojo.vo.AuditLogTagsVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 审计日志-动态标签 转换器
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Mapper(uses = {CommonConvert.class})
public interface AuditLogTagsConvert {

    AuditLogTagsConvert INSTANCE = Mappers.getMapper(AuditLogTagsConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogTagsDTO toDTO(AuditLogTagsCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogTagsDTO toDTO(AuditLogTagsUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    List<AuditLogTagsDTO> toDTO(List<AuditLogTags> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogTagsDTO toDTO(AuditLogTags entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogTags toDO(AuditLogTagsDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogTagsVO toVO(AuditLogTagsDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    List<AuditLogTagsVO> toVO(List<AuditLogTags> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    AuditLogTagsVO toVO(AuditLogTags entity);
}
