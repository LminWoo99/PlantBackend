package com.example.plantchatservice.client.error;


import com.example.plantchatservice.common.exception.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {
    Environment env;
    @Autowired
    public FeignErrorDecoder(Environment env) {
        this.env = env;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                break;
            case 404:
                if (methodKey.contains("find")) {
                    throw ErrorCode.throwMemberNotFound();
                }
                if (methodKey.contains("boardContent")) {
                    throw ErrorCode.throwTradeBoardNotFound();
                }
                break;
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
