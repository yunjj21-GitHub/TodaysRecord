package com.yunjung.todaysrecord.join_membership

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.User

class JoinMembershipViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()

    val user : LiveData<User>
        get() = _user

    fun updateUser(email : String, profileImage : String){
        _user.value?.email = email
        _user.value?.profileImage = profileImage
    }
}