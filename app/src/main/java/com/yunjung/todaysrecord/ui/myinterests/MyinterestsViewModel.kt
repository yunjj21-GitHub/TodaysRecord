package com.yunjung.todaysrecord.ui.myinterests

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
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                try{
                    RetrofitManager.service.getPhotostudioListByUserId(userId = user.value!!._id)
                }catch (e : Throwable){
                    listOf()
                }
            }
            _interestsList.value = response
            _interestsNum.value = response.size
        }
    }
}