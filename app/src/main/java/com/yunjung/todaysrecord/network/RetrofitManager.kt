package com.yunjung.todaysrecord.network

import android.content.ContentValues.TAG
import android.util.Log
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.network.api.IRetrofit
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {

    // retrofit 객체 초기화
    private val retrofit = Retrofit.Builder()
    .baseUrl("http://localhost:3000")  // baseUrl 설정
    .addConverterFactory(GsonConverterFactory.create()) // Gson converter factory를 만들어 반환
    .build()

    // retrofit 인터페이스 가져오기
    private val iRetrofit : IRetrofit? = retrofit.create(IRetrofit::class.java)

    // retrofit 인터페이스의 특정 메서드 이용 (호출은 필요한 activity에서!)
    fun getInformation(){
        val call : Call<PhotoStudio>? = iRetrofit?.getPhotoStudioByAreaAndType(area = "망원동", type = "증명사진").let{
            it
        }?:return

        call?.enqueue(object : retrofit2.Callback<PhotoStudio>{
            // 응답 성공시
            override fun onResponse(call: Call<PhotoStudio>, response: Response<PhotoStudio>) {
                Log.e(TAG, "onResponse ${response.body().toString()}")
            }

            // 응답 실패시
            override fun onFailure(call: Call<PhotoStudio>, t: Throwable) {
                Log.e(TAG, "onFailure")
            }
        })
    }
}