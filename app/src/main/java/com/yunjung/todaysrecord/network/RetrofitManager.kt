package com.yunjung.todaysrecord.network

import com.yunjung.todaysrecord.network.api.RetrofitService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
    // Retrofit Client 인스턴스 생성
    private val retrofitClient = Retrofit.Builder()
    .baseUrl("http://13.209.25.227:80")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    // Retrofit Client 인스턴스로 Service 구현체 생성
    val service : RetrofitService = retrofitClient.create(RetrofitService::class.java)
}