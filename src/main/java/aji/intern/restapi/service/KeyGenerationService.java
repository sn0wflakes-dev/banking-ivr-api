package aji.intern.restapi.service;

public interface KeyGenerationService {
    String registerServiceKey();
    void removeServiceKey();
    String rotateKey();
}
