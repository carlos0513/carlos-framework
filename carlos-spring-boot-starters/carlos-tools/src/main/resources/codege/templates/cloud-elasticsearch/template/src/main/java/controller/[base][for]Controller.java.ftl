package ${project.groupId}.controller;


import com.carlos.core.param.ParamIdSet;

import ${project.groupId}.convert.${table.classPrefix}Convert;
import ${project.groupId}.pojo.param.${table.classPrefix}CreateParam;
import ${project.groupId}.pojo.param.${table.classPrefix}UpdateParam;
import ${project.groupId}.pojo.param.${table.classPrefix}PageParam;
import ${project.groupId}.pojo.dto.${table.classPrefix}DTO;
import ${project.groupId}.pojo.vo.${table.classPrefix}VO;
import ${project.groupId}.service.${table.classPrefix}Service;
import ${project.groupId}.manager.${table.classPrefix}Manager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * ${table.comment} rest服务接口
 * </p>
 *
 * @author  ${project.author}
 * @date    ${.now}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${table.apiPath}")
@Tag(name = "${table.comment}相关API")
public class ${table.classPrefix}Controller {

    public static final String BASE_NAME = "${table.comment}";

    private final ${table.classPrefix}Service ${table.classMainPrefix}Service;

    private final ${table.classPrefix}Manager ${table.classMainPrefix}Manager;


    @PostMapping
@Operation(summary = "新增" + BASE_NAME)
    public boolean add(@RequestBody @Validated ${table.classPrefix}CreateParam param) {
        ${table.classPrefix}DTO dto = ${table.classPrefix}Convert.INSTANCE.toDTO(param);
        return ${table.classMainPrefix}Service.add${table.classPrefix}(dto);
    }

    @PostMapping("delete")
@Operation(summary = "删除" + BASE_NAME)
    public boolean delete(@RequestBody ParamIdSet<String> param) {
        return ${table.classMainPrefix}Service.delete${table.classPrefix}(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public boolean update(@RequestBody @Validated ${table.classPrefix}UpdateParam param) {
        ${table.classPrefix}DTO dto = ${table.classPrefix}Convert.INSTANCE.toDTO(param);
        return ${table.classMainPrefix}Service.update${table.classPrefix}(dto);
    }

    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    public ${table.classPrefix}VO detail(@PathVariable String id) {
        return ${table.classPrefix}Convert.INSTANCE.toVO(${table.classMainPrefix}Manager.getDtoById(id));
    }

}
