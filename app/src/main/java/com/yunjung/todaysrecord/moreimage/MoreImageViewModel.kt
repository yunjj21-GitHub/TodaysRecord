package com.yunjung.todaysrecord.moreimage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.Review

class MoreImageViewModel  : ViewModel() {
    // 수정 가능한 라이브 데이터 (클래스 내부에서만 사용), 초기화 값 0
    private val _reviewList = MutableLiveData<List<Review>>()

    // 수정 불 가능한 라이브 데이터 (클래스 외부에서 접근 시 사용)
    val reviewList: LiveData<List<Review>>
        get() = _reviewList // 클래스 내부에서 사용하는 변수를 get()으로 가져와 반환

    // 초기화
    private val tmpList = ArrayList<Review>()
    init {
        _reviewList.value = tmpList
    }
}