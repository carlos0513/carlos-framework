package ${project.packageName}.manager;

import ${project.packageName}.pojo.vo.${table.classPrefix}VO;
import ${project.packageName}.pojo.entity.${table.classPrefix};
import ${project.packageName}.pojo.dto.${table.classPrefix}DTO;

import java.util.Map;
/**
 * <p>
 * ${table.comment} 查询封装接口
 * </p>
 *
 * @author  ${project.author}
 * @date    ${.now}
 */
public interface ${table.classPrefix}Manager {


    /**
     * 获取数据详情
     *
     * @param id 主键id
* @return ${project.packageName}.pojo.dto.${table.classPrefix}DTO
     * @author ${project.author}
     * @date   ${.now}
     */
    ${table.classPrefix}DTO getDtoById(String id);

}
