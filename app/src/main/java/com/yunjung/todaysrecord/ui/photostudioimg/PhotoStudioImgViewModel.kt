package com.yunjung.todaysrecord.ui.photostudioimg

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoStudioImgViewModel : ViewModel() {
    val _photoStudioImg = MutableLiveData<String>()

    val photoStudioImg : LiveData<String>
        get() = _photoStudioImg

    fun updatePhotoSudioImg(img : String){
        _photoStudioImg.value = img
    }
}