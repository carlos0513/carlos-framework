package com.carlos.log.service;

import com.carlos.log.entity.SystemOperationLog;

public interface OperationLogService {

    boolean addSysLog(SystemOperationLog dto);
}
