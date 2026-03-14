package com.carlos.mongodb.tool;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * <p>
 * MongoDB 查询构建器
 * 提供便捷的查询条件构建方法
 * </p>
 *
 * @author carlos
 * @date 2024/01/15
 */
public class MongoQueryBuilder {

    private final Query query;
    private final Criteria criteria;

    public MongoQueryBuilder() {
        this.criteria = new Criteria();
        this.query = new Query(this.criteria);
    }

    /**
     * 创建新的查询构建器
     */
    public static MongoQueryBuilder create() {
        return new MongoQueryBuilder();
    }

    /**
     * 等于
     */
    public MongoQueryBuilder eq(String field, Object value) {
        if (value != null) {
            this.query.addCriteria(Criteria.where(field).is(value));
        }
        return this;
    }

    /**
     * 不等于
     */
    public MongoQueryBuilder ne(String field, Object value) {
        if (value != null) {
            this.query.addCriteria(Criteria.where(field).ne(value));
        }
        return this;
    }

    /**
     * 大于
     */
    public MongoQueryBuilder gt(String field, Object value) {
        if (value != null) {
            this.query.addCriteria(Criteria.where(field).gt(value));
        }
        return this;
    }

    /**
     * 大于等于
     */
    public MongoQueryBuilder gte(String field, Object value) {
        if (value != null) {
            this.query.addCriteria(Criteria.where(field).gte(value));
        }
        return this;
    }

    /**
     * 小于
     */
    public MongoQueryBuilder lt(String field, Object value) {
        if (value != null) {
            this.query.addCriteria(Criteria.where(field).lt(value));
        }
        return this;
    }

    /**
     * 小于等于
     */
    public MongoQueryBuilder lte(String field, Object value) {
        if (value != null) {
            this.query.addCriteria(Criteria.where(field).lte(value));
        }
        return this;
    }

    /**
     * 范围查询（包含边界）
     */
    public MongoQueryBuilder between(String field, Object start, Object end) {
        if (start != null && end != null) {
            this.query.addCriteria(Criteria.where(field).gte(start).lte(end));
        } else if (start != null) {
            gte(field, start);
        } else if (end != null) {
            lte(field, end);
        }
        return this;
    }

    /**
     * 模糊查询（包含指定字符串）
     */
    public MongoQueryBuilder like(String field, String value) {
        if (value != null && !value.isEmpty()) {
            this.query.addCriteria(Criteria.where(field).regex(".*" + Pattern.quote(value) + ".*", "i"));
        }
        return this;
    }

    /**
     * 左模糊查询（以指定字符串结尾）
     */
    public MongoQueryBuilder likeLeft(String field, String value) {
        if (value != null && !value.isEmpty()) {
            this.query.addCriteria(Criteria.where(field).regex(".*" + Pattern.quote(value) + "$", "i"));
        }
        return this;
    }

    /**
     * 右模糊查询（以指定字符串开头）
     */
    public MongoQueryBuilder likeRight(String field, String value) {
        if (value != null && !value.isEmpty()) {
            this.query.addCriteria(Criteria.where(field).regex("^" + Pattern.quote(value) + ".*", "i"));
        }
        return this;
    }

    /**
     * 正则匹配
     */
    public MongoQueryBuilder regex(String field, String regex) {
        if (regex != null && !regex.isEmpty()) {
            this.query.addCriteria(Criteria.where(field).regex(regex));
        }
        return this;
    }

    /**
     * 正则匹配（带选项）
     */
    public MongoQueryBuilder regex(String field, String regex, String options) {
        if (regex != null && !regex.isEmpty()) {
            this.query.addCriteria(Criteria.where(field).regex(regex, options));
        }
        return this;
    }

    /**
     * 在集合中
     */
    public MongoQueryBuilder in(String field, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            this.query.addCriteria(Criteria.where(field).in(values));
        }
        return this;
    }

    /**
     * 在集合中
     */
    public MongoQueryBuilder in(String field, Object... values) {
        if (values != null && values.length > 0) {
            this.query.addCriteria(Criteria.where(field).in(values));
        }
        return this;
    }

    /**
     * 不在集合中
     */
    public MongoQueryBuilder nin(String field, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            this.query.addCriteria(Criteria.where(field).nin(values));
        }
        return this;
    }

    /**
     * 为空
     */
    public MongoQueryBuilder isNull(String field) {
        this.query.addCriteria(Criteria.where(field).isNull());
        return this;
    }

    /**
     * 不为空
     */
    public MongoQueryBuilder isNotNull(String field) {
        this.query.addCriteria(Criteria.where(field).ne(null));
        return this;
    }

    /**
     * 存在该字段
     */
    public MongoQueryBuilder exists(String field, boolean exists) {
        this.query.addCriteria(Criteria.where(field).exists(exists));
        return this;
    }

    /**
     * 添加自定义 Criteria
     */
    public MongoQueryBuilder addCriteria(Criteria criteria) {
        this.query.addCriteria(criteria);
        return this;
    }

    /**
     * OR 条件
     */
    public MongoQueryBuilder or(Criteria... criterias) {
        this.query.addCriteria(new Criteria().orOperator(criterias));
        return this;
    }

    /**
     * AND 条件
     */
    public MongoQueryBuilder and(Criteria... criterias) {
        this.query.addCriteria(new Criteria().andOperator(criterias));
        return this;
    }

    /**
     * 构建 Query 对象
     */
    public Query build() {
        return this.query;
    }
}
