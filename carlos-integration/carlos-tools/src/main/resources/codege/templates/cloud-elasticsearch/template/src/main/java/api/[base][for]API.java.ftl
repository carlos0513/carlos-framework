package ${project.packageName}.api;

import com.carlos.core.annotation.ClientApi;
import lombok.RequiredArgsConstructor;
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
@ClientApi
@RestController
@RequiredArgsConstructor
@RequestMapping("api/${table.apiPath}")
@Tag(name = "${table.comment}Feign接口")
public class ${table.classPrefix}API {


}
