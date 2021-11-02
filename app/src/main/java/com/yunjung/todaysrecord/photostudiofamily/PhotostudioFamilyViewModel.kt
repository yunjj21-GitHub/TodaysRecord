package com.yunjung.todaysrecord.photostudiofamily

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.PhotoStudio

class PhotostudioFamilyViewModel : ViewModel() {
    // 클래스 내부에서만 사용
    private val _photoStudioList = MutableLiveData<List<PhotoStudio>?>()

    // 클래스 외부에서 접근할 때 사용
    val photoStudioList : LiveData<List<PhotoStudio>?>
        get() = _photoStudioList

    // 서버에서 가져온 데이터로 업데이트
    fun updatePhotoStudioList(tmpList : List<PhotoStudio>?){
        _photoStudioList.value = tmpList
    }
}