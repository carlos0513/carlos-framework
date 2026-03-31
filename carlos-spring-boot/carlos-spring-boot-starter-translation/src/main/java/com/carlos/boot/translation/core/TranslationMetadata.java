package com.carlos.boot.translation.core;

import com.carlos.boot.translation.annotation.*;
import com.carlos.core.base.DepartmentInfo;
import com.carlos.core.base.Dict;
import com.carlos.core.base.RegionInfo;
import com.carlos.core.base.UserInfo;
import com.carlos.core.enums.BaseEnum;
import com.carlos.core.enums.Enum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 字段翻译元数据（缓存反射信息，提升性能）
 * </p>
 *
 * @author carlos
 * @date 2025/01/20
 */
@Slf4j
public class TranslationMetadata {

    /**
     * 类级别的元数据缓存
     */
    private static final ConcurrentHashMap<Class<?>, TranslationMetadata> CACHE =
        new ConcurrentHashMap<>();

    private final List<TranslatableField> fields;
    private final boolean hasNested;

    private TranslationMetadata(Class<?> clazz) {
        this.fields = parseFields(clazz);
        this.hasNested = fields.stream().anyMatch(f -> f instanceof NestedField);
    }

    /**
     * 获取类的翻译元数据
     *
     * @param clazz 类
     * @return 翻译元数据
     */
    public static TranslationMetadata of(Class<?> clazz) {
        return CACHE.computeIfAbsent(clazz, TranslationMetadata::new);
    }

    /**
     * 清除缓存
     */
    public static void clearCache() {
        CACHE.clear();
    }

    private List<TranslatableField> parseFields(Class<?> clazz) {
        List<TranslatableField> list = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            // 跳过静态和 final 字段
            if (Modifier.isStatic(field.getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true);

            // 解析各种注解
            TransUser userAnno = field.getAnnotation(TransUser.class);
            if (userAnno != null) {
                list.add(new UserField(field, userAnno));
                continue;
            }

            TransDict dictAnno = field.getAnnotation(TransDict.class);
            if (dictAnno != null) {
                list.add(new DictField(field, dictAnno));
                continue;
            }

            TransDept deptAnno = field.getAnnotation(TransDept.class);
            if (deptAnno != null) {
                list.add(new DeptField(field, deptAnno));
                continue;
            }

            TransRegion regionAnno = field.getAnnotation(TransRegion.class);
            if (regionAnno != null) {
                list.add(new RegionField(field, regionAnno));
                continue;
            }

            TransEnum enumAnno = field.getAnnotation(TransEnum.class);
            if (enumAnno != null) {
                list.add(new EnumField(field, enumAnno));
                continue;
            }

            TransNested nestedAnno = field.getAnnotation(TransNested.class);
            if (nestedAnno != null) {
                list.add(new NestedField(field, nestedAnno));
            }
        }

        return Collections.unmodifiableList(list);
    }

    /**
     * 收集需要翻译的ID
     *
     * @param obj   对象
     * @param batch 批次
     */
    public void collect(Object obj, TranslationBatch batch) {
        if (obj == null) {
            return;
        }

        for (TranslatableField field : fields) {
            try {
                field.collect(obj, batch);
            } catch (Exception e) {
                log.warn("Failed to collect translation id from field: {}", field.getName(), e);
            }
        }
    }

    /**
     * 填充翻译结果
     *
     * @param obj  对象
     * @param data 翻译数据
     */
    public void fill(Object obj, TranslationData data) {
        if (obj == null) {
            return;
        }

        for (TranslatableField field : fields) {
            try {
                field.fill(obj, data);
            } catch (Exception e) {
                log.warn("Failed to fill translation to field: {}", field.getName(), e);
            }
        }
    }

    /**
     * 是否包含嵌套字段
     *
     * @return true/false
     */
    public boolean hasNested() {
        return hasNested;
    }

    /**
     * 获取可翻译字段列表
     *
     * @return 字段列表
     */
    public List<TranslatableField> getFields() {
        return fields;
    }

    // ============ 字段定义抽象 ============

    /**
     * 可翻译字段抽象
     */
    @Getter
    @AllArgsConstructor
    public abstract static class TranslatableField {
        protected final Field field;
        protected final String name;

        /**
         * 收集ID
         *
         * @param obj   对象
         * @param batch 批次
         * @throws Exception 异常
         */
        public abstract void collect(Object obj, TranslationBatch batch) throws Exception;

        /**
         * 填充翻译结果
         *
         * @param obj  对象
         * @param data 数据
         * @throws Exception 异常
         */
        public abstract void fill(Object obj, TranslationData data) throws Exception;
    }

    /**
     * 用户字段
     */
    public static class UserField extends TranslatableField {
        private final TransUser annotation;
        private final Field sourceField;
        private final Field targetField;

        UserField(Field field, TransUser annotation) {
            super(field, field.getName());
            this.annotation = annotation;

            // 确定源字段和目标字段
            if (StringUtils.hasText(annotation.source())) {
                this.sourceField = ReflectionUtils.findField(
                    field.getDeclaringClass(), annotation.source());
                if (this.sourceField != null) {
                    this.sourceField.setAccessible(true);
                }
            } else {
                this.sourceField = field;
            }

            if (StringUtils.hasText(annotation.target())) {
                this.targetField = ReflectionUtils.findField(
                    field.getDeclaringClass(), annotation.target());
                if (this.targetField != null) {
                    this.targetField.setAccessible(true);
                }
            } else {
                this.targetField = field;
            }
        }

        @Override
        public void collect(Object obj, TranslationBatch batch) throws Exception {
            if (sourceField == null) {
                return;
            }
            Serializable id = (Serializable) sourceField.get(obj);
            batch.addUserId(id);
        }

        @Override
        public void fill(Object obj, TranslationData data) throws Exception {
            if (sourceField == null || targetField == null) {
                return;
            }
            Serializable id = (Serializable) sourceField.get(obj);
            if (id == null) {
                return;
            }

            UserInfo user = data.getUser(id);
            if (user == null) {
                return;
            }

            Object value = extractValue(user, annotation.type());
            targetField.set(obj, value);
        }

        private Object extractValue(UserInfo user, TransUser.Type type) {
            switch (type) {
                case FULL:
                    return user;
                case NAME:
                    return user.getName();
                case NICKNAME:
                    return user.getNickName();
                case REALNAME:
                    return user.getRealName();
                case PHONE:
                    return user.getPhone();
                case EMAIL:
                    return user.getEmail();
                default:
                    return user.getName();
            }
        }
    }

    /**
     * 字典字段
     */
    public static class DictField extends TranslatableField {
        private final TransDict annotation;
        private final Field targetField;

        DictField(Field field, TransDict annotation) {
            super(field, field.getName());
            this.annotation = annotation;

            if (StringUtils.hasText(annotation.target())) {
                this.targetField = ReflectionUtils.findField(
                    field.getDeclaringClass(), annotation.target());
                if (this.targetField != null) {
                    this.targetField.setAccessible(true);
                }
            } else {
                this.targetField = field;
            }
        }

        @Override
        public void collect(Object obj, TranslationBatch batch) throws Exception {
            String value = (String) field.get(obj);
            if (value != null) {
                batch.addDictCode(annotation.type(), value);
            }
        }

        @Override
        public void fill(Object obj, TranslationData data) throws Exception {
            if (targetField == null) {
                return;
            }
            String value = (String) field.get(obj);
            if (value == null) {
                return;
            }

            Dict dict = data.getDict(annotation.type(), value);
            if (dict == null) {
                return;
            }

            Object output = extractValue(dict, annotation.outputType());
            targetField.set(obj, output);
        }

        private Object extractValue(Dict dict, TransDict.OutputType type) {
            switch (type) {
                case FULL:
                    return dict;
                case NAME:
                    return dict.getName();
                case CODE:
                    return dict.getCode();
                default:
                    return dict.getName();
            }
        }
    }

    /**
     * 部门字段
     */
    public static class DeptField extends TranslatableField {
        private final TransDept annotation;
        private final Field sourceField;
        private final Field targetField;

        DeptField(Field field, TransDept annotation) {
            super(field, field.getName());
            this.annotation = annotation;

            if (StringUtils.hasText(annotation.source())) {
                this.sourceField = ReflectionUtils.findField(
                    field.getDeclaringClass(), annotation.source());
                if (this.sourceField != null) {
                    this.sourceField.setAccessible(true);
                }
            } else {
                this.sourceField = field;
            }

            if (StringUtils.hasText(annotation.target())) {
                this.targetField = ReflectionUtils.findField(
                    field.getDeclaringClass(), annotation.target());
                if (this.targetField != null) {
                    this.targetField.setAccessible(true);
                }
            } else {
                this.targetField = field;
            }
        }

        @Override
        public void collect(Object obj, TranslationBatch batch) throws Exception {
            if (sourceField == null) {
                return;
            }
            Serializable id = (Serializable) sourceField.get(obj);
            batch.addDeptId(id);
        }

        @Override
        public void fill(Object obj, TranslationData data) throws Exception {
            if (sourceField == null || targetField == null) {
                return;
            }
            Serializable id = (Serializable) sourceField.get(obj);
            if (id == null) {
                return;
            }

            DepartmentInfo dept = data.getDept(id);
            if (dept == null) {
                return;
            }

            Object value = extractValue(dept, annotation.type(),
                annotation.separator(), annotation.limit());
            targetField.set(obj, value);
        }

        private Object extractValue(DepartmentInfo dept, TransDept.OutputType type,
                                    String separator, int limit) {
            switch (type) {
                case FULL:
                    return dept;
                case NAME:
                    return dept.getName();
                case FULLNAME:
                    List<String> fullName = dept.getFullName();
                    if (fullName == null || fullName.isEmpty()) {
                        return dept.getName();
                    }
                    int end = Math.min(limit, fullName.size());
                    return String.join(separator, fullName.subList(0, end));
                default:
                    return dept.getName();
            }
        }
    }

    /**
     * 区域字段
     */
    public static class RegionField extends TranslatableField {
        private final TransRegion annotation;
        private final Field sourceField;
        private final Field targetField;

        RegionField(Field field, TransRegion annotation) {
            super(field, field.getName());
            this.annotation = annotation;

            if (StringUtils.hasText(annotation.source())) {
                this.sourceField = ReflectionUtils.findField(
                    field.getDeclaringClass(), annotation.source());
                if (this.sourceField != null) {
                    this.sourceField.setAccessible(true);
                }
            } else {
                this.sourceField = field;
            }

            if (StringUtils.hasText(annotation.target())) {
                this.targetField = ReflectionUtils.findField(
                    field.getDeclaringClass(), annotation.target());
                if (this.targetField != null) {
                    this.targetField.setAccessible(true);
                }
            } else {
                this.targetField = field;
            }
        }

        @Override
        public void collect(Object obj, TranslationBatch batch) throws Exception {
            if (sourceField == null) {
                return;
            }
            String code = (String) sourceField.get(obj);
            batch.addRegionCode(code);
        }

        @Override
        public void fill(Object obj, TranslationData data) throws Exception {
            if (sourceField == null || targetField == null) {
                return;
            }
            String code = (String) sourceField.get(obj);
            if (code == null) {
                return;
            }

            RegionInfo region = data.getRegion(code);
            if (region == null) {
                return;
            }

            Object value = extractValue(region, annotation.type(),
                annotation.separator(), annotation.limit());
            targetField.set(obj, value);
        }

        private Object extractValue(RegionInfo region, TransRegion.OutputType type,
                                    String separator, int limit) {
            switch (type) {
                case FULL:
                    return region;
                case NAME:
                    return region.getName();
                case FULLNAME:
                    List<String> fullName = region.getFullName();
                    if (fullName == null || fullName.isEmpty()) {
                        return region.getName();
                    }
                    int end = Math.min(limit, fullName.size());
                    return String.join(separator, fullName.subList(0, end));
                default:
                    return region.getName();
            }
        }
    }

    /**
     * 枚举字段
     */
    public static class EnumField extends TranslatableField {
        private final TransEnum annotation;
        private final Field targetField;

        EnumField(Field field, TransEnum annotation) {
            super(field, field.getName());
            this.annotation = annotation;

            if (StringUtils.hasText(annotation.target())) {
                this.targetField = ReflectionUtils.findField(
                    field.getDeclaringClass(), annotation.target());
                if (this.targetField != null) {
                    this.targetField.setAccessible(true);
                }
            } else {
                this.targetField = field;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void collect(Object obj, TranslationBatch batch) throws Exception {
            Object value = field.get(obj);
            if (value != null) {
                batch.addEnumValue(
                    (Class<? extends BaseEnum>) annotation.enumClass(),
                    value);
            }
        }

        @Override
        public void fill(Object obj, TranslationData data) throws Exception {
            if (targetField == null) {
                return;
            }
            Object value = field.get(obj);
            if (value == null) {
                return;
            }

            Enum enumVO = data.getEnum(annotation.enumClass(), value);
            if (enumVO == null) {
                return;
            }

            Object output = extractValue(enumVO, annotation.type());
            targetField.set(obj, output);
        }

        private Object extractValue(Enum vo, TransEnum.OutputType type) {
            switch (type) {
                case FULL:
                    return vo;
                case CODE:
                    return vo.getCode();
                case DESC:
                    return vo.getDesc();
                default:
                    return vo.getDesc();
            }
        }
    }

    /**
     * 嵌套对象字段（递归翻译）
     */
    public static class NestedField extends TranslatableField {
        private final TransNested annotation;

        NestedField(Field field, TransNested annotation) {
            super(field, field.getName());
            this.annotation = annotation;
        }

        @Override
        public void collect(Object obj, TranslationBatch batch) throws Exception {
            if (!annotation.recursive()) {
                return;
            }

            Object nested = field.get(obj);
            if (nested == null) {
                return;
            }

            if (nested instanceof Collection) {
                for (Object item : (Collection<?>) nested) {
                    if (item != null && !isBasicType(item.getClass())) {
                        TranslationMetadata.of(item.getClass()).collect(item, batch);
                    }
                }
            } else if (!isBasicType(nested.getClass())) {
                TranslationMetadata.of(nested.getClass()).collect(nested, batch);
            }
        }

        @Override
        public void fill(Object obj, TranslationData data) throws Exception {
            if (!annotation.recursive()) {
                return;
            }

            Object nested = field.get(obj);
            if (nested == null) {
                return;
            }

            if (nested instanceof Collection) {
                for (Object item : (Collection<?>) nested) {
                    if (item != null && !isBasicType(item.getClass())) {
                        TranslationMetadata.of(item.getClass()).fill(item, data);
                    }
                }
            } else if (!isBasicType(nested.getClass())) {
                TranslationMetadata.of(nested.getClass()).fill(nested, data);
            }
        }

        private boolean isBasicType(Class<?> clazz) {
            return clazz.isPrimitive()
                || clazz.isEnum()
                || clazz.getName().startsWith("java.")
                || clazz.getName().startsWith("javax.");
        }
    }
}
