package com.yunjung.todays_record.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todays_record.MainViewModel
import com.yunjung.todays_record.R
import com.yunjung.todays_record.databinding.ActivityMainBinding
import com.yunjung.todays_record.detail.DetailFragment
import com.yunjung.todays_record.mypage.MyPageFragment
import com.yunjung.todays_record.recyclerview.PhotoStudioAdapter
import com.yunjung.todays_record.studio.StudioFragment

class MainActivity : AppCompatActivity(){
    // 데이터 바인딩 + 뷰모델(라이브 데이터 포함)
    private lateinit var binding : ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    // 프래그먼트
    private lateinit var studioFragment: StudioFragment
    private lateinit var myPageFragment: MyPageFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)// binding 객체의 레이아웃 지정

        // 현재의 액티비티를 라이프사이클의 오너로 명시
        // (라이프 사이클을 감시하며 변화를 binding 객체에 적용할 수 있다.)
        binding.lifecycleOwner = this
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // 넘겨준 mainViewModel binding객체의 레이아웃으로 넘어감
        binding.viewModel = mainViewModel

        // 기본 프래그먼트 설정
        studioFragment = StudioFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment, studioFragment).commit()

        // bottom navigation menu 이벤트 설정

    }
}