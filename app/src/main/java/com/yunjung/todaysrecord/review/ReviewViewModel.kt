package com.yunjung.todaysrecord.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.Review

class ReviewViewModel : ViewModel() {
    // 수정 가능한 라이브 데이터 (클래스 내부에서만 사용), 초기화 값 0
    private val _reviewList = MutableLiveData<List<Review>>()

    // 수정 불 가능한 라이브 데이터 (클래스 외부에서 접근 시 사용)
    val reviewList: LiveData<List<Review>>
        get() = _reviewList // 클래스 내부에서 사용하는 변수를 get()으로 가져와 반환

    // 초기화
    private val tmpList = ArrayList<Review>()
    init {
        tmpList.add(Review(userId = "yunjj21", rating = 5, content = "오랜만에 찍은 증명사진!\n걱적 많이 했는데 예쁘게 나왔네요ㅠㅠ!\n만족합니다.",image = "https://lh3.googleusercontent.com/proxy/pNKW99dqDdCTGV_UvIhP5lcNXk8wRLsSTh4fqXEOWQYjmYgaBYCAbl7N8hnOmUQXoWxqm333KBVtRqJzKS0PvTvmQOFypJDTVz1oFendDo5I5AoKW2LSYdBYs-b-RlwuFEl32vg"))

        _reviewList.value = tmpList
    }
}