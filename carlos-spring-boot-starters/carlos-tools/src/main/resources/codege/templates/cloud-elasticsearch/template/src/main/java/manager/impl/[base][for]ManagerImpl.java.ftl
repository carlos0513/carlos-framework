package ${project.groupId}.manager.impl;


import ${project.groupId}.convert.${table.classPrefix}Convert;
import ${project.groupId}.pojo.vo.${table.classPrefix}VO;
import ${project.groupId}.pojo.dto.${table.classPrefix}DTO;
import ${project.groupId}.pojo.entity.${table.classPrefix};
import ${project.groupId}.repository.${table.classPrefix}Repository;
import ${project.groupId}.manager.${table.classPrefix}Manager;
import ${project.groupId}.pojo.param.${table.classPrefix}PageParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * ${table.comment} 查询封装实现类
 * </p>
 *
 * @author  ${project.author}
 * @date    ${.now}
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ${table.classPrefix}ManagerImpl implements ${table.classPrefix}Manager {

private final ${table.classPrefix}Repository ${table.classMainPrefix}Repository;

    @Override
    public ${table.classPrefix}DTO getDtoById(String id) {
        ${table.classPrefix} entity = ${table.classMainPrefix}Repository.findById(id).get();
        return ${table.classPrefix}Convert.INSTANCE.toDTO(entity);
    }

}
