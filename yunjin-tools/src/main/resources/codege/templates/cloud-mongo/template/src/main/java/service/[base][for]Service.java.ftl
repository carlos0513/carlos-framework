package ${project.groupId}.service;

import ${project.groupId}.pojo.dto.${table.classPrefix}DTO;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * ${table.comment} 业务接口
 * </p>
 *
 * @author  ${project.author}
 * @date    ${.now}
 */
public interface ${table.classPrefix}Service {

    /**
     * 新增${table.comment}
     *
     * @param dto ${table.comment}数据
     * @author  ${project.author}
     * @date    ${.now}
     */
    void add${table.classPrefix}(${table.classPrefix}DTO dto);

    /**
     * 删除${table.comment}
     *
     * @param ids ${table.comment}id
     * @author  ${project.author}
     * @date    ${.now}
     */
    void delete${table.classPrefix}(Set<Serializable> ids);

    /**
     * 修改${table.comment}信息
     *
     * @param dto 对象信息
     * @author  ${project.author}
     * @date    ${.now}
     */
    void update${table.classPrefix}(${table.classPrefix}DTO dto);

}
