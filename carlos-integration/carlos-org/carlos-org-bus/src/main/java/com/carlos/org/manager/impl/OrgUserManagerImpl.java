package com.carlos.org.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.convert.OrgUserConvert;
import com.carlos.org.manager.OrgUserManager;
import com.carlos.org.mapper.OrgUserMapper;
import com.carlos.org.pojo.dto.OrgUserDTO;
import com.carlos.org.pojo.entity.OrgUser;
import com.carlos.org.pojo.param.OrgUserPageParam;
import com.carlos.org.pojo.vo.OrgUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 系统用户 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrgUserManagerImpl extends BaseServiceImpl<OrgUserMapper, OrgUser> implements OrgUserManager {

    @Override
    public boolean add(OrgUserDTO dto) {
        OrgUser entity = OrgUserConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'OrgUser' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        log.debug("Insert 'OrgUser' data: id:{}", entity.getId());
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
            log.warn("Remove 'OrgUser' data fail, id:{}", id);
            return false;
        }
        log.debug("Remove 'OrgUser' data by id:{}", id);
        return true;
    }

    @Override
    public boolean modify(OrgUserDTO dto) {
        OrgUser entity = OrgUserConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'OrgUser' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        log.debug("Update 'OrgUser' data by id:{}", dto.getId());
        return true;
    }

    @Override
    public OrgUserDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        OrgUser entity = getBaseMapper().selectById(id);
        return OrgUserConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<OrgUserVO> getPage(OrgUserPageParam param) {
        LambdaQueryWrapper<OrgUser> wrapper = queryWrapper();
        wrapper.select(

            OrgUser::getId,
            OrgUser::getAccount,
            OrgUser::getNickname,
            OrgUser::getPwd,
            OrgUser::getIdentify,
            OrgUser::getPhone,
            OrgUser::getAddress,
            OrgUser::getGender,
            OrgUser::getEmail,
            OrgUser::getAvatar,
            OrgUser::getDescription,
            OrgUser::getState,
            OrgUser::getMainDeptId,
            OrgUser::getLoginTime,
            OrgUser::getLoginIp,
            OrgUser::getLoginCount,
            OrgUser::getPwdLastModify,
            OrgUser::getCreateBy,
            OrgUser::getCreateTime,
            OrgUser::getUpdateBy,
            OrgUser::getUpdateTime,
            OrgUser::getTenantId
        );
        PageInfo<OrgUser> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, OrgUserConvert.INSTANCE::toVO);
    }

    @Override
    public OrgUserDTO getUserByAccount(String account) {
        if (account == null) {
            return null;
        }
        LambdaQueryWrapper<OrgUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgUser::getAccount, account)
            .eq(OrgUser::getDeleted, false)
            .last("LIMIT 1");
        OrgUser entity = getOne(wrapper);
        return OrgUserConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public OrgUserDTO getUserByPhone(String phone) {
        if (phone == null) {
            return null;
        }
        LambdaQueryWrapper<OrgUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgUser::getPhone, phone)
            .eq(OrgUser::getDeleted, false)
            .last("LIMIT 1");
        OrgUser entity = getOne(wrapper);
        return OrgUserConvert.INSTANCE.toDTO(entity);
    }

}
