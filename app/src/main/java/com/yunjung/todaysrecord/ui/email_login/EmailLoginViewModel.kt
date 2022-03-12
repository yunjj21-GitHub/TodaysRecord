package com.yunjung.todaysrecord.ui.email_login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EmailLoginViewModel : ViewModel() {
    private val _idInput = MutableLiveData<String>("")

    private val _pwdInput = MutableLiveData<String>("")

    val idInput : LiveData<String>
        get() = _idInput

    val pwdInput : LiveData<String>
        get() = _pwdInput

    fun updateIdInputAndPwdInput(idInput : String, pwdInput : String){
        _idInput.value = idInput
        _pwdInput.value = pwdInput
    }
}