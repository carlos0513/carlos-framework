package com.yunjin.mongodb.base;

import cn.hutool.core.lang.func.Func1;
import com.yunjin.core.pagination.PageConvert;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MongoDB 公共Service接口
 *
 * @author Carlos
 * @date 2021/12/22 18:58
 */
public interface BaseService<T, ID> {

    /**
     * 获取对应 entity 的 MongoRepository
     *
     * @return MongoRepository
     */
    MongoRepository<T, ID> getBaseRepository();

    /**
     * 获取 entity 的 class
     *
     * @return {@link Class<T>}
     */
    Class<T> getEntityClass();

    /**
     * 根据分页参数获取分页对象
     *
     * @param param 分页参数
     * @return org.springframework.data.domain.Pageable
     * @author Carlos
     * @date 2021/12/17 17:16
     */
    Pageable pageable(ParamPage param);

    /**
     * mongodb分页对象转换
     *
     * @param page    原分页信息
     * @param convert 转换器
     * @return com.soundtao.core.pagination.Paging<V>
     * @author Carlos
     * @date 2021/12/17 17:03
     */
    <V> Paging<V> pageConvert(Page<T> page, PageConvert<V, T> convert);

    /**
     * 获取字段名
     *
     * @param lambda 属性lambda
     * @return java.lang.String 数据库字段名
     * @author Carlos
     * @date 2021/12/23 11:05
     */
    String field(Func1<T, ?> lambda);
}
