package com.yunjung.todaysrecord.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.User

class EditprofileViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()

    val user : LiveData<User>
        get() = _user

    fun updateUser(user : User){
        _user.value = user
    }

    fun updateUserProfileImg(newProfileImg : String){
        _user.value!!.profileImage = newProfileImg
    }
}