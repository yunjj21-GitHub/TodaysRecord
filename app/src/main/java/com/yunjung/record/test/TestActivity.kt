package com.yunjung.record.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yunjung.record.ActionType
import com.yunjung.record.R
import com.yunjung.record.TestViewModel
import com.yunjung.record.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity(){
    private lateinit var binding : ActivityTestBinding
    lateinit var testViewModel: TestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test) //

        testViewModel = ViewModelProvider(this).get(TestViewModel::class.java)

        // 현재의 액티비티를 라이프사이클의 오너로 명시
        // (라이프 사이클을 감시하며 변화를 binding 객체에 적용할 수 있다.)
        binding.setLifecycleOwner(this)
        // 넘겨준 testViewModel이 binding객체의 레이아웃으로 넘어감
        binding.setViewModel(testViewModel)
    }
}