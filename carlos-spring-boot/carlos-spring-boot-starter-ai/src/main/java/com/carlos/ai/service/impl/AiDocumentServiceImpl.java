package com.carlos.ai.service.impl;

import com.carlos.ai.enums.AiErrorCode;
import com.carlos.ai.exception.AiException;
import com.carlos.ai.service.AiDocumentService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * <p>
 * AI 文档解析服务实现
 * </p>
 *
 * @author carlos
 * @since 3.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiDocumentServiceImpl implements AiDocumentService {

    @Override
    public String parseDocument(InputStream inputStream, String fileName) {
        if (inputStream == null) {
            throw AiErrorCode.AI_DOCUMENT_PARSE_ERROR.exception("文档流不能为空");
        }
        if (fileName == null || fileName.isBlank()) {
            throw AiErrorCode.AI_DOCUMENT_PARSE_ERROR.exception("文件名不能为空");
        }
        try {
            String lowerName = fileName.toLowerCase();
            DocumentParser parser;
            if (lowerName.endsWith(".pdf")) {
                parser = new ApachePdfBoxDocumentParser();
            } else {
                throw AiErrorCode.AI_DOCUMENT_PARSE_ERROR.exception("不支持的文档格式: " + fileName);
            }
            Document document = parser.parse(inputStream);
            return document.text();
        } catch (AiException e) {
            throw e;
        } catch (Exception e) {
            log.error("文档解析失败, fileName={}", fileName, e);
            throw new AiException("文档解析失败", e);
        }
    }

}
