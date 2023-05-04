package unist.pjs.the.repository

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import unist.pjs.the.data.UserInfo

interface LikeService {
    @FormUrlEncoded
    @POST("/user/sendheart")
    suspend fun postSendHeart(
        @Field("name") name: String,
        @Field("partner") partner: String
    ): UserInfo

    @FormUrlEncoded
    @POST("/user/sendlike")
    suspend fun postSendLike(
        @Field("name") name: String,
        @Field("partner") partner: String
    ): UserInfo

    companion object {
        fun create(): LikeService = provideRetrofit().create(LikeService::class.java)
    }
}