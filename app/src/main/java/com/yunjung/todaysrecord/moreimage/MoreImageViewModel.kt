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
        tmpList.add(Review(1, "yunjj21", 5, "오랜만에 찍은 증명사진!\n걱적 많이 했는데 예쁘게 나왔네요ㅠㅠ!\n만족합니다.","https://lh3.googleusercontent.com/proxy/pNKW99dqDdCTGV_UvIhP5lcNXk8wRLsSTh4fqXEOWQYjmYgaBYCAbl7N8hnOmUQXoWxqm333KBVtRqJzKS0PvTvmQOFypJDTVz1oFendDo5I5AoKW2LSYdBYs-b-RlwuFEl32vg",2021828))
        tmpList.add(Review(2, "heejung", 4, "사장님 친철하셔서 너무 좋네요.\n추천~!","https://lh3.googleusercontent.com/proxy/pNKW99dqDdCTGV_UvIhP5lcNXk8wRLsSTh4fqXEOWQYjmYgaBYCAbl7N8hnOmUQXoWxqm333KBVtRqJzKS0PvTvmQOFypJDTVz1oFendDo5I5AoKW2LSYdBYs-b-RlwuFEl32vg",20210815))
        tmpList.add(Review(3, "urim_9012", 5, "이력서용 사진찍기 무난한듯", "https://lh3.googleusercontent.com/proxy/pNKW99dqDdCTGV_UvIhP5lcNXk8wRLsSTh4fqXEOWQYjmYgaBYCAbl7N8hnOmUQXoWxqm333KBVtRqJzKS0PvTvmQOFypJDTVz1oFendDo5I5AoKW2LSYdBYs-b-RlwuFEl32vg", 20210620))
        tmpList.add(Review(4, "6_funny", 5, "매년 새학기 사진은 여기서 찍네요.", "https://lh3.googleusercontent.com/proxy/pNKW99dqDdCTGV_UvIhP5lcNXk8wRLsSTh4fqXEOWQYjmYgaBYCAbl7N8hnOmUQXoWxqm333KBVtRqJzKS0PvTvmQOFypJDTVz1oFendDo5I5AoKW2LSYdBYs-b-RlwuFEl32vg", 20210617))
        tmpList.add(Review(5, "tangotango", 3, "잘 찍으시네요.\n보정도 요구에 맞게 자연스럽게 해주시고요.\n감사합니다.", "https://lh3.googleusercontent.com/proxy/pNKW99dqDdCTGV_UvIhP5lcNXk8wRLsSTh4fqXEOWQYjmYgaBYCAbl7N8hnOmUQXoWxqm333KBVtRqJzKS0PvTvmQOFypJDTVz1oFendDo5I5AoKW2LSYdBYs-b-RlwuFEl32vg", 20210530))

        _reviewList.value = tmpList
    }
}