package com.yunjung.todaysrecord.ui.information

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.PhotoStudio

class InformationViewModel : ViewModel() {
    private val _photoStudio = MutableLiveData<PhotoStudio>()

    val photoStudio: LiveData<PhotoStudio>
        get() = _photoStudio

    fun updatePhotoStudio(photoStudio: PhotoStudio) {
        _photoStudio.value = photoStudio
    }
}