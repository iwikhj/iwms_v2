package com.iwi.iwms.config.retrofit;

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
    			String errorBody = response.errorBody().string();
    			if(response.code() == 400) {
    				throw new CommonException(ErrorCode.PARAMETER_MALFORMED, errorBody);
    			} else if(response.code() == 404) {
    				throw new CommonException(ErrorCode.TARGET_DATA_NOT_EXISTS, errorBody);
    			} else {
    				throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, errorBody);
    			}
            }
        } catch (CommonException e) {
			throw new CommonException(e.getCode(), e.getReason());
        } catch (Exception e) {
        	//e.printStackTrace();
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, e.getMessage());
        }
    }
}
