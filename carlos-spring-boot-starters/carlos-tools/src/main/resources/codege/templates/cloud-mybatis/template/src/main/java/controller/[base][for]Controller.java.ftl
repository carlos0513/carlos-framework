package ${project.groupId}.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.carlos.core.pagination.Paging;
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
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


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
@Tag(name = "${table.comment}")
public class ${table.classPrefix}Controller {

    public static final String BASE_NAME = "${table.comment}";

    private final ${table.classPrefix}Service ${table.classMainPrefix}Service;

    private final ${table.classPrefix}Manager ${table.classMainPrefix}Manager;


    @ApiOperationSupport(author = "${project.author}")
    @PostMapping("add")
@Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated ${table.classPrefix}CreateParam param) {
        ${table.classPrefix}DTO dto = ${table.classPrefix}Convert.INSTANCE.toDTO(param);
        ${table.classMainPrefix}Service.add${table.classPrefix}(dto);
    }

    @ApiOperationSupport(author = "${project.author}")
    @PostMapping("delete")
@Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        ${table.classMainPrefix}Service.delete${table.classPrefix}(param.getIds());
    }

    @ApiOperationSupport(author = "${project.author}")
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated ${table.classPrefix}UpdateParam param) {
        ${table.classPrefix}DTO dto = ${table.classPrefix}Convert.INSTANCE.toDTO(param);
        ${table.classMainPrefix}Service.update${table.classPrefix}(dto);
    }

    @ApiOperationSupport(author = "${project.author}")
    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public ${table.classPrefix}VO detail(String id) {
        return ${table.classPrefix}Convert.INSTANCE.toVO(${table.classMainPrefix}Manager.getDtoById(id));
    }

    @ApiOperationSupport(author = "${project.author}")
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<${table.classPrefix}VO> page(${table.classPrefix}PageParam param) {
        return ${table.classMainPrefix}Manager.getPage(param);
    }
}
