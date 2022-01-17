package com.yunjung.todaysrecord.myinterests

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import retrofit2.Call
import retrofit2.Response

class MyinterestsViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    private val _interestsNum = MutableLiveData(0) // 유저가 찜한 사진관 개수
    private val _interestsList = MutableLiveData<List<PhotoStudio>>() // 유저가 찜한 사진관 리스트

    val user : LiveData<User>
        get() = _user

    val interestsNum: LiveData<Int>
        get() = _interestsNum

    val interestsList: LiveData<List<PhotoStudio>>
        get() = _interestsList

    fun updateUser(user : User) {
        _user.value = user
    }

    fun updateInterestsList() {
        val call : Call<List<PhotoStudio>> = RetrofitManager.iRetrofit?.
        getPhotostudioListByUserId(userId = user.value!!._id)
        call?.enqueue(object : retrofit2.Callback<List<PhotoStudio>>{
            // 응답 성공시
            override fun onResponse(
                call: Call<List<PhotoStudio>>,
                response: Response<List<PhotoStudio>>
            ) {
                _interestsList.value = response.body() ?: listOf()
                _interestsNum.value = (response.body() ?: listOf()).size
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<PhotoStudio>>, t: Throwable) {
                Log.e(TAG, t.localizedMessage)
            }
        })
    }

    fun updateInterestsNum(){
        _interestsNum.value = 5
    }
}