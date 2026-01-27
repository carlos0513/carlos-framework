package com.carlos.datasource.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.carlos.core.param.ParamPage;
import com.carlos.datasource.pagination.PageInfo;

/**
 * <p>
 * 基于Mybatis-Plus的 公共Service接口
 * </p>
 *
 * @author yunjin
 * @date 2020/4/11 22:59
 */
public interface BaseService<T> extends IService<T> {


    /**
     * 获取查询构造器
     *
     * @return com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<T>
     * @author yunjin
     * @date 2021/12/16 11:56
     */
    default LambdaQueryWrapper<T> queryWrapper() {
        return Wrappers.lambdaQuery(getEntityClass());
    }

    /**
     * 获取更新构造器
     *
     * @return com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<T>
     * @author yunjin
     * @date 2021/12/16 11:56
     */
    default LambdaUpdateWrapper<T> updateWrapper() {
        return Wrappers.lambdaUpdate(getEntityClass());
    }

    /**
     * 分页信息获取
     *
     * @param param 分页参数
     * @return com.carlos.mybatis.pagination.PageInfo<T>
     * @author yunjin
     * @date 2021/12/20 12:21
     */
    default PageInfo<T> pageInfo(ParamPage param) {
        return new PageInfo<>(param);
    }
}
