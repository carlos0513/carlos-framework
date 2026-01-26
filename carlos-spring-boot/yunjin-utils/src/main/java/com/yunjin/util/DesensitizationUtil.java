package com.yunjin.util;

import cn.hutool.core.util.StrUtil;
import com.yunjin.util.constants.CharConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.Arrays;

/**
 * @author yunjin @WebSite www.yunjin.cn @Description: 脱敏工具类
 */
public class DesensitizationUtil {

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     *
     * @param fullName 全名
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String chineseName(@Nullable final String fullName) {
        return sensitive(fullName, 1, 0);
    }

    /**
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     *
     * @param id 身份证号
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String idCardNum(@Nullable final String id) {
        return sensitive(id, 0, 4);
    }

    /**
     * [固定电话] 后四位，其他隐藏<例子：****1234>
     *
     * @param num 固定电话号
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String phoneNo(@Nullable final String num) {
        return sensitive(num, 0, 4);
    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138****1234>
     *
     * @param num 手机号
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String mobileNo(@Nullable final String num) {
        return sensitive(num, 3, 4);
    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     *
     * @param address       地区
     * @param sensitiveSize 敏感信息长度
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String address(@Nullable final String address, final int sensitiveSize) {
        return sensitive(address, 0, sensitiveSize);
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     *
     * @param email 邮箱
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String email(@Nullable final String email) {
        if (StringUtils.isEmpty(email)) {
            return StringUtils.EMPTY;
        }
        final int index = email.indexOf(CharConstants.AT);
        if (index <= 1) {
            return email;
        } else {
            return sensitive(email, 1, email.length() - index);
        }
    }

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:622260***********1234>
     *
     * @param cardNum 银行卡号
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String bankCard(@Nullable final String cardNum) {
        return sensitive(cardNum, 6, 4);
    }

    /**
     * [公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号<例子:12********>
     *
     * @param code 银行联行号
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String cnapsCode(@Nullable final String code) {
        return sensitive(code, 2, 0);
    }

    /**
     * 右边脱敏
     *
     * @param sensitiveStr 待脱敏的字符串
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String right(@Nullable final String sensitiveStr) {
        if (StringUtils.isEmpty(sensitiveStr)) {
            return StringUtils.EMPTY;
        }
        final int length = sensitiveStr.length();
        return sensitive(sensitiveStr, length / 2, 0);
    }

    /**
     * 左边脱敏
     *
     * @param sensitiveStr 待脱敏的字符串
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String left(@Nullable final String sensitiveStr) {
        if (StringUtils.isEmpty(sensitiveStr)) {
            return StringUtils.EMPTY;
        }
        final int length = sensitiveStr.length();
        return sensitive(sensitiveStr, 0, length / 2);
    }

    /**
     * 中间脱敏，保留两端
     *
     * @param sensitiveStr 待脱敏的字符串
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String middle(@Nullable final String sensitiveStr) {
        if (StringUtils.isEmpty(sensitiveStr)) {
            return StringUtils.EMPTY;
        }
        final int length = sensitiveStr.length();
        if (length < 3) {
            return StringUtils.leftPad(StringUtils.EMPTY, length, CharConstants.STAR);
        }
        final char[] chars = new char[length];
        final int last = length - 1;
        Arrays.fill(chars, 1, last, CharConstants.STAR);
        chars[0] = sensitiveStr.charAt(0);
        chars[last] = sensitiveStr.charAt(last);
        return new String(chars);
    }

    /**
     * 自适应脱敏
     *
     * @param str 脱敏字符串
     * @return java.lang.String
     * @author Carlos
     * @date 2023/2/8 15:50
     */
    public static String adaptive(@Nullable final String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        final int length = str.length();
        if (length == 1) {
            return str;
        }
        if (length == 2) {
            return StrUtil.replaceByCodePoint(str, 0, 1, '*');
        }
        // 字符保留长度
        final int l = length / 2 + 1;
        final int pre = l / 2;
        final int end = l - pre;
        final int startIndex = pre == 0 ? 0 : pre - 1;
        final int endIndex = length - end;
        return StrUtil.replaceByCodePoint(str, startIndex + 1, endIndex, '*');
    }

    /**
     * 全部脱敏
     *
     * @param sensitiveStr 待脱敏的字符串
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String all(@Nullable final String sensitiveStr) {
        return sensitive(sensitiveStr, 0, 0);
    }

    /**
     * 文本脱敏
     *
     * @param str       字符串
     * @param fromIndex 开始的索引
     * @param lastSize  尾部长度
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String sensitive(@Nullable final String str, final int fromIndex, final int lastSize) {
        return sensitive(str, fromIndex, lastSize, CharConstants.STAR);
    }

    /**
     * 文本脱敏
     *
     * @param str       字符串
     * @param fromIndex 开始的索引
     * @param lastSize  尾部长度
     * @param padSize   填充的长度
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String sensitive(@Nullable final String str, final int fromIndex, final int lastSize, final int padSize) {
        return sensitive(str, fromIndex, lastSize, CharConstants.STAR, padSize);
    }

    /**
     * 文本脱敏
     *
     * @param str       字符串
     * @param fromIndex 开始的索引
     * @param lastSize  尾部长度
     * @param padChar   填充的字符
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String sensitive(@Nullable final String str, final int fromIndex, final int lastSize, final char padChar) {
        return sensitive(str, fromIndex, lastSize, padChar, -1);
    }

    /**
     * 文本脱敏
     *
     * @param str       字符串
     * @param fromIndex 开始的索引
     * @param lastSize  尾部长度
     * @param padChar   填充的字符
     * @param padSize   填充的长度
     * @return 脱敏后的字符串
     */
    @Nullable
    public static String sensitive(
            @Nullable final String str, final int fromIndex, final int lastSize, final char padChar, final int padSize) {
        if (str == null) {
            return null;
        }
        if (StringUtils.isEmpty(str)) {
            return StringUtils.EMPTY;
        }
        final int length = str.length();
        // 全部脱敏
        if (fromIndex == 0 && lastSize == 0) {
            final int padSiz = padSize > 0 ? padSize : length;
            return StringUtils.repeat(CharConstants.STAR, padSiz);
        }
        final int toIndex = length - lastSize;
        final int padSiz = padSize > 0 ? padSize : toIndex - fromIndex;
        // 头部脱敏
        if (fromIndex == 0) {
            final String tail = str.substring(toIndex);
            return StringUtils.repeat(padChar, padSiz).concat(tail);
        }
        // 尾部脱敏
        if (toIndex == length) {
            final String head = str.substring(0, fromIndex);
            return head.concat(StringUtils.repeat(padChar, padSiz));
        }
        // 中部
        final String head = str.substring(0, fromIndex);
        final String tail = str.substring(toIndex);
        return head + StringUtils.repeat(padChar, padSiz) + tail;
    }
}
