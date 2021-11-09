package com.yunjung.todaysrecord.myreviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.Review

class MyreviewViewModel : ViewModel() {
    // 수정 가능한 라이브 데이터 (클래스 내부에서만 사용), 초기화 값 0
    private val _reviewNum = MutableLiveData<Int>(0) // 유저가 작성한 리뷰의 개수
    private val _reviewList = MutableLiveData<List<Review>?>() // 유저가 작성한 리뷰 목록

    // 수정 불가능한 라이브 데이터 (클래스 외부에서 접근 시 사용)
    val reviewNum: LiveData<Int>
        get() = _reviewNum // 클래스 내부에서 사용하는 변수를 get()으로 가져와 반환

    val reviewList: LiveData<List<Review>?>
        get() = _reviewList

    // ViewModel이 가지고 있는 값을 업데이트
    fun updateReviewNum(num : Int) {
        _reviewNum.value = num
    }

    fun updateReviewList(tmpList : List<Review>?) {
        _reviewList.value = tmpList
    }
}