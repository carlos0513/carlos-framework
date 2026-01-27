package ${project.groupId}.convert;

import ${project.groupId}.pojo.param.${table.classPrefix}CreateParam;
import ${project.groupId}.pojo.param.${table.classPrefix}UpdateParam;
import ${project.groupId}.pojo.entity.${table.classPrefix};
import ${project.groupId}.pojo.vo.${table.classPrefix}VO;
import ${project.groupId}.pojo.dto.${table.classPrefix}DTO;
import ${project.groupId}.convert.CommonConvert;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * ${table.comment} 转换器
 * </p>
 *
 * @author ${project.author}
 * @date ${.now}
 */
@Mapper(uses = {CommonConvert.class})
public interface ${table.classPrefix}Convert {

    ${table.classPrefix}Convert INSTANCE = Mappers.getMapper(${table.classPrefix}Convert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author ${project.author}
     * @date ${.now}
     */
    ${table.classPrefix}DTO toDTO(${table.classPrefix}CreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author ${project.author}
     * @date ${.now}
     */
    ${table.classPrefix}DTO toDTO(${table.classPrefix}UpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author ${project.author}
     * @date ${.now}
     */
    List<${table.classPrefix}DTO> toDTO(List<${table.classPrefix}> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author ${project.author}
     * @date ${.now}
     */
    ${table.classPrefix}DTO toDTO(${table.classPrefix} entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author ${project.author}
     * @date ${.now}
     */
    ${table.classPrefix} toDO(${table.classPrefix}DTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author ${project.author}
     * @date ${.now}
     */
    ${table.classPrefix}VO toVO(${table.classPrefix}DTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author ${project.author}
     * @date ${.now}
     */
    List<${table.classPrefix}VO> toVO(List<${table.classPrefix}> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author ${project.author}
     * @date ${.now}
     */
    ${table.classPrefix}VO toVO(${table.classPrefix} entity);
}
