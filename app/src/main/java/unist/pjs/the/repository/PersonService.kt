package unist.pjs.the.repository

import retrofit2.http.GET
import retrofit2.http.Query
import unist.pjs.the.data.Peoples
import unist.pjs.the.data.UserInfo

interface PersonService {
    @GET("/person")
    suspend fun getPerson(
        @Query("grouping") grouping: String
    ): Peoples

    @GET("/user")
    suspend fun getUserInfo(
        @Query("name") name: String
    ): UserInfo

    companion object {
        fun create(): PersonService = provideRetrofit().create(PersonService::class.java)
    }
}