package ${project.packageName}.service;

import ${project.packageName}.pojo.entity.${table.classPrefix};
import ${project.packageName}.pojo.dto.${table.classPrefix}DTO;
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
     * @return boolean
     * @author  ${project.author}
     * @date    ${.now}
     */
    boolean add${table.classPrefix}(${table.classPrefix}DTO dto);

    /**
     * 删除${table.comment}
     *
     * @param ids ${table.comment}id
     * @return boolean
     * @author  ${project.author}
     * @date    ${.now}
     */
    boolean delete${table.classPrefix}(Set<String> ids);

    /**
     * 修改${table.comment}信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author  ${project.author}
     * @date    ${.now}
     */
    boolean update${table.classPrefix}(${table.classPrefix}DTO dto);

}
