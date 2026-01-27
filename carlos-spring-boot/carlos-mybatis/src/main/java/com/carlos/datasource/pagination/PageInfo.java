package com.carlos.datasource.pagination;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carlos.core.param.ParamPage;
import com.carlos.datasource.param.ParamPageOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自定义分页参数封装  对MybatisPlus 的分页参数进行二次封装 ，集成排序字段和查询参数的封装
 * </p>
 *
 * @author yunjin
 * @date 2020/4/14 14:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PageInfo<T> extends Page<T> {

    private static final long serialVersionUID = -2211095086394170578L;

    /**
     * 分页字段
     */
    private ParamPage paramPage;
    /**
     * 排序字段映射
     */
    private OrderMapping orderMapping;

    // region----------------------  带其他参数的构造方法 start  ------------------------

    /**
     * 传入分页参数
     */
    public PageInfo(ParamPage paramPage) {
        this(paramPage, null);
    }


    /**
     * 传入分页参数，排序参数，排序字段映射
     *
     * @param paramPage    分页参数
     * @param orderMapping 字段映射规则
     */
    public PageInfo(ParamPage paramPage, OrderMapping orderMapping) {
        this.paramPage = paramPage;
        if (paramPage != null) {
            super.setCurrent(paramPage.getCurrent());
            super.setSize(paramPage.getSize());
        }

        // 如果不为空 则进行字段映射
        if (orderMapping == null) {
            orderMapping = new OrderMapping(true);
        }

        if (paramPage instanceof ParamPageOrder) {
            ParamPageOrder pageOrder = (ParamPageOrder) paramPage;
            if (CollectionUtil.isNotEmpty(pageOrder.getSorts())) {
                // 进行排序字段映射
                orderMapping.mappingOrderItems(pageOrder.getSorts());
                super.setOrders(pageOrder.getSorts());
            }
        }
    }
    // endregion----------------------   带其他参数的构造方法 end   ------------------------


    public PageInfo(long current, long size) {
        super(current, size, 0);
    }

    public PageInfo(long current, long size, long total) {
        super(current, size, total, true);
    }

    public PageInfo(long current, long size, boolean isSearchCount) {
        super(current, size, isSearchCount);
    }

    public PageInfo(long current, long size, long total, boolean isSearchCount) {
        super(current, size, total, isSearchCount);
    }
}
