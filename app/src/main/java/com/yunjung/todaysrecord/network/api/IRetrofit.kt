package com.yunjung.todaysrecord.network.api

import com.yunjung.todaysrecord.models.*
import retrofit2.Call
import retrofit2.http.*

// Retrofit Api 인터페이스 (어노테이션과 파라미터만 지정해 주면 Retrofit이 자동으로 구현)
interface IRetrofit {
    /*
    * 접근하는 주소에 따라, Path 또는 Query 지정 가능
    * @HTTP method("/uri") // baseUrl + uri
    * fun 메소드명(@Query("쿼리 명") 매개변수 명 : 매개변수 타입, ... ) : Call<받아올 data model>
    */

    // Creat (POST)
    @FormUrlEncoded
    @POST("/postReview")
    fun postReview(
        @Field("psId") psId : String? = null,
        @Field("userId") userId : String? = null,
        @Field("rating") rating : Int? = null,
        @Field("content") content : String? = null,
        @Field("image") image : String? = null,
    ) : Call<Review>

    // Read (GET)
    @GET("/getPhotoStudioByAreaAndType")
    fun getPhotoStudioByAreaAndType(
        @Query("area") area : String? = null,
        @Query("type") type : String? = null
    ) : Call<List<PhotoStudio>>

    @GET("/getReviewByPsId")
    fun getReviewByPsId(
        @Query("psId") psId : String? = null
    ) : Call<List<Review>>

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
    ) : Call<List<Review>>

    @GET("/getAreaLarge")
    fun getAreaLarge(
    ) : Call<List<AreaLarge>>

    @GET("/getAreaMediumByBelong")
    fun getAreaMediumByBelong(
        @Query("belong") belong : String? = null
    ) : Call<List<AreaMedium>>

    @GET("/getAreaSmallByBelong")
    fun getAreaSmallByBelong(
        @Query("belong") belong : String? = null
    ) : Call<List<AreaSmall>>

    @GET("/checkPhotostudioIdInUserInterests")
    fun checkPhotostudioIdInUserInterests(
        @Query("psId") psId : String? = null,
        @Query("userId") userId : String? = null
    ) : Call<Boolean>

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

    // 유저의 interests에 특정 photostudio _id를 추가하는 API
    @PATCH("addPhotostudioIdInUserInterests")
    fun addPhotostudioIdInUserInterests(
        @Query("psId") psId : String? = null,
        @Query("userId") userId : String? = null
    ) : Call<User>

    // 유저의 interests에 특정 photostudio _id를 빼는 API
    @PATCH("pullPhotostudioIdInUserInterests")
    fun pullPhotostudioIdInUserInterests(
        @Query("psId") psId : String? = null,
        @Query("userId") userId : String? = null
    ) : Call<User>

    // Delete (DELETE)
    @DELETE("/deleteReviewById")
    fun deleteReviewById(
        @Query("_id") _id : String? = null
    ) : Call<Review>
}