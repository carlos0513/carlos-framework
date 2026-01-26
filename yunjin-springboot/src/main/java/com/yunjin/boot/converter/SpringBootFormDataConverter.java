package com.yunjin.boot.converter;

import com.yunjin.json.util.ConvertUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;


/**
 * <p>
 * 表单数据转换器，该转换器不适用于参数为json的请求
 * </p>
 *
 * @author yunjin
 * @date 2022/3/16 21:36
 */
public class SpringBootFormDataConverter {

    /**
     * <p>
     * 日期转换器,将请求参数的日期字符串转换成java.util.Date类型
     * </p>
     *
     * @author yunjin
     * @date 2020/4/14 12:15
     */
    public static class StringToDateConverter implements Converter<String, Date> {

        @Override
        public Date convert(@Nullable final String source) {
            return ConvertUtil.string2Date(source);
        }
    }

    /**
     * <p>
     * String 转 Double 转换器
     * </p>
     *
     * @author yunjin
     * @date 2020/4/14 13:02
     */
    public static class StringToDoubleConverter implements Converter<String, Double> {

        @Override
        public Double convert(@Nullable final String source) {
            return ConvertUtil.string2Double(source);
        }
    }

    /**
     * <p>
     * String 转 Integer转换器
     * </p>
     *
     * @author yunjin
     * @date 2020/4/14 13:02
     */
    public static class StringToIntegerConverter implements Converter<String, Integer> {

        @Override
        public Integer convert(@Nullable final String source) {
            return ConvertUtil.string2Integer(source);
        }
    }

    /**
     * <p>
     * String 转 LocalDateTime 转换器
     * </p>
     *
     * @author yunjin
     * @date 2020/4/14 13:02
     */
    public static class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

        @Override
        public LocalDateTime convert(@Nullable final String source) {
            return ConvertUtil.string2LocalDateTime(source);
        }
    }

    /**
     * <p>
     * String 转 LocalDate 转换器
     * </p>
     *
     * @author yunjin
     * @date 2020/4/14 13:02
     */
    public static class StringToLocalDateConverter implements Converter<String, LocalDate> {

        @Override
        public LocalDate convert(@Nullable final String source) {
            return ConvertUtil.string2LocalDate(source);
        }
    }

    /**
     * <p>
     * String 转 LocalDate 转换器
     * </p>
     *
     * @author yunjin
     * @date 2020/4/14 13:02
     */
    public static class StringToLocalTimeConverter implements Converter<String, LocalTime> {

        @Override
        public LocalTime convert(@Nullable final String source) {
            return ConvertUtil.string2LocalTime(source);
        }
    }

}
