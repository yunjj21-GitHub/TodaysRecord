package com.yunjung.todaysrecord.detail

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import retrofit2.Call
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    private val _photoStudio = MutableLiveData<PhotoStudio>()
    private val _heartState = MutableLiveData<Boolean>(false)

    val user : LiveData<User>
        get() = _user

    val photoStudio: LiveData<PhotoStudio>
        get() = _photoStudio

    val heartState : LiveData<Boolean>
        get() = _heartState

    fun updateUser(user : User){
        _user.value = user
    }

    fun updatePhotoStudio(photoStudio: PhotoStudio) {
        _photoStudio.value = photoStudio
    }

    fun updateHeartState(){
        val call : Call<Boolean> = RetrofitManager.iRetrofit.checkUserIdInPhotostudioInterested(_id = photoStudio.value!!._id, userId = user.value!!._id)
        call.enqueue(object : retrofit2.Callback<Boolean>{
            // 응답 성공시
            override fun onResponse(
                call: Call<Boolean>,
                response: Response<Boolean>
            ) {
                _heartState.value = response.body()!!
            }

            // 응답 실패시
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }
}
