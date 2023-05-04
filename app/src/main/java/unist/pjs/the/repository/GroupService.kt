package unist.pjs.the.repository

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import unist.pjs.the.data.Confirm
import unist.pjs.the.data.Groups
import unist.pjs.the.data.UserInfo

interface GroupService {
    @GET("/person/group")
    suspend fun getGroupList(
    ): Groups

    @FormUrlEncoded
    @POST("/login/register")
    suspend fun postRegister(
        @Field("name") name: String,
        @Field("birth") birth: String,
        @Field("gender") gender: String,
        @Field("phone") phone: String,
        @Field("group") group: String,
        @Field("auth_no") auth_no: String
    ): UserInfo

    @FormUrlEncoded
    @POST("/user/token")
    suspend fun postToken(
        @Field("id") id: String,
        @Field("token") token: String
    ): Confirm

    companion object {
        fun create(): GroupService = provideRetrofit().create(GroupService::class.java)
    }
}