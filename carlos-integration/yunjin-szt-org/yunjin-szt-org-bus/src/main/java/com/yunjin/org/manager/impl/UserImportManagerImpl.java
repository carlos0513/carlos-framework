package com.yunjin.org.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.datasource.pagination.MybatisPage;
import com.yunjin.datasource.pagination.PageInfo;
import com.yunjin.org.convert.UserImportConvert;
import com.yunjin.org.manager.UserImportManager;
import com.yunjin.org.mapper.UserImportMapper;
import com.yunjin.org.pojo.dto.UserImportDTO;
import com.yunjin.org.pojo.entity.UserImport;
import com.yunjin.org.pojo.param.UserImportPageParam;
import com.yunjin.org.pojo.vo.UserImportVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 用户导入 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2023-5-27 12:52:09
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class UserImportManagerImpl extends BaseServiceImpl<UserImportMapper, UserImport> implements UserImportManager {

    @Override
    public boolean add(UserImportDTO dto) {
        UserImport entity = UserImportConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'UserImport' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'UserImport' data: id:{}", entity.getId());
        }
        return true;
    }

    @Override
    public boolean delete(Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        boolean success = removeById(id);
        if (!success) {
            log.warn("Remove 'UserImport' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'UserImport' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(UserImportDTO dto) {
        UserImport entity = UserImportConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'UserImport' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'UserImport' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public UserImportDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        UserImport entity = getBaseMapper().selectById(id);
        return UserImportConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<UserImportVO> getPage(UserImportPageParam param) {
        LambdaQueryWrapper<UserImport> wrapper = queryWrapper();
        wrapper.select(
                UserImport::getId,
                UserImport::getAccount,
                UserImport::getRealname,
                UserImport::getIdentify,
                UserImport::getPhone,
                UserImport::getRole,
                UserImport::getDepartment,
                UserImport::getRegionCode,
                UserImport::getGender,
                UserImport::getEmail
        );
        PageInfo<UserImport> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, UserImportConvert.INSTANCE::toVO);
    }

}
