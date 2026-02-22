package com.carlos.system.fallback;

import com.carlos.core.response.Result;
import com.carlos.system.api.ApiMenuOperate;
import com.carlos.system.pojo.ao.MenuOperateAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;
import java.util.Set;


/**
 * <p>
 * 菜单操作 api 降级
 * </p>
 *
 * @author carlos
 * @date 2023-7-7 14:19:55
 */
@Slf4j
public class ApiMenuOperateFallbackFactory implements FallbackFactory
        <ApiMenuOperate> {

    @Override
    public ApiMenuOperate create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("菜单操作服务调用失败: message:{}", message);
        return new ApiMenuOperate() {

            @Override
            public Result<List<MenuOperateAO>> listByIds(Set<String> ids) {
                return Result.fail("菜单操作信息获取失败");
            }

            @Override
            public Result<List<MenuOperateAO>> listByMenuId(String id) {
                return Result.fail("菜单操作信息获取失败");
            }

            @Override
            public Result<List<MenuOperateAO>> listByMenuIds(Set<String> menuIds) {
                return Result.fail("菜单操作信息获取失败");
            }
        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiMenuOperateFallbackFactory menuOperateFallbackFactory() {
    //     return new ApiMenuOperateFallbackFactory();
    // }
}