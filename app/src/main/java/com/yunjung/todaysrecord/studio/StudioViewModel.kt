package com.yunjung.todaysrecord.studio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.PhotoStudio

class StudioViewModel : ViewModel() {
    // 수정 가능한 라이브 데이터 (클래스 내부에서만 사용)
    private val _photoStudioList = MutableLiveData<List<PhotoStudio>?>()

    // 수정 불가능한 라이브 데이터 (클래스 외부에서 접근 시 사용)
    val photoStudioList : LiveData<List<PhotoStudio>?>
        get() = _photoStudioList

    fun updatePhotoStudioList(tmpList : List<PhotoStudio>?){
        _photoStudioList.value = tmpList
    }
}