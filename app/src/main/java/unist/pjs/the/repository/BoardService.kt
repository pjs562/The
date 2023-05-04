package unist.pjs.the.repository

import retrofit2.http.*
import unist.pjs.the.data.*

interface BoardService {
    @GET("/board/notice")
    suspend fun getNotice(
        @Query("lastId") lastId: String,
        @Query("limit") limit: Int = 20,
    ): BoardList

    @FormUrlEncoded
    @POST("/board/notice")
    suspend fun postNotice(
        @Field("name") name: String,
        @Field("title") title: String,
        @Field("content") content: String
    ): Bulletin

    @FormUrlEncoded
    @POST("/board/notice/like")
    suspend fun postNoticeLike(
        @Field("name") name: String,
        @Field("boardId") boardId: String
    ): Bulletin

    @FormUrlEncoded
    @PUT("/board/notice")
    suspend fun putNotice(
        @Field("name") name: String,
        @Field("boardId") boardId: String,
        @Field("title") title: String,
        @Field("content") content: String
    ): Bulletin

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/board/notice", hasBody = true)
    suspend fun deleteNotice(
        @Field("name") name: String,
        @Field("boardId") boardId: String
    ): Confirm

    @FormUrlEncoded
    @POST("/board/notice/reply")
    suspend fun postNoticeReply(
        @Field("name") name: String,
        @Field("boardId") boardId: String,
        @Field("content") content: String,
    ): Bulletin

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/board/notice/reply", hasBody = true)
    suspend fun deleteNoticeReply(
        @Field("name") name: String,
        @Field("boardId") boardId: String,
        @Field("replyId") replyId: String
    ): Bulletin

    @GET("/board/freeboard")
    suspend fun getFree(
        @Query("lastId") lastId: String,
        @Query("limit") limit: Int = 20,
    ): BoardList

    @FormUrlEncoded
    @POST("/board/freeboard")
    suspend fun postFree(
        @Field("name") name: String,
        @Field("title") title: String,
        @Field("content") content: String
    ): Bulletin

    @FormUrlEncoded
    @PUT("/board/freeboard")
    suspend fun putFree(
        @Field("name") name: String,
        @Field("boardId") boardId: String,
        @Field("title") title: String,
        @Field("content") content: String
    ): Bulletin

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/board/freeboard", hasBody = true)
    suspend fun deleteFree(
        @Field("name") name: String,
        @Field("boardId") boardId: String
    ): Confirm

    @FormUrlEncoded
    @POST("/board/freeboard/reply")
    suspend fun postFreeReply(
        @Field("name") name: String,
        @Field("boardId") boardId: String,
        @Field("content") content: String,
    ): Bulletin

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/board/freeboard/reply/", hasBody = true)
    suspend fun deleteFreeReply(
        @Field("name") name: String,
        @Field("boardId") boardId: String,
        @Field("replyId") replyId: String
    ): Bulletin

    @FormUrlEncoded
    @POST("/board/freeboard/like")
    suspend fun postFreeLike(
        @Field("name") name: String,
        @Field("boardId") boardId: String
    ): Bulletin

    @GET("/board/intercession")
    suspend fun getPray(
        @Query("lastId") lastId: String,
        @Query("limit") limit: Int = 20,
    ): BoardList

    @FormUrlEncoded
    @POST("/board/intercession")
    suspend fun postPray(
        @Field("name") name: String,
        @Field("title") title: String,
        @Field("content") content: String
    ): Bulletin

    @FormUrlEncoded
    @PUT("/board/intercession")
    suspend fun putPray(
        @Field("name") name: String,
        @Field("boardId") boardId: String,
        @Field("title") title: String,
        @Field("content") content: String
    ): Bulletin

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/board/intercession", hasBody = true)
    suspend fun deletePray(
        @Field("name") name: String,
        @Field("boardId") boardId: String
    ): Confirm

    @FormUrlEncoded
    @POST("/board/intercession/reply")
    suspend fun postPrayReply(
        @Field("name") name: String,
        @Field("boardId") boardId: String,
        @Field("content") content: String,
    ): Bulletin

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/board/intercession/reply/", hasBody = true)
    suspend fun deletePrayReplay(
        @Field("name") name: String,
        @Field("boardId") boardId: String,
        @Field("replyId") replyId: String
    ): Bulletin

    @FormUrlEncoded
    @POST("/board/intercession/like")
    suspend fun postPrayLike(
        @Field("name") name: String,
        @Field("boardId") boardId: String
    ): Bulletin

    @GET("/board/quitetime")
    suspend fun getQuitetime(
        @Query("lastId") lastId: String,
        @Query("limit") limit: Int = 20,
    ): BoardList

    @FormUrlEncoded
    @POST("/board/quitetime")
    suspend fun postQuitetime(
        @Field("name") name: String,
        @Field("title") title: String,
        @Field("content") content: String
    ): Bulletin

    @FormUrlEncoded
    @PUT("/board/quitetime")
    suspend fun putQuitetime(
        @Field("name") name: String,
        @Field("boardId") boardId: String,
        @Field("title") title: String,
        @Field("content") content: String
    ): Bulletin

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/board/quitetime", hasBody = true)
    suspend fun deleteQuitetime(
        @Field("name") name: String,
        @Field("boardId") boardId: String
    ): Confirm

    @FormUrlEncoded
    @POST("/board/quitetime/reply")
    suspend fun postQuitetimeReply(
        @Field("name") name: String,
        @Field("boardId") boardId: String,
        @Field("content") content: String,
    ): Bulletin

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/board/quitetime/reply/", hasBody = true)
    suspend fun deleteQuitetimeReplay(
        @Field("name") name: String,
        @Field("boardId") boardId: String,
        @Field("replyId") replyId: String
    ): Bulletin

    @FormUrlEncoded
    @POST("/board/quitetime/like")
    suspend fun postQuitetimeLike(
        @Field("name") name: String,
        @Field("boardId") boardId: String
    ): Bulletin

    @GET("/board/balance")
    suspend fun getBalance(
        @Query("lastId") lastId: String,
        @Query("limit") limit: Int = 20,
    ): BalanceList

    @FormUrlEncoded
    @POST("/board/balance")
    suspend fun postBalance(
        @Field("name") name: String,
        @Field("left") left: String,
        @Field("right") right: String
    ): Balance

    @FormUrlEncoded
    @PUT("/board/balance")
    suspend fun putBalance(
        @Field("name") name: String,
        @Field("balanceId") _id: String,
        @Field("left") title: String,
        @Field("right") content: String
    ): Balance

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/board/balance/reply", hasBody = true)
    suspend fun deleteBalanceReply(
        @Field("name") name: String,
        @Field("balanceId") _id: String,
        @Field("replyId") replyId: String
    ): Balance

    @FormUrlEncoded
    @POST("/board/balance/like")
    suspend fun postBalanceLike(
        @Field("name") name: String,
        @Field("balanceId") balanceId: String,
        @Field("isLeft") isLeft: Boolean
    ): Balance

    @FormUrlEncoded
    @POST("/board/balance/reply")
    suspend fun postBalanceReply(
        @Field("name") name: String,
        @Field("balanceId") boardId: String,
        @Field("content") content: String,
    ): Balance

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/board/balance", hasBody = true)
    suspend fun deleteBalance(
        @Field("name") name: String,
        @Field("balanceId") balanceId: String
    ): Confirm

    @FormUrlEncoded
    @POST("/board/report")
    suspend fun postBoardReport(
        @Field("name") name: String,
        @Field("itemId") boardId: String,
        @Field("content") content: String,
        @Field("type") type: String
    ): Confirm

    @FormUrlEncoded
    @POST("/board/poll")
    suspend fun postPoll(
        @Field("id") id: String,
        @Field("content") content: String,
        @Field("type") type: String
    ): Confirm

    companion object {
        fun create(): BoardService = provideRetrofit().create(BoardService::class.java)
    }
}