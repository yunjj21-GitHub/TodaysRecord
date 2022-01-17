package com.yunjung.todaysrecord.booth

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.PhotoBooth
import com.yunjung.todaysrecord.network.RetrofitManager
import retrofit2.Call
import retrofit2.Response

class BoothViewModel : ViewModel() {
    private val _adjBoothList = MutableLiveData<List<PhotoBooth>>()

    val adjBoothList: LiveData<List<PhotoBooth>>
        get() = _adjBoothList

    fun updateAdjBoothList(lng : String, lat : String) {
        val call : Call<List<PhotoBooth>>? = RetrofitManager.iRetrofit?.getPhotoboothByLocation(lng, lat)
        call?.enqueue(object : retrofit2.Callback<List<PhotoBooth>> {
            // 응답 성공시
            override fun onResponse(
                call: Call<List<PhotoBooth>>,
                response: Response<List<PhotoBooth>>
            ) {
                _adjBoothList.value = response.body() ?: listOf()
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<PhotoBooth>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }
}