package com.yunjung.todaysrecord.myinterests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.Review

class MyinterestsViewModel : ViewModel() {
    // 수정 가능한 라이브 데이터 (클래스 내부에서만 사용), 초기화 값 0
    private val _interestsNum = MutableLiveData<Int>(0) // 유저가 찜한 사진관 개수
    private val _interestsList = MutableLiveData<List<PhotoStudio>?>() // 유저가 찜한 사진관 리스트

    // 수정 불가능한 라이브 데이터 (클래스 외부에서 접근 시 사용)
    val interestsNum: LiveData<Int>
        get() = _interestsNum // 클래스 내부에서 사용하는 변수를 get()으로 가져와 반환

    val interestsList: LiveData<List<PhotoStudio>?>
        get() = _interestsList

    // ViewModel이 가지고 있는 값을 업데이트
    fun updateInterestsNum(num : Int) {
        _interestsNum.value = num
    }

    fun updateInterestsList(tmpList : List<PhotoStudio>?) {
        _interestsList.value = tmpList
    }
}