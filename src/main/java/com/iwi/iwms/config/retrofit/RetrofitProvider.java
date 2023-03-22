package com.iwi.iwms.config.retrofit;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;

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
				throw new CommonException(ErrorCode.TARGET_DATA_NOT_EXISTS, response.message());
            }
        } catch (IOException e) {
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, e.getMessage());
        }
    }
}
