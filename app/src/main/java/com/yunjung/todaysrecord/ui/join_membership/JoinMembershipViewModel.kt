package com.yunjung.todaysrecord.ui.join_membership

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.User

class JoinMembershipViewModel : ViewModel() {
    private val _userId = MutableLiveData<String?>()

    private val _userProfileImg = MutableLiveData<String?>()

    private val _userIdValid = MutableLiveData<Boolean>(false)

    private val _userPwdValid = MutableLiveData<Boolean>(false)

    val userId : LiveData<String?>
        get() = _userId

    val userProfileImg : LiveData<String?>
        get() = _userProfileImg

    val userIdValid : LiveData<Boolean>
        get() = _userIdValid

    val userPwdValid : LiveData<Boolean>
        get() = _userPwdValid

    fun updateUserId(userId : String?){
        if(userId == null) return
        else _userId.value = userId
    }

    fun updateUserProfileImg(userProfileImg : String?){
        if(userProfileImg == null) return
        else _userProfileImg.value = userProfileImg
    }

    fun updateUserIdValid(result : Boolean){
        _userIdValid.value = result
    }

    fun updateUserPwdValid(result : Boolean){
        _userPwdValid.value = result
    }
}