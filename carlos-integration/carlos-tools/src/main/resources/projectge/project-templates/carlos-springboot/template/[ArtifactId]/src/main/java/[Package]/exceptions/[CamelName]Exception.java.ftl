package ${project.packageName}.exceptions;

import com.carlos.core.exception.ComponentException;
import com.carlos.core.response.StatusCode;

/**
* <p>
    * 服务主异常
    * </p>
*
* @author  ${project.author}
* @date    ${.now}
*/
public class ${project.camelName}Exception extends ComponentException {

public ${project.camelName}Exception() {
super("${project.camelName}  exception!");
}

public ${project.camelName}Exception(String message) {
super(message);
}

public ${project.camelName}Exception(Integer errorCode, String message) {
super(errorCode, message);
}

public ${project.camelName}Exception(StatusCode statusCode) {
super(statusCode);
}

public ${project.camelName}Exception(Throwable cause) {
super(cause);
}

public ${project.camelName}Exception(String message, Throwable cause) {
super(message, cause);
}

}