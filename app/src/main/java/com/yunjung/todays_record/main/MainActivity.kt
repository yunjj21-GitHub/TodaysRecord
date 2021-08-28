package com.yunjung.todays_record.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.yunjung.todays_record.MainViewModel
import com.yunjung.todays_record.R
import com.yunjung.todays_record.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    // 데이터 바인딩 + 뷰모델(라이브 데이터 포함)
    private lateinit var binding : ActivityMainBinding
    lateinit var mainViewModel: MainViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var host : NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 데이터 바인딩 & 뷰모델
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)// binding 객체의 레이아웃 지정

        // 현재의 액티비티를 라이프사이클의 오너로 명시
        // (라이프 사이클을 감시하며 변화를 binding 객체에 적용할 수 있다.)
        binding.lifecycleOwner = this
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // 넘겨준 mainViewModel binding객체의 레이아웃으로 넘어감
        binding.viewModel = mainViewModel

        // 툴바 설정
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar) // 해당 액티비티의 앱바로 설정
        supportActionBar?.setDisplayShowTitleEnabled(false) // 액션바에서 타이틀을 숨김

        // 프래그먼트의 host를 지정
        host = supportFragmentManager
            .findFragmentById(R.id.fragment_frame) as NavHostFragment? ?: return

        val navController = host.navController // navController : NavHost에서 App Navigation을 관리하는 객체

        appBarConfiguration = AppBarConfiguration(navController.graph) // appBar에게 graph에 대한 정보를 전달

        setupActionBar(navController, appBarConfiguration)

        setupBottomNavMenu(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            // back button icon 설정
            if(destination.id != R.id.studioFragment){
                if(destination.id != R.id.boothFragment){
                    if(destination.id != R.id.mypageFragment){
                        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
                    }
                }
            }

            // Toolbar title 설정
            var titleName : String
            when(destination.id){
                R.id.mypageFragment -> {
                    titleName = "나의 기록"
                }
                R.id.editFragment -> {
                    titleName = "내 정보 수정"
                }
                R.id.myinterestsFragment ->{
                    titleName = "관심목록"
                }
                R.id.myreviewFragment -> {
                    titleName = "리뷰관리"
                }
                else -> {
                    titleName = "오늘의 기록"
                }
            }
            binding.title.text = titleName
        }
    }

    // bottom navigation button 움직임 설정
    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = binding.bottomNavigation

        bottomNav?.setupWithNavController(navController)

        bottomNav.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.studio->{
                    navController.navigate(R.id.action_global_studioFragment)
                }
                R.id.booth -> {
                    navController.navigate(R.id.action_global_boothFragment)
                }

                R.id.myPage -> {
                    navController.navigate(R.id.action_global_mypageFragment)
                }
            }
            true
        }
    }

    private fun setupActionBar(navController: NavController,
                               appBarConfig : AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    // 뒤로가기 버튼을 동작하도록 함
    override fun onSupportNavigateUp(): Boolean {
        return host.navController.navigateUp() || super.onSupportNavigateUp()
    }
}