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

class MainActivity : AppCompatActivity(){
    // 데이터 바인딩 + 뷰모델(라이브 데이터 포함)
    private lateinit var binding : ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    // 프래그먼트
    private lateinit var detailFragment: DetailFragment
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

        // 리사이클러뷰 적용
        initRecycler()
        subscribeStudioList()
        
        // 하단 메뉴버튼 클릭 이벤트
        binding.myPageBtn.setOnClickListener{
            myPageFragment = MyPageFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.fragment_frame, myPageFragment)
                .commit()
            binding.myPageBtn.setBackgroundResource(R.drawable.ic_mypage_filled)
            binding.mainBtn.setBackgroundResource(R.drawable.ic_studio_empty)
        }
    }

    // 리사이클러뷰 적용
    private fun initRecycler(){
        binding.recyclerViewStudio.adapter = PhotoStudioAdapter()
        binding.recyclerViewStudio.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        // RecyclerView.VERTICAL : 리사이클러뷰의 스크롤방향을 수직으로 설정
        // false : 리사이클러뷰에 마지막 데이터 ~ 첫번째 데이터 순으로 디스플레이 할 것 인지 설정
    }

    // photoStudioList를 관찰하도록 함
    private fun subscribeStudioList() {
        mainViewModel.photoStudioList.observe(this, {
            // photoStudioList가 변경되었다면 실행
            (binding.recyclerViewStudio.adapter as PhotoStudioAdapter).submitList(it)
        })
    }

    fun setDetailFragment(){
        detailFragment = DetailFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.fragment_frame, detailFragment)
            .commit()
    }
}

