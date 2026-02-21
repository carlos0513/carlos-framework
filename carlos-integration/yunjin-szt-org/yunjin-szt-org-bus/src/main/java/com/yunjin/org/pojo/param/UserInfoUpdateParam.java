package com.yunjin.org.pojo.param;

import cn.hutool.core.lang.RegexPool;
import com.yunjin.org.pojo.enums.UserGenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
@Schema(value = "个人中心用户修改参数", description = "个人中心用户修改参数")
public class UserInfoUpdateParam {

    @Schema(value = "主键", hidden = true)
    private String id;
    @Schema(value = "真实姓名")
    private String realname;
    @Schema(value = "账号")
    private String account;
    @Pattern(regexp = RegexPool.MOBILE, message = "手机号格式不正确")
    @Schema(value = "手机号码")
    private String phone;
    @Schema(value = "性别")
    private UserGenderEnum gender;
    @Email(message = "邮箱格式不正确")
    @Schema(value = "邮箱")
    private String email;
    @Schema(value = "介绍")
    private String description;
    @Schema(value = "头像ID（需要更新头像时，传空）")
    private HeaderImage head;
    @Schema(value = "电子签名ID")
    private String signature;


    @Data
    public static class HeaderImage {

        /**
         * 主键
         */
        private String id;
        /**
         * 文件分组
         */
        private String groupId;
        /**
         * 文件名称
         */
        private String name;
        /**
         * 文件url
         */
        private String url;
    }
}
