package com.yunjung.todaysrecord.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    // 수정 가능한 라이브 데이터 (클래스 내부에서만 사용), 초기화 값 0
    private val _userArea = MutableLiveData<String>("전국")

    // 수정 불가능한 라이브 데이터 (클래스 외부에서 접근 시 사용)
    val userArea: LiveData<String>
        get() = _userArea // 클래스 내부에서 사용하는 변수를 get()으로 가져와 반환

    fun updateUerArea(userArea : String){
        _userArea.value = userArea
    }
}