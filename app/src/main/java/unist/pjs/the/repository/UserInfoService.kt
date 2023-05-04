package unist.pjs.the.repository

import retrofit2.http.GET
import retrofit2.http.Query
import unist.pjs.the.data.UserInfo

interface UserInfoService {
    @GET("/user")
    suspend fun getUserInfo(
        @Query("name") name: String
    ): UserInfo

    companion object {
        fun create(): UserInfoService = provideRetrofit().create(UserInfoService::class.java)
    }
}