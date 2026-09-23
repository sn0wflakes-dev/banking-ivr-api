package aji.intern.restapi.config;

import aji.intern.restapi.client.api.KeyClient;
import aji.intern.restapi.client.api.OtpClient;
import aji.intern.restapi.client.dto.ErrorResponse;
import aji.intern.restapi.error.RestApiClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Duration;

@Configuration
public class ApiClientConfig {

    @Value("${rest.services.base-url}")
    private String baseUrl;

    private final int connectionTimeout;
    private final int requestTimeout;
    private final ObjectMapper objectMapper;

    public ApiClientConfig(
            @Value("${rest.timeout.connection}") int connectionTimeout,
            @Value("${rest.timeout.request}") int requestTimeout, ObjectMapper objectMapper) {
        this.connectionTimeout = connectionTimeout;
        this.requestTimeout = requestTimeout;
        this.objectMapper = objectMapper;
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpClientSettings settings = HttpClientSettings.defaults()
                .withConnectTimeout(Duration.ofSeconds(requestTimeout))
                .withReadTimeout(Duration.ofSeconds(connectionTimeout));

        return ClientHttpRequestFactoryBuilder.jdk().build(settings);
    }
    
    @Bean
    public KeyClient keyClient() {
        RestClient client = client();
        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(KeyClient.class);
    }

    @Bean
    public OtpClient otpClient() {
        RestClient client = client();
        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(OtpClient.class);
    }

    private RestClient client() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(clientHttpRequestFactory())
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    ErrorResponse errorResponse;
                    try {
                        errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);
                    } catch (IOException e) {
                        throw new RestClientException("Failed to parse error body");
                    }
                    throw new RestApiClientException(response.getStatusCode(), errorResponse);
                }).build();
    }
}
