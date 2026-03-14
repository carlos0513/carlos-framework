package com.carlos.mongodb.base;

import cn.hutool.core.lang.func.Func1;
import com.carlos.core.pagination.PageConvert;
import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB 公共 Service 接口
 * 提供与 MyBatis-Plus 风格一致的 CRUD 操作
 *
 * @author Carlos
 * @date 2021/12/22 18:58
 */
public interface BaseService<T, ID extends Serializable> {

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
     */
    Pageable pageable(ParamPage param);

    /**
     * mongodb 分页对象转换
     *
     * @param page    原分页信息
     * @param convert 转换器
     * @return com.carlos.core.pagination.Paging<V>
     */
    <V> Paging<V> pageConvert(Page<T> page, PageConvert<V, T> convert);

    /**
     * 获取字段名（根据 lambda 表达式）
     *
     * @param lambda 属性 lambda
     * @return java.lang.String 数据库字段名
     */
    String field(Func1<T, ?> lambda);

    // ==================== 新增方法 ====================

    /**
     * 根据 ID 查询
     *
     * @param id 主键
     * @return 实体对象
     */
    default Optional<T> getById(ID id) {
        return getBaseRepository().findById(id);
    }

    /**
     * 根据 ID 查询（直接返回对象，不存在返回 null）
     *
     * @param id 主键
     * @return 实体对象
     */
    default T getByIdOrNull(ID id) {
        return getBaseRepository().findById(id).orElse(null);
    }

    /**
     * 查询所有数据
     *
     * @return 实体列表
     */
    default List<T> list() {
        return getBaseRepository().findAll();
    }

    /**
     * 根据 ID 列表查询
     *
     * @param ids ID 列表
     * @return 实体列表
     */
    default List<T> listByIds(List<ID> ids) {
        return getBaseRepository().findAllById(ids);
    }

    /**
     * 保存实体
     *
     * @param entity 实体对象
     * @return 保存后的实体
     */
    T save(T entity);

    /**
     * 批量保存
     *
     * @param entityList 实体列表
     * @return 保存后的实体列表
     */
    default List<T> saveBatch(List<T> entityList) {
        return getBaseRepository().saveAll(entityList);
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键
     */
    default void removeById(ID id) {
        getBaseRepository().deleteById(id);
    }

    /**
     * 根据实体删除
     *
     * @param entity 实体对象
     */
    default void remove(T entity) {
        getBaseRepository().delete(entity);
    }

    /**
     * 批量删除
     *
     * @param entityList 实体列表
     */
    default void removeBatch(List<T> entityList) {
        getBaseRepository().deleteAll(entityList);
    }

    /**
     * 删除所有数据
     */
    default void removeAll() {
        getBaseRepository().deleteAll();
    }

    /**
     * 根据 ID 判断是否存在
     *
     * @param id 主键
     * @return true 如果存在
     */
    default boolean existsById(ID id) {
        return getBaseRepository().existsById(id);
    }

    /**
     * 统计所有数据数量
     *
     * @return 数据数量
     */
    default long count() {
        return getBaseRepository().count();
    }
}
