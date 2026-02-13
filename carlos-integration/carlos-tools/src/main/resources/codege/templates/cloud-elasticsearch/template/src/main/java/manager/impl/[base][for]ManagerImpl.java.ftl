package ${project.packageName}.manager.impl;


import ${project.packageName}.convert.${table.classPrefix}Convert;
import ${project.packageName}.pojo.vo.${table.classPrefix}VO;
import ${project.packageName}.pojo.dto.${table.classPrefix}DTO;
import ${project.packageName}.pojo.entity.${table.classPrefix};
import ${project.packageName}.repository.${table.classPrefix}Repository;
import ${project.packageName}.manager.${table.classPrefix}Manager;
import ${project.packageName}.pojo.param.${table.classPrefix}PageParam;

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
