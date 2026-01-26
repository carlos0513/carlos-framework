package com.yunjin.mongodb.page;

import com.yunjin.core.pagination.PageConvert;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * mongo分页工具
 *
 * @author Carlos
 * @date 2021/12/17 17:21
 */
public class MongoPage {

    /**
     * 根据分页参数获取分页对象
     *
     * @param param 分页参数
     * @return org.springframework.data.domain.Pageable
     * @author Carlos
     * @date 2021/12/17 17:16
     */
    public static Pageable page(final ParamPage param) {
        return PageRequest.of((int) param.getCurrent() - 1, (int) param.getSize());
    }

    /**
     * mongodb分页对象转换
     *
     * @param page    原分页信息
     * @param convert 转换器
     * @return com.soundtao.core.pagination.Paging<V>
     * @author Carlos
     * @date 2021/12/17 17:03
     */
    public static <V, D> Paging<V> convert(final Page<D> page, final PageConvert<V, D> convert) {
        final Paging<V> pageInfo = new Paging<>();
        final Pageable pageable = page.getPageable();
        pageInfo.setTotal((int) page.getTotalElements());
        pageInfo.setRecords(convert.convert(page.getContent()));
        pageInfo.setCurrent(pageable.getPageNumber());
        pageInfo.setSize(page.getSize());
        pageInfo.setPages(page.getTotalPages());
        return pageInfo;
    }
}
