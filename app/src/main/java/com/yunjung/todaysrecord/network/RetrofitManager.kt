package com.yunjung.todaysrecord.network

import com.yunjung.todaysrecord.network.api.IRetrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
    // retrofit 객체 초기화
    private val retrofit = Retrofit.Builder()
    .baseUrl("http://13.209.25.227:80")  // baseUrl 설정
    .addConverterFactory(GsonConverterFactory.create()) // Gson converter factory를 만들어 반환
    .build()

    // retrofit 인터페이스 가져오기
    val iRetrofit : IRetrofit = retrofit.create(IRetrofit::class.java)
}