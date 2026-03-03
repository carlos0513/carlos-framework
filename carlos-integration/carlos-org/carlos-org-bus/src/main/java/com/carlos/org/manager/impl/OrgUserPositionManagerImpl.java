package com.carlos.org.manager.impl;

import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.org.manager.OrgUserPositionManager;
import com.carlos.org.mapper.OrgUserPositionMapper;
import com.carlos.org.pojo.entity.OrgUserPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 用户岗位 查询封装实现类
 * </p>
 *
 * @author carlos
 * @date 2026-03-03
 */
@Slf4j
@Component
public class OrgUserPositionManagerImpl extends BaseServiceImpl<OrgUserPositionMapper, OrgUserPosition> implements OrgUserPositionManager {

}
