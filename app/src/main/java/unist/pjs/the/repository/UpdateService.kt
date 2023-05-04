package unist.pjs.the.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import unist.pjs.the.data.UserInfo

interface UpdateService {
    @FormUrlEncoded
    @POST("/user/update")
    suspend fun postUpdateContent(
        @Field("name") name: String,
        @Field("bio") bio: String
    ): UserInfo

    @Multipart
    @POST("/user/upload")
    suspend fun postUploadImage(
        @Part name: MultipartBody.Part,
        @Part image: MultipartBody.Part
    ): UserInfo

    companion object {
        fun create(): UpdateService = provideRetrofit().create(UpdateService::class.java)
    }
}