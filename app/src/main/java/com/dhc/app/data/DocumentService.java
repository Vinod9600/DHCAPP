package com.dhc.app.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Vinod Durge on  16 Nov 2020
 */
public interface DocumentService {

    @GET("api/GetDocDetails")
    Call<BaseResponse<ArInvoice>> getDocumentDetails(@Query("ObjType") String objType, @Query("DocEntry") String docEntry);

    @GET("api/GetDocumentList/Get")
    Call<BaseResponse<ArInvoice>> getDocumentList(@Query("ObjType") String objType,
                                                  @Query("Cardcode") String cardCode,
                                                  @Query("FromDate") String fromDate,
                                                  @Query("ToDate") String toDate,
                                                  @Query("VendorRefNo") String vendorRefNo);

    @GET("api/GetDocumentList/Get")
    Call<BaseResponse<DocListItem>> getDocumentListWithDate(@Query("ObjType") String objType,
                                                  @Query("Cardcode") String cardCode,
                                                  @Query("FromDate") String fromDate,
                                                  @Query("ToDate") String toDate,
                                                  @Query("VendorRefNo") String vendorRefNo);

}
