package com.carlos.mongodb.service.impl;

import com.carlos.mongodb.base.BaseServiceImpl;
import com.carlos.mongodb.entity.LogDocument;
import com.carlos.mongodb.repository.LogDocumentRepository;
import com.carlos.mongodb.service.LogDocumentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日志文档 Service 实现类
 *
 * @author Carlos
 * @date 2026/3/15
 */
@Service
public class LogDocumentServiceImpl extends BaseServiceImpl<LogDocumentRepository, LogDocument, String> implements LogDocumentService {

    private final LogDocumentRepository logDocumentRepository;

    public LogDocumentServiceImpl(LogDocumentRepository baseRepository, org.springframework.data.mongodb.core.MongoTemplate mongoTemplate, LogDocumentRepository logDocumentRepository) {
        super(baseRepository, null, mongoTemplate);
        this.logDocumentRepository = logDocumentRepository;
    }

    @Override
    public LogDocumentRepository getBaseRepository() {
        return logDocumentRepository;
    }

    @Override
    public Class<LogDocument> getEntityClass() {
        return LogDocument.class;
    }

    @Override
    public List<LogDocument> findByLevel(String level) {
        return logDocumentRepository.findByLevel(level);
    }

    @Override
    public Page<LogDocument> findByLevel(String level, Pageable pageable) {
        return logDocumentRepository.findByLevel(level, pageable);
    }

    @Override
    public List<LogDocument> findByModuleLike(String module) {
        return logDocumentRepository.findByModuleLike(module);
    }

    @Override
    public List<LogDocument> findByUserId(Long userId) {
        return logDocumentRepository.findByUserId(userId);
    }

    @Override
    public List<LogDocument> findBySuccess(Boolean success) {
        return logDocumentRepository.findBySuccess(success);
    }

    @Override
    public Page<LogDocument> page(Pageable pageable) {
        return logDocumentRepository.findAll(pageable);
    }
}
