package com.yunjung.todaysrecord.ui.photostudioprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                try{
                    RetrofitManager.service.getPhotoStudioByAreaAndType(area = userArea.value, type = "프로필사진")
                }
                catch (e : Throwable){
                    listOf()
                }

            }
            _photoStudioList.value = response
        }
    }
}