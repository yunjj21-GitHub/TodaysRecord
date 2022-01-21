package com.yunjung.todaysrecord.photostudioimg

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoStudioImgViewModel : ViewModel() {
    val _photoStudioImg = MutableLiveData<String>()

    val photoStudioImg : LiveData<String>
        get() = _photoStudioImg

    fun updatePhotoSudioImg(img : String){
        _photoStudioImg.value = img
        Log.e(ContentValues.TAG, _photoStudioImg.value.toString() + " viewModel")
    }
}