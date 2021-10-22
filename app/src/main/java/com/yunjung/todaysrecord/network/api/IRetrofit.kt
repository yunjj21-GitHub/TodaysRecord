package com.yunjung.todaysrecord.network.api

import com.yunjung.todaysrecord.models.ApiTest
import retrofit2.Call
import retrofit2.http.GET

// Retrofit Api 인터페이스 (어노테이션과 파라미터만 지정해 주면 Retrofit이 자동으로 구현)
interface IRetrofit {
    // Creat (Post)

    // Read (Get)
    @GET("/api/") // baseUrl + "/api/"
    // 접근하는 주소에 따라, Path 또는 Query 지정 가능
    fun readChannel() : Call<ApiTest> // ApiTest로 받게된다.

    // Update (전체를 수정하는 Put, 부분을 수정하는 Patch)

    // Delete (Delete)
}