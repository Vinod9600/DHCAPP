package com.dhc.app.data;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Vinod Durge on  04 Feb 2021
 */
public interface DownloadService {
    @GET("api/DownloadReportPDF")
    Call<ResponseBody> getDownloadReport(@Query("ObjType") String objType, @Query("DocEntry") String docEntry, @Query("DocType") String docType);

    @GET
    Call<ResponseBody>  getDownloadReport(@Url String fileUrl);
}
