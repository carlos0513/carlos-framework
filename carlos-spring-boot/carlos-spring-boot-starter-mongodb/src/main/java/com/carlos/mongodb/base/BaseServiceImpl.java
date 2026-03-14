package com.carlos.mongodb.base;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.core.pagination.PageConvert;
import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamPage;
import com.carlos.core.util.PropertyNamer;
import com.carlos.mongodb.MetaObject;
import com.carlos.mongodb.MetaObjectHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公共 Service 实现类
 * 提供 MongoDB 的基础 CRUD 操作和字段自动填充功能
 *
 * @author Carlos
 * @date 2021/12/22 19:09
 */
@SuppressWarnings("unchecked")
public abstract class BaseServiceImpl<M extends MongoRepository<T, ID>, T, ID extends Serializable>
    implements BaseService<T, ID> {

    /**
     * 实体类型（Service 对应的实体类）
     */
    private Class<T> entityClass;

    @Autowired
    protected M baseRepository;

    @Autowired(required = false)
    protected MetaObjectHandler metaObjectHandler;

    @Autowired(required = false)
    protected MongoTemplate mongoTemplate;

    {
        final Class<?> clazz = this.getClass();
        final Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            final Type[] p = ((ParameterizedType) type).getActualTypeArguments();
            // 注意：这里取第1个，因为第0个是 M（Repository）
            if (p.length > 1) {
                this.entityClass = (Class<T>) p[1];
            }
        }
    }

    @Override
    public M getBaseRepository() {
        return this.baseRepository;
    }

    @Override
    public Class<T> getEntityClass() {
        return this.entityClass;
    }

    @Override
    public Pageable pageable(final ParamPage param) {
        return PageRequest.of(param.getCurrent() - 1, param.getSize());
    }

    @Override
    public <V> Paging<V> pageConvert(final Page<T> page, final PageConvert<V, T> convert) {
        final Paging<V> pageInfo = new Paging<>();
        final Pageable pageable = page.getPageable();
        pageInfo.setTotal((int) page.getTotalElements());
        pageInfo.setRecords(convert.convert(page.getContent()));
        pageInfo.setCurrent(pageable.getPageNumber() + 1); // MongoDB 分页从 0 开始，这里+1
        pageInfo.setSize(page.getSize());
        pageInfo.setPages(page.getTotalPages());
        return pageInfo;
    }

    /**
     * 字段映射缓存
     */
    private final Map<Func1<T, ?>, String> COLUMN_CACHE = new ConcurrentHashMap<>();

    @Override
    public String field(final Func1<T, ?> lambda) {
        String columnName = this.COLUMN_CACHE.get(lambda);
        if (StrUtil.isNotEmpty(columnName)) {
            return columnName;
        }
        final SerializedLambda serializedLambda = LambdaUtil.resolve(lambda);
        final String methodName = serializedLambda.getImplMethodName();
        // 从 lambda 信息取出 method、field、class 等
        final String property = PropertyNamer.methodToProperty(methodName);
        final Field field;

        final String className = serializedLambda.getImplClass().replace("/", ".");
        try {
            final Class<T> clazz = ClassUtil.loadClass(className, false);
            field = clazz.getDeclaredField(property);
        } catch (final NoSuchFieldException e) {
            throw new RuntimeException("Field not found: " + property, e);
        }

        // 从 field 取出字段名，优先使用 @Field 注解的值
        final org.springframework.data.mongodb.core.mapping.Field fieldAnnotation =
            field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class);
        if (fieldAnnotation != null && fieldAnnotation.value().length() > 0) {
            columnName = fieldAnnotation.value();
        } else {
            columnName = property;
        }
        this.COLUMN_CACHE.put(lambda, columnName);
        return columnName;
    }

    @Override
    public T save(T entity) {
        // 执行插入字段自动填充
        if (metaObjectHandler != null) {
            MetaObject metaObject = new MetaObject(entity);
            metaObjectHandler.insertFill(metaObject);
        }
        return this.baseRepository.save(entity);
    }

    /**
     * 更新实体（带字段自动填充）
     *
     * @param entity 实体对象
     * @return 更新后的实体
     */
    public T update(T entity) {
        // 执行更新字段自动填充
        if (metaObjectHandler != null) {
            MetaObject metaObject = new MetaObject(entity);
            metaObjectHandler.updateFill(metaObject);
        }
        return this.baseRepository.save(entity);
    }

    /**
     * 批量保存（带字段自动填充）
     *
     * @param entityList 实体列表
     * @return 保存后的实体列表
     */
    @Override
    public java.util.List<T> saveBatch(java.util.List<T> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return entityList;
        }
        // 对每个实体执行字段填充
        if (metaObjectHandler != null) {
            for (T entity : entityList) {
                MetaObject metaObject = new MetaObject(entity);
                metaObjectHandler.insertFill(metaObject);
            }
        }
        return this.baseRepository.saveAll(entityList);
    }

    /**
     * 获取 MongoTemplate（用于复杂查询）
     *
     * @return MongoTemplate
     */
    protected MongoTemplate getMongoTemplate() {
        return this.mongoTemplate;
    }

    /**
     * 获取元对象处理器
     *
     * @return MetaObjectHandler
     */
    protected MetaObjectHandler getMetaObjectHandler() {
        return this.metaObjectHandler;
    }

    /**
     * 创建元对象
     *
     * @param entity 实体对象
     * @return MetaObject
     */
    protected MetaObject createMetaObject(Object entity) {
        return new MetaObject(entity);
    }

    /**
     * 处理实体字段填充（插入）
     *
     * @param entity 实体对象
     */
    protected void handleInsertFill(T entity) {
        if (metaObjectHandler != null) {
            MetaObject metaObject = new MetaObject(entity);
            metaObjectHandler.insertFill(metaObject);
        }
    }

    /**
     * 处理实体字段填充（更新）
     *
     * @param entity 实体对象
     */
    protected void handleUpdateFill(T entity) {
        if (metaObjectHandler != null) {
            MetaObject metaObject = new MetaObject(entity);
            metaObjectHandler.updateFill(metaObject);
        }
    }
}
