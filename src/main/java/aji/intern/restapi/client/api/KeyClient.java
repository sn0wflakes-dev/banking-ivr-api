package aji.intern.restapi.client.api;

import aji.intern.restapi.client.dto.ApiResponse;
import aji.intern.restapi.client.dto.key.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange("/api/key")
public interface KeyClient {
    @PostExchange()
    ResponseEntity<ApiResponse<RegisterServiceApiResponse>> registerKey(@RequestBody RegisterServiceApiRequest request);

    @DeleteExchange()
    ResponseEntity<ApiResponse<RemoveKeyApiResponse>> removeKey(@RequestBody RemoveKeyApiRequest request);

    @PutExchange()
    ResponseEntity<ApiResponse<RotateKeyResponse>> rotateKey(@RequestBody RotateKeyApiRequest request);
}
