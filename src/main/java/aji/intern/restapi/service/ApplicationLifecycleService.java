package aji.intern.restapi.service;

public interface ApplicationLifecycleService {
    void registerServiceKeyOnAppReady();
    void removeServiceKeyOnAppShutdown();
    void scheduledRotationKey();
}
