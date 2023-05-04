package unist.pjs.the.repository

import retrofit2.http.*
import unist.pjs.the.data.Confirm
import unist.pjs.the.data.UserInfo

interface LoginService {
    @GET("/login/check")
    suspend fun getCheck(
        @Query("name") name: String,
        @Query("phone") phone: String
    ): Confirm

    @GET("/login/sendauth")
    suspend fun getSendAuth(
        @Query("name") name: String,
        @Query("phone") phone: String
    ): String

    @FormUrlEncoded
    @POST("/login/confirm")
    suspend fun postConfirm(
        @Field("name") name: String,
        @Field("auth_no") auth_no: String
    ): UserInfo

    @GET("/login/checkname")
    suspend fun checkName(
        @Query("name") name: String
    ): Confirm

    @FormUrlEncoded
    @POST("/user/token")
    suspend fun postToken(
        @Field("id") id: String,
        @Field("token") token: String
    ): Confirm

    companion object {
        fun create(): LoginService = provideRetrofit().create(LoginService::class.java)
    }
}