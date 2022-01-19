package com.yunjung.todaysrecord.detail

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                RetrofitManager.service.checkUserIdInPhotostudioInterested(_id = photoStudio.value!!._id, userId = user.value!!._id)
            }
            _heartState.value = response
        }
    }
}
