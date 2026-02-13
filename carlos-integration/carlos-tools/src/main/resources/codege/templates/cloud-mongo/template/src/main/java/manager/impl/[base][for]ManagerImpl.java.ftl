package ${project.packageName}.manager.impl;

import com.carlos.core.pagination.Paging;
import com.carlos.mongodb.page.MongoPage;
import ${project.packageName}.convert.${table.classPrefix}Convert;
import ${project.packageName}.manager.${table.classPrefix}Manager;
import ${project.packageName}.pojo.dto.${table.classPrefix}DTO;
import ${project.packageName}.pojo.entity.${table.classPrefix};
import ${project.packageName}.pojo.param.${table.classPrefix}PageParam;
import ${project.packageName}.pojo.vo.${table.classPrefix}VO;
import ${project.packageName}.repository.${table.classPrefix}Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.Serializable;

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
    public boolean add(${table.classPrefix}DTO dto) {
        ${table.classPrefix} entity = ${table.classPrefix}Convert.INSTANCE.toDO(dto);
        entity = ${table.classMainPrefix}Repository.save(entity);
        dto.setId(entity.getId());
        if (log.isDebugEnabled()) {
            log.debug("Insert '${table.classPrefix}' data: id:{}", entity.getId());
        }
        return true;
    }

    @Override
    public boolean delete(Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
       ${table.classMainPrefix}Repository.deleteById(String.valueOf(id));
        if (log.isDebugEnabled()) {
            log.debug("Remove '${table.classPrefix}' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(${table.classPrefix}DTO dto) {
        ${table.classPrefix} entity = ${table.classPrefix}Convert.INSTANCE.toDO(dto);
        ${table.classMainPrefix}Repository.save(entity);
        if (log.isDebugEnabled()) {
            log.debug("Update '${table.classPrefix}' data, {}", entity);
        }
        return true;
    }

    @Override
    public ${table.classPrefix}DTO getDtoById(String id) {
        ${table.classPrefix} entity = ${table.classMainPrefix}Repository.findById(id).get();
        return ${table.classPrefix}Convert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<${table.classPrefix}VO> getPage(${table.classPrefix}PageParam param){
        Pageable pageable = MongoPage.page(param);
        Example<${table.classPrefix}> example = null;
        Page<${table.classPrefix}> page =${table.classMainPrefix}Repository.findAll(example, pageable);
        return MongoPage.convert(page, ${table.classPrefix}Convert.INSTANCE::toVO);
    }

}
