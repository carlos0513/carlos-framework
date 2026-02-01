package ${project.groupId}.config;

import com.carlos.boot.request.RequestUtil;
import com.carlos.core.auth.UserContext;
import com.carlos.core.interfaces.ApplicationExtend;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
* <p>
    * 自定义系统扩展实现
    * </p>
 *
 * @author  ${project.author}
 * @date    ${.now}
*/
@Slf4j
@Component
@AllArgsConstructor
public class ApplicationExtendImpl implements ApplicationExtend {


@Override
public Serializable getUserId() {
UserContext userContext = RequestUtil.getRequestInfo().getUserContext();
return userContext.getUserId();
}

}
