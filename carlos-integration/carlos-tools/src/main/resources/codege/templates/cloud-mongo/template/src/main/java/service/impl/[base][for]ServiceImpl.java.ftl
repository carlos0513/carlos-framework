package ${project.packageName}.service.impl;

import ${project.packageName}.manager.${table.classPrefix}Manager;
import ${project.packageName}.pojo.dto.${table.classPrefix}DTO;
import ${project.packageName}.service.${table.classPrefix}Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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

    private final ${table.classPrefix}Manager ${table.classMainPrefix}Manager;

    @Override
    public void add${table.classPrefix}(${table.classPrefix}DTO dto){
        boolean success = ${table.classMainPrefix}Manager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    @Override
    public void delete${table.classPrefix}(Set<Serializable> ids){
        for (Serializable id : ids) {
            boolean success = ${table.classMainPrefix}Manager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    @Override
    public void update${table.classPrefix}(${table.classPrefix}DTO dto){
        boolean success = ${table.classMainPrefix}Manager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }


}
