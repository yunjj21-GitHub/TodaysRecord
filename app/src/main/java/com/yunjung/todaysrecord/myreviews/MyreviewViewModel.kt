package com.yunjung.todaysrecord.myreviews

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import retrofit2.Call
import retrofit2.Response

class MyreviewViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    private val _reviewNum = MutableLiveData<Int>(0) // 유저가 작성한 리뷰의 개수
    private val _reviewList = MutableLiveData<List<Review>>() // 유저가 작성한 리뷰 목록

    val user : LiveData<User>
        get() = _user

    val reviewNum: LiveData<Int>
        get() = _reviewNum // 클래스 내부에서 사용하는 변수를 get()으로 가져와 반환

    val reviewList: LiveData<List<Review>>
        get() = _reviewList

    fun updateUser(user : User){
        _user.value = user
    }

    fun updateReviewList() {
        val call : Call<List<Review>> = RetrofitManager.iRetrofit?.getReviewByUserId(userId = user.value!!._id)
        call?.enqueue(object : retrofit2.Callback<List<Review>>{
            // 응답 성공시
            override fun onResponse(
                call: Call<List<Review>>,
                response: Response<List<Review>>
            ) {
                _reviewList.value = response.body() ?: listOf()
                _reviewNum.value = (response.body() ?: listOf()).size
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }
}