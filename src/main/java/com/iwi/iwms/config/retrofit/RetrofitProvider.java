package com.iwi.iwms.config.retrofit;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import retrofit2.Call;
import retrofit2.Response;

@Component
public class RetrofitProvider {
	
    public <T> T execute(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
            	throw new ResponseStatusException(response.code(), response.message(), null);  
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, e.getMessage(), e);
        }
    }
}
