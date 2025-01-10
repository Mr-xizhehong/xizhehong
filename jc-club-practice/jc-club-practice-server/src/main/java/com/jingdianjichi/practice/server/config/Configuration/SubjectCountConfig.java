package com.jingdianjichi.practice.server.config.Configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "subjectcount")
public class SubjectCountConfig {
    private Integer radio;
    private Integer multiple;
    private Integer judge;
    private Integer total;
}
