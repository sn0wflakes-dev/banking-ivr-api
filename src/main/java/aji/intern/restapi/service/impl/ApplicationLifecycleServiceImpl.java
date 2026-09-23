package aji.intern.restapi.service.impl;

import aji.intern.restapi.config.ApplicationConfig;
import aji.intern.restapi.service.ApplicationLifecycleService;
import aji.intern.restapi.service.KeyGenerationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ApplicationLifecycleServiceImpl implements ApplicationLifecycleService {

    private static final Logger log = LogManager.getLogger(ApplicationLifecycleServiceImpl.class);

    private final KeyGenerationService service;
    private final ApplicationConfig config;
    private final ApplicationContext context;

    public ApplicationLifecycleServiceImpl(
            KeyGenerationService service,
            ApplicationConfig config,
            ApplicationContext context) {
        this.service = service;
        this.config = config;
        this.context = context;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Override
    public void registerServiceKeyOnAppReady() {
        try {
            String key = service.registerServiceKey();
            config.setServiceKey(key);
            log.info("Inject key success: service key successfully injected");
        } catch (Exception e) {
            log.warn("Inject key failed: error while executing task, error detail : {}", e.getMessage());
            int exitCode = 1;
            System.exit(SpringApplication.exit(context, () -> exitCode));
        }
    }

    @EventListener(ContextClosedEvent.class)
    @Override
    public void removeServiceKeyOnAppShutdown() {
        service.removeServiceKey();
        config.setServiceKey("empty");
        log.info("Remove key success: service key successfully removed");
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void scheduledRotationKey() {
        try {
            String key = service.rotateKey();
            config.setServiceKey(key);
            log.info("Rotate key success: service key successfully rotated");
        } catch (Exception e) {
            log.warn("Rotate key failed: error while executing task, error detail : {}", e.getMessage());
        }
    }
}
