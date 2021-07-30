package com.yunjung.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// update메소드에 필요한 액션타입 정의
enum class ActionType { // enum : 열거체를 정의
    PLUS, MINUS
}

class TestViewModel : ViewModel(){
    // _ : 클래스 내부에서만 사용하는 변수, MutableLiveData : 수정 가능
    private val _counter = MutableLiveData<Int>()

    // LiveData : 수정 불가능
    val counter : LiveData<Int> // 외부에서 counter에 접근하면 클래스내에 _counter의 값을 get()으로 가져와 반환
        get() = _counter

    // 초기값 설정
    init{
        _counter.value = 0
    }

    // ViewModel이 가지고 있는 값을 업데이트
    fun updateValue(actionType : ActionType){
        when(actionType){
            ActionType.PLUS ->
                _counter.value = _counter.value?.plus(1)
            ActionType.MINUS ->
                _counter.value  = _counter.value?.minus(1)
        }
    }
}