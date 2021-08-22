package com.yunjung.todays_record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todays_record.models.PhotoStudio

class MainViewModel : ViewModel(){
    // 수정 가능한 라이브 데이터 (클래스 내부에서만 사용)
    private val _photoStudioList = MutableLiveData<List<PhotoStudio>>()

    // 수정 불 가능한 라이브 데이터 (클래스 외부에서 접근 시 사용)
    val photoStudioList : LiveData<List<PhotoStudio>>
        get() = _photoStudioList // 클래스 내부에서 사용하는 변수를 get()으로 가져와 반환

    // 초기화
    private val tempList = ArrayList<PhotoStudio>()
    init {
        tempList.add(PhotoStudio(1, R.drawable.sample1,"태양 사진관", "권선구 탑동 9-21 202호",15000))
        tempList.add(PhotoStudio(2,R.drawable.sample2,"새로나 스튜디오", "영통구 매탄동 77-5", 8000))
        tempList.add(PhotoStudio(3,R.drawable.sample3,"사랑이야기", "팔달구 인계동 65-2 (시청역 부근)",12000))
        tempList.add(PhotoStudio(4, R.drawable.sample4,"시현하다", "팔달구 화서동 41-11번지 3층", 10000))
        tempList.add(PhotoStudio(5, R.drawable.sample5,"smile photo studio", "장안구 정자동 35-4 골드빌딩 101호", 5000))

        _photoStudioList.value = tempList
    }
}