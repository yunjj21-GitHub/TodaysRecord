package com.yunjung.todaysrecord.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.Review

class ReviewViewModel : ViewModel() {
    // 클래스 내부에서만 사용
    private val _reviewList = MutableLiveData<List<Review>?>() // 해당 사진관의 리뷰리스트
    private val _reviewNum = MutableLiveData<Int>(0) // 해당 사진관의 리뷰 개수
    private val _reviewAvg = MutableLiveData<Double>(0.0) // 해당 사진관의 리뷰 평점

    // 클래스 외부에서도 접근 가능
    val reviewList: LiveData<List<Review>?>
        get() = _reviewList

    val reviewNum: LiveData<Int>
        get() = _reviewNum

    val reviewAvg: LiveData<Double>
        get() = _reviewAvg

    fun getReviewList(tmpList : List<Review>?) {
        _reviewList.value = tmpList
    }

    fun getReviewNum(num : Int){
        _reviewNum.value = num
    }
}