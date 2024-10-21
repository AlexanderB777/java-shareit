package ru.practicum.util.config;

import feign.Client;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.httpclient.ApacheHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.util.exception.FeignResponseException;

@Configuration
public class FeignClientConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder.Default() {
            @Override
            public Exception decode(String methodKey, Response response) {
                return new FeignResponseException(response);
            }
        };
    }

    @Bean
    public Client feignClient() {
        return new ApacheHttpClient();
    }
}