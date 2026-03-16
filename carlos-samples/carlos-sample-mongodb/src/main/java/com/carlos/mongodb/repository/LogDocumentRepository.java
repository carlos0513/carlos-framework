package com.carlos.mongodb.repository;

import com.carlos.mongodb.entity.LogDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 日志文档 Repository 接口
 *
 * @author Carlos
 * @date 2026/3/15
 */
@Repository
public interface LogDocumentRepository extends MongoRepository<LogDocument, String> {

    /**
     * 根据日志级别查询
     *
     * @param level 日志级别
     * @return 日志列表
     */
    List<LogDocument> findByLevel(String level);

    /**
     * 根据日志级别分页查询
     *
     * @param level    日志级别
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<LogDocument> findByLevel(String level, Pageable pageable);

    /**
     * 根据模块名称模糊查询
     *
     * @param module 模块名称
     * @return 日志列表
     */
    List<LogDocument> findByModuleLike(String module);

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return 日志列表
     */
    List<LogDocument> findByUserId(Long userId);

    /**
     * 根据是否成功查询
     *
     * @param success 是否成功
     * @return 日志列表
     */
    List<LogDocument> findBySuccess(Boolean success);
}
