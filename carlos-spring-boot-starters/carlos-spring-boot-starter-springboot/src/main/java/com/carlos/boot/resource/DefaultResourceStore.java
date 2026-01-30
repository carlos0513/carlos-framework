package com.carlos.boot.resource;

import com.carlos.boot.resource.bean.ApplicationResource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 默认存储实现
 * </p>
 *
 * @author carlos
 * @date 2022/1/12 16:10
 */
@Slf4j
@AllArgsConstructor
public class DefaultResourceStore implements ResourceStore {


    @Override
    public void save(ApplicationResource resource) {

    }
}
