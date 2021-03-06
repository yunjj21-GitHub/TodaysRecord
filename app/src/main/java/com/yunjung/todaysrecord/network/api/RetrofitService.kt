package com.yunjung.todaysrecord.network.api

import com.yunjung.todaysrecord.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import javax.annotation.PostConstruct

interface RetrofitService {
    // Create (POST)
    @Multipart
    @POST("/reviewImageUpload")
    suspend fun reviewImageUpload(
        @Part reviewImage : MultipartBody.Part
    ) : String

    @Multipart
    @POST("/profileImageUpload")
    suspend fun profileImageUpload(
        @Part profileImage : MultipartBody.Part
    ) : String

    @FormUrlEncoded
    @POST("/postReview")
    suspend fun postReview(
        @Field("psId") psId : String? = null,
        @Field("userId") userId : String? = null,
        @Field("rating") rating : Int? = null,
        @Field("content") content : String? = null,
        @Field("image") image : String? = null
    ) : Review

    @FormUrlEncoded
    @POST("/postUser")
    suspend fun postUser(
        @Field("email") email : String? = null,
        @Field("profileImage") profileImage : String? = null,
        @Field("nickname") nickname : String? = null,
        @Field("pwd") pwd : String? = null
    ) : User

    @FormUrlEncoded
    @POST("/postReviewReport")
    suspend fun postReviewReport(
        @Field("reviewId") reviewId : String,
        @Field("accuser") accuser : String,
        @Field("reportType") reportType : String,
    ) : reviewReport

    // Read (GET)
    @GET("/getReviewByPsId")
    suspend fun getReviewByPsId(
        @Query("psId") psId : String? = null
    ) : List<Review>

    @GET("/getUserById")
    suspend fun getUserById(
        @Query("_id") _id : String? = null
    ) : User

    @GET("/getPhotostudioById")
    suspend fun getPhotostudioById(
        @Query("_id") _id : String? = null
    ) : PhotoStudio

    @GET("/getReviewByUserId")
    suspend fun getReviewByUserId(
        @Query("userId") userId : String? = null
    ) : List<Review>

    @GET("/getAreaLarge")
    suspend fun getAreaLarge(
    ) : List<AreaLarge>

    @GET("/getAreaMediumByBelong")
    suspend fun getAreaMediumByBelong(
        @Query("belong") belong : String? = null
    ) : List<AreaMedium>

    @GET("/getAreaSmallByBelong")
    suspend fun getAreaSmallByBelong(
        @Query("belong") belong : String? = null
    ) : List<AreaSmall>

    @GET("/checkUserIdInPhotostudioInterested")
    suspend fun checkUserIdInPhotostudioInterested(
        @Query("_id") _id : String? = null,
        @Query("userId") userId : String? = null
    ) : Boolean

    @GET("/getImageReviewByPsId")
    suspend fun getImageReviewByPsId(
        @Query("psId") psId : String? = null
    ) : List<Review>

    @GET("/getPhotoboothByLocation")
    suspend fun getPhotoboothByLocation(
        @Query("userLongitude") userLongitude : String? = null,
        @Query("userLatitude") userLatitude : String? = null
    ) : List<PhotoBooth>

    @GET("/getPhotostudioListByUserId")
    suspend fun getPhotostudioListByUserId(
        @Query("userId") userId : String? = null
    ) : List<PhotoStudio>

    @GET("/checkIfEmailAlreadySingedUp")
    suspend fun checkIfEmailAlreadySingedUp(
        @Query("email") email : String
    ) : User

    @GET("/emailLogin")
    suspend fun emailLogin(
        @Query("id") id : String,
        @Query("pwd") pwd : String
    ) : User

    @GET("/getPSListBySearchWord")
    suspend fun getPSListBySearchWord(
        @Query("searchWord") searchWord : String
    ) : List<PhotoStudio>

    // ?????????
    @GET("/getPhotoStudioByAreaAndType")
    suspend fun getPhotoStudioByAreaAndType(
        @Query("area") area : String? = null,
        @Query("type") type : String? = null
    ) : List<PhotoStudio>

    // ?????????
    @GET("/getPsListOfUserAreaInPopularityOrder")
    suspend fun getPsListOfUserAreaInPopularityOrder(
        @Query("area") area : String? = null,
        @Query("type") type : String? = null
    ) : List<PhotoStudio>

    // ?????????
    @GET("/getPsListOfUserAreaInCostOrder")
    suspend fun getPsListOfUserAreaInCostOrder(
        @Query("area") area : String? = null,
        @Query("type") type : String? = null
    ) : List<PhotoStudio>


    // Update
    // ????????? ???????????? PUT
    @PUT("/putReviewById")
    suspend fun putReviewById(
        @Query("_id") _id : String? = null,
        @Query("rating") rating : Int? = null,
        @Query("content") content : String? = null,
        @Query("image") image : String? = null
    ) : Review

    // ????????? ???????????? PATCH
    @PATCH("/patchUserById")
    suspend fun patchUserById(
        @Query("_id") _id : String? = null,
        @Query("profileImg") profileImg : String? = null,
        @Query("nickname") nickname : String? = null
    ) : User

    // ????????? interests??? ?????? photostudio _id??? ???????????? API
    @PATCH("/addUserIdInPhotostudioInterested")
    suspend fun addUserIdInPhotostudioInterested(
        @Query("_id") _id : String? = null,
        @Query("userId") userId : String? = null
    ) : PhotoStudio

    // ????????? interests??? ?????? photostudio _id??? ?????? API
    @PATCH("/pullUserIdInPhotostudioInterested")
    suspend fun pullUserIdInPhotostudioInterested(
        @Query("_id") _id : String? = null,
        @Query("userId") userId : String? = null
    ) : PhotoStudio

    // Delete (DELETE)
    @DELETE("/deleteReviewById")
    suspend fun deleteReviewById(
        @Query("_id") _id : String? = null
    ) : Review
}