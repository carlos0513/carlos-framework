package com.carlos.system.news.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.carlos.system.news.pojo.dto.ImageDTO;
import com.carlos.system.news.pojo.vo.ImageVO;
import com.carlos.system.upload.service.FileService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.mapstruct.Named;

/**
 * <p>
 * 通用转换器
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
@Named("CommonConvert")
public class CommonConvert {

    /***
     * image对象列表转换为字符串以|分割
     * @param image
     * @return ava.lang.String
     * @author yunjin
     * @date 2022-11-15 14:13:53
     */
    @Named("stringToObjectList")
    public List<ImageVO> stringToList(String image) {
        if (StrUtil.isEmpty(image)) {
            return new ArrayList<>();
        }
        List<ImageVO> images = new ArrayList<>();
        FileService fileService = SpringUtil.getBean(FileService.class);
        try {
            String[] split = image.split(StrUtil.COMMA);
            for (String fileId : split) {
                ImageVO imageVO = new ImageVO();
                imageVO.setId(fileId);
                String url = fileService.getDownloadUrl(fileId);
                imageVO.setUrl(url);
                images.add(imageVO);
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return images;
    }

    /***
     * image对象列表转换为字符串以|分割
     * @param images
     * @return ava.lang.String
     * @author yunjin
     * @date 2022-11-15 14:13:53
     */
    @Named("objectListToString")
    public String objectListToString(List<ImageDTO> images) {
        if (CollectionUtils.isEmpty(images)) {
            return "";
        }
        List<String> idList = images.stream().filter(Objects::nonNull).map(ImageDTO::getId).collect(Collectors.toList());
        if (CollUtil.isEmpty(idList)) {
            return null;
        }
        return StrUtil.join(StrUtil.COMMA, idList);
    }


}
