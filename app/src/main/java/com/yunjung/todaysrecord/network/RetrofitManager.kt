package com.yunjung.todaysrecord.network

import com.google.gson.GsonBuilder
import com.yunjung.todaysrecord.network.api.RetrofitService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
    val gson = GsonBuilder()
        .setLenient()
        .create()

    // Retrofit Client 인스턴스 생성
    private val retrofitClient = Retrofit.Builder()
    .baseUrl("http://192.168.0.11:80")
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

    // Retrofit Client 인스턴스로 Service 구현체 생성
    val service : RetrofitService = retrofitClient.create(RetrofitService::class.java)
}