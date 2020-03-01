package io.github.cemalunal.backend.response;

import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {

    public <T> Response<T> createResponse(String message, T data) {
        return new Response<T>()
                .setMessage(message)
                .setData(data);
    }
}
