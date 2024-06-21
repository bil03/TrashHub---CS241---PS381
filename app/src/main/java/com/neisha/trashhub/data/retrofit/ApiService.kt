package com.neisha.trashhub.data.retrofit

import com.neisha.trashhub.data.pref.response.*
import com.neisha.trashhub.view.response.CreatePickupResponse
import com.neisha.trashhub.view.response.DeletePickupResponse
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("users/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String,
        @Field("phone") phone: String,
        @Field("mitra") mitra: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("users/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("users/refresh-token/{id}")
    suspend fun refreshToken(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): RefreshTokenResponse

    @GET("articles")
    suspend fun getArticles(
    ): ArticlesResponse

    @GET("articles/detail/{id}")
    suspend fun getArticleDetail(
        @Path("id") id: String
    ): DetailArticleResponse

    @GET("articles/search/{title}")
    suspend fun searchArticles(
        @Path("title") title: String
    ): SearchArticleResponse

    @GET("articles/news/{count}")
    suspend fun getNewestArticles(
        @Path("count") count: Int
    ): NewestArticleResponse

    @POST("/predict/create")
    suspend fun createPredictionNoImage(): ErrorUploadResponse

    @POST("/predict/create")
    suspend fun createPredictionMoreThan1MB(): ErrorUploadResponse

    @POST("/predict/create")
    suspend fun createPredictionBadRequest(): ErrorUploadResponse

    @POST("/predict/create")
    @Multipart
    suspend fun createPrediction(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): PredictionResponse

    @GET("pickup/{id}")
    suspend fun getPickup(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): CreatePickupResponse

    @Multipart
    @POST("pickup")
    suspend fun getUpload(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latLng: RequestBody,
        @Part("lon") long: RequestBody
    ): DeletePickupResponse

    @GET("pickup")
    suspend fun getAddressWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location : Int = 1,
    ): CreatePickupResponse

}


