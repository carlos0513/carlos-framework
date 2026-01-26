package com.yunjin.log.service;

import com.yunjin.log.entity.SystemOperationLog;

public interface OperationLogService {

    boolean addSysLog(SystemOperationLog dto);
}
