package aji.intern.restapi.config;

import aji.intern.core.bank.account.Account;
import aji.intern.core.bank.account.AccountService;
import jakarta.xml.ws.BindingProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

@Configuration
public class SoapServiceConfig {

    private static final Logger log = LogManager.getLogger(SoapServiceConfig.class);

    @Value("${soap.services.account.path}")
    private String accountServicePath;

    @Value("${soap.services.account.endpoint}")
    private String accountServiceEndpoint;

    @Value("${soap.timeout.request}")
    private Integer requestTimeout;

    @Value("${soap.timeout.connection}")
    private Integer connectionTimeout;

    @Bean
    public Account accountService() throws MalformedURLException {
        log.info("INITIALIZE ACCOUNT SERVICE SOAP");
        URL wsdlLocation = Path.of(accountServicePath).toUri().toURL();
        AccountService service = new AccountService(wsdlLocation);
        Account port = service.getAccountSoap11();

        Map<String, Object> context = ((BindingProvider) port).getRequestContext();
        context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, accountServiceEndpoint);
        context.put("com.sun.xml.ws.connect.timeout", connectionTimeout * 1000);
        context.put("com.sun.xml.ws.request.timeout", requestTimeout * 1000);

        return port;
    }

}
