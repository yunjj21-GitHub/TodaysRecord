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
import com.naver.maps.map.NaverMapSdk
import com.yunjung.todays_record.MainViewModel
import com.yunjung.todays_record.R
import com.yunjung.todays_record.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    // DataBinding & ViewMdoel(+LiveData) 관련 변수
    private lateinit var binding : ActivityMainBinding
    lateinit var viewModel: MainViewModel

    // Navigation Component 관련 변수
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var host : NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* DataBinding & ViewMdoel(+LiveData) 관련 */
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.viewModel = viewModel

        /* Navigation Component 관련 */
        // 툴바 설정
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바의 타이틀 제거

        // NavHost를 이용하여 NavController가져오기
        host = supportFragmentManager.findFragmentById(R.id.fragment_frame) as NavHostFragment? ?: return
        val navController = host.navController

        // 최상위 수준의 화면 지정
        appBarConfiguration = AppBarConfiguration(setOf(R.id.studioFragment, R.id.boothFragment, R.id.mypageFragment))
        // bottom navigation을 사용하지 않는다면 AppBarConfiguration(navController.graph)를 넘겨준다.

        // 액션바에 navController와 appBarConfiguration객체를 설정
        setupActionBar(navController, appBarConfiguration)

        // botoom navigation에 navController객체를 설정
        setupBottomNavMenu(navController)

        // 탐색이 수행 될 때 마다 실행
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // back button icon 이미지 설정
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
                R.id.boothFragment->{
                    titleName = "사진부스 찾기"
                }
                R.id.mypageFragment -> {
                    titleName = "나의 기록"
                }
                R.id.editFragment -> {
                    titleName = "프로필 수정"
                }
                R.id.myinterestsFragment ->{
                    titleName = "관심목록"
                }
                R.id.myreviewFragment -> {
                    titleName = "리뷰관리"
                }
                R.id.imageFragment -> {
                    titleName = "사진 더보기"
                }
                else -> {
                    titleName = "오늘의 기록"
                }
            }
            binding.title.text = titleName
        }

        // bottom navigation 이벤트 설정
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
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

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = binding.bottomNavigation
        bottomNav?.setupWithNavController(navController)
    }

    // 뒤로가기 버튼을 동작하도록 함
    override fun onSupportNavigateUp(): Boolean {
        return host.navController.navigateUp() || super.onSupportNavigateUp()
    }
}