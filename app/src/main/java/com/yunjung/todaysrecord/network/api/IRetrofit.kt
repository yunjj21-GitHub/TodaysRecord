package com.yunjung.todaysrecord.network.api

import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.models.User
import retrofit2.Call
import retrofit2.http.*

// Retrofit Api 인터페이스 (어노테이션과 파라미터만 지정해 주면 Retrofit이 자동으로 구현)
interface IRetrofit {
    /*
    * 접근하는 주소에 따라, Path 또는 Query 지정 가능
    * @HTTP method("uri") // baseUrl + uri
    * fun 메소드명(@Query("쿼리 명") 매개변수 명 : 매개변수 타입, ... ) : Call<받아올 data model>
    */

    // Creat (POST)
    @POST("/postReview")
    fun postReview(
        @Query("psId") psId : String? = null,
        @Query("userId") userId : String? = null,
        @Query("rating") rating : Int? = null,
        @Query("content") content : String? = null,
        @Query("image") image : String? = null,
    ) : Call<Review>

    // Read (GET)
    @GET("/getPhotoStudioByAreaAndType")
    fun getPhotoStudioByAreaAndType(
        @Query("area") area : String? = null,
        @Query("type") type : String? = null
    ) : Call<PhotoStudio>

    @GET("/getReviewByPsId")
    fun getReviewByPsId(
        @Query("psId") psId : String? = null
    ) : Call<Review>

    @GET("/getUserById")
    fun getUserById(
        @Query("_id") _id : String? = null
    ) : Call<User>

    @GET("/getPhotostudioById")
    fun getPhotostudioById(
        @Query("_id") _id : String? = null
    ) : Call<PhotoStudio>

    @GET("/getReviewByUserId")
    fun getReviewByUserId(
        @Query("userId") userId : String? = null
    ) : Call<Review>

    // Update
    // 전체를 수정하는 PUT
    @PUT("/putReviewById")
    fun putReviewById(
        @Query("_id") _id : String? = null,
        @Query("rating") rating : Int? = null,
        @Query("content") content : String? = null,
        @Query("image") image : String? = null
    ) : Call<Review>

    // 부분을 수정하는 PATCH
    @PATCH("/patchUserNickNameById")
    fun patchUserNickNameById(
        @Query("_id") _id : String? = null,
        @Query("nickName") nickName : String? = null
    ) : Call<User>

    @PATCH("/patchUserProfileImageById")
    fun patchUserProfileImageById(
        @Query("_id") _id : String? = null,
        @Query("profileImage") profileImage : String? = null
    ) : Call<User>

    // Delete (DELETE)
    @DELETE("/deleteReviewById")
    fun deleteReviewById(
        @Query("_id") _id : String? = null
    ) : Call<Review>
}