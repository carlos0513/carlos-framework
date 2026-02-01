package com.carlos.datasource.pagination;

import com.carlos.core.pagination.PageConvert;
import com.carlos.core.pagination.Paging;

/**
 * <p>
 * mybatis分页工具
 * </p>
 *
 * @author carlos
 * @date 2021/12/17 17:21
 */
public class MybatisPage {

    /**
     * mongodb分页对象转换
     *
     * @param page    原分页信息
     * @param convert 转换器
     * @return com.carlos.core.pagination.Paging<V>
     * @author carlos
     * @date 2021/12/17 17:03
     */
    public static <V, D> Paging<V> convert(PageInfo<D> page, PageConvert<V, D> convert) {
        Paging<V> pageInfo = new Paging<>();
        pageInfo.setTotal(Long.valueOf(page.getTotal()).intValue());
        pageInfo.setRecords(convert.convert(page.getRecords()));
        pageInfo.setCurrent(Long.valueOf(page.getCurrent()).intValue());
        pageInfo.setSize(Long.valueOf(page.getSize()).intValue());
        pageInfo.setPages(Long.valueOf(page.getPages()).intValue());
        return pageInfo;
    }


}
