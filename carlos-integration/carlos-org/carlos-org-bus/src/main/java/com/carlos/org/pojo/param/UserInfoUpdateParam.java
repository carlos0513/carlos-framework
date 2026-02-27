package com.carlos.org.pojo.param;

import cn.hutool.core.lang.RegexPool;
import com.carlos.org.pojo.enums.UserGenderEnum;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "个人中心用户修改参数")
public class UserInfoUpdateParam {

    @Schema(description = "主键")
    @Hidden
    private Long id;
    @Schema(description = "真实姓名")
    private String realname;
    @Schema(description = "账号")
    private String account;
    @Pattern(regexp = RegexPool.MOBILE, message = "手机号格式不正确")
    @Schema(description = "手机号码")
    private String phone;
    @Schema(description = "性别")
    private UserGenderEnum gender;
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "介绍")
    private String description;
    @Schema(description = "头像ID（需要更新头像时，传空）")
    private HeaderImage head;
    @Schema(description = "电子签名ID")
    private String signature;


    @Data
    public static class HeaderImage {

        /**
         * 主键
         */
        private Long id;
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
