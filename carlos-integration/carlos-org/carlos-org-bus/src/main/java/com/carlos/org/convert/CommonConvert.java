package com.carlos.org.convert;

import cn.hutool.core.util.StrUtil;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.pojo.dto.UserDepartmentDTO;
import com.carlos.org.pojo.enums.UserGenderEnum;
import com.carlos.org.pojo.vo.UserDepartmentVO;
import org.mapstruct.Named;

import java.io.Serializable;

/**
 * <p>
 * 通用转换器
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Named("CommonConvert")
public class CommonConvert {

    /**
     * 样例方法
     *
     * @param id source字段绑定的参数
     * @return java.lang.String
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    @Named("toName")
    public String toName(Serializable id) {
        if (id == null) {
            return null;
        }
        // MenuManager menuManager = SpringUtil.getBean(MenuManager.class);
        // return menuManager.getNameById(menuId);
        return "";
    }

    @Named("getDeptNameById")
    public String getDeptNameById(Serializable id) {
        if (id == null) {
            return null;
        }
        // MenuManager menuManager = SpringUtil.getBean(MenuManager.class);
        // return menuManager.getNameById(menuId);
        return "";
    }


    @Named("toGender")
    public UserGenderEnum toGender(String gender) {
        if (StrUtil.isBlank(gender)) {
            return UserGenderEnum.UNKNOWN;
        }
        switch (gender) {
            case "男":
                return UserGenderEnum.MALE;
            case "女":
                return UserGenderEnum.FEMALE;
            default:
                return UserGenderEnum.UNKNOWN;
        }
    }

    @Named("toUserVO")
    public PageInfo<UserDepartmentVO> toUserVO(PageInfo<UserDepartmentDTO> userPages) {
        PageInfo<UserDepartmentVO> newUserPage = new PageInfo<>(userPages.getParamPage());
        newUserPage.setCurrent(userPages.getCurrent());
        newUserPage.setSize(userPages.getSize());
        newUserPage.setTotal(userPages.getTotal());
        newUserPage.setPages(userPages.getPages());
        newUserPage.setRecords(UserDepartmentConvert.INSTANCE.dtoToVO(userPages.getRecords()));
        newUserPage.setParamPage(userPages.getParamPage());
        return newUserPage;
    }
}
