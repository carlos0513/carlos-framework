package com.yunjin.mongodb.base;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.yunjin.core.pagination.PageConvert;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamPage;
import com.yunjin.core.util.PropertyNamer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公共Service实现类
 *
 * @author Carlos
 * @date 2021/12/22 19:09
 */
@SuppressWarnings("unchecked")
public class BaseServiceImpl<M extends MongoRepository<T, ID>, T, ID>
        implements BaseService<T, ID> {

    /**
     * 实体类型(Service对应的实体类)
     */
    private Class<T> entityClass;

    @Autowired
    protected M baseRepository;

    {
        final Class<T> clazz = (Class<T>) this.getClass();
        final Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            final Type[] p = ((ParameterizedType) type).getActualTypeArguments();
            this.entityClass = (Class<T>) p[1];
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
        return PageRequest.of((int) param.getCurrent() - 1, (int) param.getSize());
    }

    @Override
    public <V> Paging<V> pageConvert(final Page<T> page, final PageConvert<V, T> convert) {
        final Paging<V> pageInfo = new Paging<>();
        final Pageable pageable = page.getPageable();
        pageInfo.setTotal((int) page.getTotalElements());
        pageInfo.setRecords(convert.convert(page.getContent()));
        pageInfo.setCurrent(pageable.getPageNumber());
        pageInfo.setSize(page.getSize());
        pageInfo.setPages(page.getTotalPages());
        return pageInfo;
    }

    /**
     * 字段映射
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
        // 从lambda信息取出method、field、class等
        final String property = PropertyNamer.methodToProperty(methodName);
        final Field field;

        final String className = serializedLambda.getImplClass().replace("/", ".");
        try {
            final Class<T> clazz = ClassUtil.loadClass(className, false);
            field = clazz.getDeclaredField(property);
        } catch (final NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        // 从field取出字段名，可以根据实际情况调整
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

    public T save(final T entity) {
        // TODO: Carlos 2021/12/24 在此处对对象进行默认赋值
        // Field[] fields = ReflectUtil.getFields(entityClass);
        // Arrays.stream(fields).filter(field -> {
        //     FieldFill annotation = field.getAnnotation(FieldFill.class);
        //     if (annotation ==null) {
        //         return false;
        //     }
        //     if (annotation.strategy() == FieldFillStrategy.INSERT || annotation.strategy() ==
        // FieldFillStrategy.INSERT_UPDATE) {
        //         return true;
        //     }
        //     return false;
        // }).forEach(field -> {
        //     ReflectUtil.setFieldValue(entity, field, null);
        // });
        return this.baseRepository.save(entity);
    }
}
