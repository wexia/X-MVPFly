package com.xia.fly.http.api

import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author xia
 * @date 2018/7/27.
 */
interface RestService {

    //==========================//
    //         POST请求          //
    // =========================//

    @POST
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: */*")
    @FormUrlEncoded
    fun post(@Url url: String, @FieldMap maps: MutableMap<String, String>,
             @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>

    @POST
    fun postBody(@Url url: String, @Body `object`: Any,
                 @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>

    @POST
    fun postBody(@Url url: String, @Body body: RequestBody,
                 @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>

    @POST
    @Headers("Content-Type: application/json; charset=utf-8", "Accept: application/json")
    fun postJson(@Url url: String, @Body jsonBody: RequestBody,
                 @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>


    //==========================//
    //         GET请求           //
    // =========================//

    @GET
    operator fun get(@Url url: String, @QueryMap params: MutableMap<String, String>,
                     @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>


    //==========================//
    //         DELETE请求        //
    // =========================//

    @DELETE
    fun delete(@Url url: String, @QueryMap maps: MutableMap<String, String>,
               @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>

    @DELETE
    fun deleteBody(@Url url: String, @Body `object`: Any,
                   @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>

    @DELETE
    fun deleteBody(@Url url: String, @Body body: RequestBody,
                   @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>

    @DELETE
    @Headers("Content-Type: application/json; charset=utf-8", "Accept: application/json")
    fun deleteJson(@Url url: String, @Body jsonBody: RequestBody,
                   @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>


    //==========================//
    //         PUT请求           //
    // =========================//

    @PUT
    fun put(@Url url: String, @QueryMap maps: MutableMap<String, String>,
            @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>

    @PUT
    fun putBody(@Url url: String, @Body `object`: Any,
                @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>

    @PUT
    fun putBody(@Url url: String, @Body body: RequestBody,
                @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>

    @PUT
    @Headers("Content-Type: application/json; charset=utf-8", "Accept: application/json")
    fun putJson(@Url url: String, @Body jsonBody: RequestBody,
                @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>


    //==========================//
    //       文件上传下载         //
    // =========================//

    @POST
    fun uploadFiles(@Url url: String, @Body body: RequestBody,
                    @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>

    @Streaming
    @GET
    fun downloadFile(@Url url: String,
                     @HeaderMap headers: MutableMap<String, String>): Observable<ResponseBody>
}
