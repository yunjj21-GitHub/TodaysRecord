package com.yunjung.todaysrecord.ui.writereivew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.User

class WriteReviewViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    private val _psId = MutableLiveData<String>()
    private val _reviewImage = MutableLiveData<String>()

    val user : LiveData<User>
        get() = _user

    val psId : LiveData<String>
        get() = _psId

    val reviewImage : LiveData<String>
        get() = _reviewImage

    fun updateUser(user : User) {
        _user.value = user
    }

    fun updatePsId(id : String){
        _psId.value = id
    }

    fun updateReviewImage(image : String){
        _reviewImage.value = image
    }
}