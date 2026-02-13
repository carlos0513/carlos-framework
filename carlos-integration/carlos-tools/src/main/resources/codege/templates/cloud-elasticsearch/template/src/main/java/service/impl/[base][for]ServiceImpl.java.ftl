package ${project.packageName}.service.impl;

import ${project.packageName}.convert.${table.classPrefix}Convert;
import ${project.packageName}.repository.${table.classPrefix}Repository;
import ${project.packageName}.pojo.dto.${table.classPrefix}DTO;
import ${project.packageName}.pojo.entity.${table.classPrefix};
import ${project.packageName}.service.${table.classPrefix}Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;


/**
 * <p>
 * ${table.comment} 业务接口实现类
 * </p>
 *
 * @author  ${project.author}
 * @date    ${.now}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ${table.classPrefix}ServiceImpl implements ${table.classPrefix}Service {

    private final ${table.classPrefix}Repository ${table.classMainPrefix}Repository;

    @Override
    public boolean add${table.classPrefix}(${table.classPrefix}DTO dto){
        ${table.classPrefix} entity = ${table.classPrefix}Convert.INSTANCE.toDO(dto);
        entity = ${table.classMainPrefix}Repository.save(entity);
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Add ${table.classMainPrefix} data: id:{}", entity.getId());
        }
        return true;
    }

    @Override
    public boolean delete${table.classPrefix}(Set<String> ids){
        for (String id : ids) {
            ${table.classMainPrefix}Repository.deleteById(id);
            if (log.isDebugEnabled()) {
                log.debug("Remove ${table.classMainPrefix} data by id:{}", id);
            }
        }
        return true;
    }

    @Override
    public boolean update${table.classPrefix}(${table.classPrefix}DTO dto){
        ${table.classPrefix} entity = ${table.classPrefix}Convert.INSTANCE.toDO(dto);
        ${table.classMainPrefix}Repository.save(entity);
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update ${table.classMainPrefix} data,  entity:{}", entity);
        }
        return true;
    }


}
