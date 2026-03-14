package com.carlos.mongodb.tool;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamPage;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * MongoDB 工具服务类
 * 提供基于 MongoTemplate 的便捷操作方法
 * </p>
 *
 * @author carlos
 * @date 2024/01/15
 */
@RequiredArgsConstructor
public class MongoServiceTool {

    private final MongoTemplate mongoTemplate;

    /**
     * 保存文档
     *
     * @param entity 实体对象
     * @param <T>    实体类型
     * @return 保存后的实体
     */
    public <T> T save(T entity) {
        return mongoTemplate.save(entity);
    }

    /**
     * 批量保存
     *
     * @param entities 实体列表
     * @param <T>      实体类型
     * @return 保存后的实体列表
     */
    public <T> List<T> saveAll(List<T> entities) {
        return (List<T>) mongoTemplate.insertAll(entities);
    }

    /**
     * 根据 ID 查询
     *
     * @param id    主键
     * @param clazz 实体类
     * @param <T>   实体类型
     * @return Optional 包装的实体
     */
    public <T, ID> Optional<T> findById(ID id, Class<T> clazz) {
        return Optional.ofNullable(mongoTemplate.findById(id, clazz));
    }

    /**
     * 根据条件查询单条记录
     *
     * @param query 查询条件
     * @param clazz 实体类
     * @param <T>   实体类型
     * @return Optional 包装的实体
     */
    public <T> Optional<T> findOne(Query query, Class<T> clazz) {
        return Optional.ofNullable(mongoTemplate.findOne(query, clazz));
    }

    /**
     * 根据条件查询列表
     *
     * @param query 查询条件
     * @param clazz 实体类
     * @param <T>   实体类型
     * @return 实体列表
     */
    public <T> List<T> find(Query query, Class<T> clazz) {
        return mongoTemplate.find(query, clazz);
    }

    /**
     * 查询所有数据
     *
     * @param clazz 实体类
     * @param <T>   实体类型
     * @return 实体列表
     */
    public <T> List<T> findAll(Class<T> clazz) {
        return mongoTemplate.findAll(clazz);
    }

    /**
     * 查询所有数据（带排序）
     *
     * @param clazz 实体类
     * @param sort  排序规则
     * @param <T>   实体类型
     * @return 实体列表
     */
    public <T> List<T> findAll(Class<T> clazz, Sort sort) {
        Query query = new Query().with(sort);
        return mongoTemplate.find(query, clazz);
    }

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @param param 分页参数
     * @param clazz 实体类
     * @param <T>   实体类型
     * @return 分页结果
     */
    public <T> Paging<T> findPage(Query query, ParamPage param, Class<T> clazz) {
        // 计算总数
        long total = mongoTemplate.count(query, clazz);

        // 分页查询
        Pageable pageable = PageRequest.of(param.getCurrent() - 1, param.getSize());
        query.with(pageable);

        List<T> records = mongoTemplate.find(query, clazz);

        // 组装分页结果
        Paging<T> paging = new Paging<>();
        paging.setCurrent(param.getCurrent());
        paging.setSize(param.getSize());
        paging.setTotal((int) total);
        paging.setRecords(records);
        paging.setPages((int) Math.ceil((double) total / param.getSize()));

        return paging;
    }

    /**
     * 更新操作
     *
     * @param query  查询条件
     * @param update 更新内容
     * @param clazz  实体类
     * @return 更新结果
     */
    public UpdateResult update(Query query, Update update, Class<?> clazz) {
        return mongoTemplate.updateMulti(query, update, clazz);
    }

    /**
     * 更新第一条匹配的记录
     *
     * @param query  查询条件
     * @param update 更新内容
     * @param clazz  实体类
     * @return 更新结果
     */
    public UpdateResult updateFirst(Query query, Update update, Class<?> clazz) {
        return mongoTemplate.updateFirst(query, update, clazz);
    }

    /**
     * 根据 ID 更新
     *
     * @param id     主键
     * @param update 更新内容
     * @param clazz  实体类
     * @return 更新结果
     */
    public UpdateResult updateById(Object id, Update update, Class<?> clazz) {
        Query query = Query.query(Criteria.where("_id").is(id));
        return mongoTemplate.updateFirst(query, update, clazz);
    }

    /**
     * 删除操作
     *
     * @param query 查询条件
     * @param clazz 实体类
     * @return 删除结果
     */
    public DeleteResult remove(Query query, Class<?> clazz) {
        return mongoTemplate.remove(query, clazz);
    }

    /**
     * 根据 ID 删除
     *
     * @param id    主键
     * @param clazz 实体类
     * @return 删除结果
     */
    public DeleteResult removeById(Object id, Class<?> clazz) {
        Query query = Query.query(Criteria.where("_id").is(id));
        return mongoTemplate.remove(query, clazz);
    }

    /**
     * 删除实体
     *
     * @param entity 实体对象
     * @return 删除结果
     */
    public DeleteResult remove(Object entity) {
        return mongoTemplate.remove(entity);
    }

    /**
     * 统计数量
     *
     * @param query 查询条件
     * @param clazz 实体类
     * @return 数量
     */
    public long count(Query query, Class<?> clazz) {
        return mongoTemplate.count(query, clazz);
    }

    /**
     * 统计所有数量
     *
     * @param clazz 实体类
     * @return 数量
     */
    public long count(Class<?> clazz) {
        return mongoTemplate.count(new Query(), clazz);
    }

    /**
     * 判断是否存在
     *
     * @param query 查询条件
     * @param clazz 实体类
     * @return true 如果存在
     */
    public boolean exists(Query query, Class<?> clazz) {
        return mongoTemplate.exists(query, clazz);
    }

    /**
     * 获取 MongoTemplate
     *
     * @return MongoTemplate
     */
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }
}
