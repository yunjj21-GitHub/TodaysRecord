package com.yunjung.todaysrecord.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.PhotoStudio

class DetailViewModel : ViewModel() {
    // 수정 가능한 라이브 데이터 (클래스 내부에서만 사용), 초기화 값 0
    private val _photoStudio = MutableLiveData<PhotoStudio>()

    // 수정 불 가능한 라이브 데이터 (클래스 외부에서 접근 시 사용)
    val photoStudio: LiveData<PhotoStudio>
        get() = _photoStudio // 클래스 내부에서 사용하는 변수를 get()으로 가져와 반환

    // ViewModel이 가지고 있는 값을 업데이트
    fun updateValue(photoStudio: PhotoStudio) {
        _photoStudio.value = photoStudio
    }
}
