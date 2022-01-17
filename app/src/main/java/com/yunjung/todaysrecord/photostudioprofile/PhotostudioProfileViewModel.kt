package com.yunjung.todaysrecord.photostudioprofile

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.recyclerview.PhotoStudioAdapter
import retrofit2.Call
import retrofit2.Response

class PhotostudioProfileViewModel : ViewModel() {
    private val _userArea = MutableLiveData<String>()
    private val _photoStudioList = MutableLiveData<List<PhotoStudio>>()

    val userArea : LiveData<String>
        get() = _userArea

    val photoStudioList : LiveData<List<PhotoStudio>>
        get() = _photoStudioList

    fun updateUserArea(area : String){
        _userArea.value = area
    }

    fun updatePhotoStudioList(){
        // 서버에서 필요한 사진관 리스트 가져오기
        val call : Call<List<PhotoStudio>>? = RetrofitManager.iRetrofit?.getPhotoStudioByAreaAndType(area = userArea.value, type = "프로필사진")
        call?.enqueue(object : retrofit2.Callback<List<PhotoStudio>>{
            // 응답 성공시
            override fun onResponse(
                call: Call<List<PhotoStudio>>,
                response: Response<List<PhotoStudio>>
            ) {
                _photoStudioList.value = response.body() ?: listOf()
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<PhotoStudio>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }
}