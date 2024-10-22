package com.happychat.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("appConfig")
@Data
public class AppConfig {
//    读取application.properties配置

    @Value("${ws.port:}")
    private Integer wsPort;

    @Value("${project.folder:}")
    private String projectFolder;

    @Value("${admin.emails:}")
    private String adminEmails;
}
