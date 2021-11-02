package com.yunjung.todaysrecord.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.Review

class ReviewViewModel : ViewModel() {
    // 클래스 내부에서만 사용
    private val _reviewList = MutableLiveData<List<Review>?>() // 해당 사진관의 리뷰리스트
    private val _reviewNum = MutableLiveData<Int>(0) // 해당 사진관의 리뷰 개수
    private val _reviewAvg = MutableLiveData<Int>(0) // 해당 사진관의 리뷰 평점

    private val _fiveStarRatio = MutableLiveData<Int>(0) // 별점 5점 리뷰의 개수
    private val _fourStarRatio = MutableLiveData<Int>(0) // 별점 5점 리뷰의 개수
    private val _threeStarRatio = MutableLiveData<Int>(0) // 별점 5점 리뷰의 개수
    private val _twoStarRatio = MutableLiveData<Int>(0) // 별점 5점 리뷰의 개수
    private val _oneStarRatio = MutableLiveData<Int>(0) // 별점 5점 리뷰의 개수

    // 클래스 외부에서도 접근 가능
    val reviewList: LiveData<List<Review>?>
        get() = _reviewList

    val reviewNum: LiveData<Int>
        get() = _reviewNum

    val reviewAvg: LiveData<Int>
        get() = _reviewAvg

    val fiveStarRatio: LiveData<Int>
        get() = _fiveStarRatio

    val fourStarRatio: LiveData<Int>
        get() = _fourStarRatio

    val threeStarRatio: LiveData<Int>
        get() = _threeStarRatio

    val twoStarRatio: LiveData<Int>
        get() = _twoStarRatio

    val oneStarRatio: LiveData<Int>
        get() = _oneStarRatio

    fun getReviewList(tmpList : List<Review>?) {
        _reviewList.value = tmpList
    }

    fun getReviewNum(num : Int){
        _reviewNum.value = num
    }

    fun getReviewAvg(avg : Int){
        _reviewAvg.value = avg
    }

    fun getRating(fiveStar : Int, fourStar : Int, threeStar : Int, twoStar : Int, oneStar : Int){
        _fiveStarRatio.value = fiveStar
        _fourStarRatio.value = fourStar
        _threeStarRatio.value = threeStar
        _twoStarRatio.value = twoStar
        _oneStarRatio.value = oneStar
    }
}