package ${project.groupId}.apiimpl;


import lombok.RequiredArgsConstructor;
import ${project.groupId}.api.Api${table.classPrefix};
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * <p>
 * ${table.comment} api接口
 * </p>
 *
 * @author  ${project.author}
 * @date    ${.now}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/${table.apiPath}")
@Tag(name = "${table.comment}Feign接口")
public class Api${table.classPrefix}Impl implements Api${table.classPrefix}{


}
