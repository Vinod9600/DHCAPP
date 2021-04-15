package com.dhc.app.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CustomerService {

    @GET("api/GetCustomerType")
    Call<BaseResponse> getCustomerDetails(@Query("CardCode") String customerCode);
}
