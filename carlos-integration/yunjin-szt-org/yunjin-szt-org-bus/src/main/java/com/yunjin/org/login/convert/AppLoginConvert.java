package com.yunjin.org.login.convert;


import com.yunjin.org.login.pojo.LoginSuccessVO;
import com.yunjin.org.pojo.ao.LoginSuccessAO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * 移动端登录信息 转换器
 * </p>
 *
 * @author Carlos
 * @date 2023-4-12 1:38:49
 */
@Mapper
public interface AppLoginConvert {

    AppLoginConvert INSTANCE = Mappers.getMapper(AppLoginConvert.class);


    LoginSuccessVO aoToVo(LoginSuccessAO ao);
}
