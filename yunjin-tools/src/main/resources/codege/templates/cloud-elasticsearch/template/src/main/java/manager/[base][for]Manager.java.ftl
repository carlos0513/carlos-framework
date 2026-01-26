package ${project.groupId}.manager;

import ${project.groupId}.pojo.vo.${table.classPrefix}VO;
import ${project.groupId}.pojo.entity.${table.classPrefix};
import ${project.groupId}.pojo.dto.${table.classPrefix}DTO;

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
     * @return ${project.groupId}.pojo.dto.${table.classPrefix}DTO
     * @author ${project.author}
     * @date   ${.now}
     */
    ${table.classPrefix}DTO getDtoById(String id);

}
