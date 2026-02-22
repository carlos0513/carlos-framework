package com.carlos.org.login.convert;

import com.carlos.org.login.pojo.LoginSuccessVO;
import com.carlos.org.pojo.ao.LoginSuccessAO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * 系统角色 转换器
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Mapper
public interface LoginConvert {

    LoginConvert INSTANCE = Mappers.getMapper(LoginConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param vo 参数
     * @return 数据传输对象
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    LoginSuccessAO voToAO(LoginSuccessVO vo);
}
