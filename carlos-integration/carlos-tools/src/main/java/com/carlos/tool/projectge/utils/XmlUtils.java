package com.carlos.tool.projectge.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.XmlUtil;
import com.carlos.tool.projectge.entity.SelectTemplate;
import com.carlos.tool.projectge.exception.ReadXmlException;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.Map;

/**
 * <p>
 * 处理xml文件的相关工具类
 * </p>
 *
 * @author Carlos
 * @date 2021/10/26 12:07
 */
@Slf4j
public class XmlUtils {

    /**
     * 读取模板描述信息
     *
     * @param templateFile 模板配置文件
     * @return Map xml文件对应的Map
     * @author Carlos
     * @date 2019/6/10 13:35
     */
    public static SelectTemplate readTemplateBaseInfo(File templateFile) {
        Document document;
        try {
            document = XmlUtil.readXML(templateFile);
        } catch (Exception e) {
            log.error("模板描述文件读取失败， path=" + templateFile.getPath(), e);
            throw new ReadXmlException();
        }
        Element rootElement = XmlUtil.getRootElement(document);
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(rootElement);
        SelectTemplate templateInfo = BeanUtil.toBean(stringObjectMap, SelectTemplate.class, CopyOptions.create().setAutoTransCamelCase(true));
        templateInfo.setPath(templateFile.getParent());
        return templateInfo;
    }
}
