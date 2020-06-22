package com.youloft.net.raw;

import retrofit2.Response;

public class RetrofitError extends RuntimeException {

    private Response response;

    public <R> RetrofitError(Response<R> response) {
        this.response = response;
    }

    @Override
    public String getMessage() {
        return "网络错误";
    }

    public RetrofitError(Throwable e) {
        super(e.getMessage(), e);
    }

    public Response getResponse() {
        return response;
    }
}
