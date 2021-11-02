package com.yunjung.todaysrecord.network

import com.yunjung.todaysrecord.network.api.IRetrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
    // retrofit 객체 초기화
    private val retrofit = Retrofit.Builder()
    .baseUrl("http://192.168.219.166:3000")  // baseUrl 설정
    .addConverterFactory(GsonConverterFactory.create()) // Gson converter factory를 만들어 반환
    .build()

    // retrofit 인터페이스 가져오기
    val iRetrofit : IRetrofit = retrofit.create(IRetrofit::class.java)
}