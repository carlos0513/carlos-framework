package com.yunjin.org.pojo.dto;

import com.yunjin.json.jackson.annotation.UserIdField;
import com.yunjin.system.pojo.ao.ImageAO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class WarningDetailDTO extends AbstractMessageDetailDTO {
}
