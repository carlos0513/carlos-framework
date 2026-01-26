package ${project.groupId}.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.datasource.pagination.MybatisPage;
import com.yunjin.datasource.pagination.PageInfo;
import ${project.groupId}.convert.${table.classPrefix}Convert;
import ${project.groupId}.pojo.vo.${table.classPrefix}VO;
import ${project.groupId}.pojo.dto.${table.classPrefix}DTO;
import ${project.groupId}.pojo.entity.${table.classPrefix};
import ${project.groupId}.manager.${table.classPrefix}Manager;
import ${project.groupId}.mapper.${table.classPrefix}Mapper;
import ${project.groupId}.pojo.param.${table.classPrefix}PageParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class ${table.classPrefix}ManagerImpl  extends BaseServiceImpl<${table.classPrefix}Mapper, ${table.classPrefix}> implements ${table.classPrefix}Manager {

    @Override
    public boolean add(${table.classPrefix}DTO dto) {
        ${table.classPrefix} entity = ${table.classPrefix}Convert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert '${table.classPrefix}' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
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
        boolean success = removeById(id);
        if (!success) {
            log.warn("Remove '${table.classPrefix}' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove '${table.classPrefix}' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(${table.classPrefix}DTO dto) {
        ${table.classPrefix} entity = ${table.classPrefix}Convert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update '${table.classPrefix}' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update '${table.classPrefix}' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public ${table.classPrefix}DTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        ${table.classPrefix} entity = getBaseMapper().selectById(id);
        return ${table.classPrefix}Convert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<${table.classPrefix}VO> getPage(${table.classPrefix}PageParam param){
        LambdaQueryWrapper<${table.classPrefix}> wrapper = queryWrapper();
        wrapper.select(
        <#list table.columns as column>
            <#if !column.logicField && !column.versionField>
                ${table.classPrefix}::get${column.propertyNameUp}<#if column_has_next>,</#if>
            </#if>
        </#list>
                );
        PageInfo<${table.classPrefix}> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, ${table.classPrefix}Convert.INSTANCE::toVO);
    }

}
