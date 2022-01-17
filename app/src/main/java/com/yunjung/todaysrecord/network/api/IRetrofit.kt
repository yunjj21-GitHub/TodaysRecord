package com.yunjung.todaysrecord.network.api

import com.yunjung.todaysrecord.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {
    // Create (POST)
    @FormUrlEncoded
    @POST("/postReview")
    fun postReview(
        @Field("psId") psId : String? = null,
        @Field("userId") userId : String? = null,
        @Field("rating") rating : Int? = null,
        @Field("content") content : String? = null,
        @Field("image") image : String? = null
    ) : Call<Review>

    @FormUrlEncoded
    @POST("/postUser")
    fun postUser(
        @Field("email") email : String? = null,
        @Field("profileImage") profileImage : String? = null,
        @Field("nickname") nickname : String? = null
    ) : Call<User>

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

    @GET("/checkUserIdInPhotostudioInterested")
    fun checkUserIdInPhotostudioInterested(
        @Query("_id") _id : String? = null,
        @Query("userId") userId : String? = null
    ) : Call<Boolean>

    @GET("/getImageReviewByPsId")
    fun getImageReviewByPsId(
        @Query("psId") psId : String? = null
    ) : Call<List<Review>>

    @GET("/getPhotoboothByLocation")
    fun getPhotoboothByLocation(
        @Query("userLongitude") userLongitude : String? = null,
        @Query("userLatitude") userLatitude : String? = null
    ) : Call<List<PhotoBooth>>

    @GET("/getPhotostudioListByUserId")
    fun getPhotostudioListByUserId(
        @Query("userId") userId : String? = null
    ) : Call<List<PhotoStudio>>

    @GET("/checkIfEmailAlreadySingedUp")
    fun checkIfEmailAlreadySingedUp(
        @Query("email") email : String
    ) : Call<User>

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
    @PATCH("/patchUserNicknameById")
    fun patchUserNicknameById(
        @Query("_id") _id : String? = null,
        @Query("nickname") nickname : String? = null
    ) : Call<User>

    @PATCH("/patchUserProfileImageById")
    fun patchUserProfileImageById(
        @Query("_id") _id : String? = null,
        @Query("profileImage") profileImage : String? = null
    ) : Call<User>

    // 유저의 interests에 특정 photostudio _id를 추가하는 API
    @PATCH("/addUserIdInPhotostudioInterested")
    fun addUserIdInPhotostudioInterested(
        @Query("_id") _id : String? = null,
        @Query("userId") userId : String? = null
    ) : Call<PhotoStudio>

    // 유저의 interests에 특정 photostudio _id를 빼는 API
    @PATCH("/pullUserIdInPhotostudioInterested")
    fun pullUserIdInPhotostudioInterested(
        @Query("_id") _id : String? = null,
        @Query("userId") userId : String? = null
    ) : Call<PhotoStudio>

    // Delete (DELETE)
    @DELETE("/deleteReviewById")
    fun deleteReviewById(
        @Query("_id") _id : String? = null
    ) : Call<Review>
}