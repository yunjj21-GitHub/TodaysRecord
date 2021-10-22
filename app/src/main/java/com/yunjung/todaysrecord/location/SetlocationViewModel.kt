package com.yunjung.todaysrecord.location

import com.yunjung.todaysrecord.models.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SetlocationViewModel : ViewModel() {
    // 수정 가능한 라이브 데이터 (클래스 내부에서만 사용), 초기화 값 0
    private val _locationList = MutableLiveData<List<Location>>()

    // 수정 불 가능한 라이브 데이터 (클래스 외부에서 접근 시 사용)
    val locationList: LiveData<List<Location>>
        get() = _locationList // 클래스 내부에서 사용하는 변수를 get()으로 가져와 반환

    // 초기화
    private val locationTempList = ArrayList<Location>()
    init {
        // recyclerViewLocation_1에 담길 배열
        locationTempList.add(Location("서울"))
        locationTempList.add(Location("경기"))
        locationTempList.add(Location("인천"))
        locationTempList.add(Location("강원"))
        locationTempList.add(Location("대전"))
        locationTempList.add(Location("세종"))
        locationTempList.add(Location("충남"))
        locationTempList.add(Location("충북"))
        locationTempList.add(Location("부산"))
        locationTempList.add(Location("울산"))
        locationTempList.add(Location("경님"))
        locationTempList.add(Location("경북"))
        locationTempList.add(Location("대구"))
        locationTempList.add(Location("광주"))
        locationTempList.add(Location("전남"))
        locationTempList.add(Location("제주"))
        locationTempList.add(Location("전국"))

        _locationList.value = locationTempList
    }
}