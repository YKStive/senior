package com.youloft.net.raw;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;

/**
 * LiveDataCallAdapter
 *
 * @param <R>
 */
public class RawCallAdapter<R> implements CallAdapter<R, R> {

    private Type responseType;

    public RawCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public R adapt(Call<R> call) {
        try {
            Response<R> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            throw new RetrofitError(response);
        } catch (Throwable e) {
            throw new RetrofitError(e);
        }
    }


}
