package aji.intern.restapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ApplicationConfig {
    @Value("${application.config.service-id}")
    private String serviceId;

    @Value("${application.config.service-key: empty}")
    private String serviceKey;
}
