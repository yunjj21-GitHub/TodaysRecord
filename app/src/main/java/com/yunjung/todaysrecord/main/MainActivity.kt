package com.yunjung.todaysrecord.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.yunjung.todaysrecord.MainViewModel
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.ActivityMainBinding
import com.yunjung.todaysrecord.network.RetrofitManager

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

        // 임의의 로그인
        viewModel.updateUserId("616be02b8346b820364b8295")

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
            // 지역설정 버튼 & 텍스트 보임 여부 설정
            if(destination.id == R.id.studioFragment){
                binding.locationBtn.visibility = View.VISIBLE
                binding.locationText.visibility = View.VISIBLE
            }else{
                binding.locationBtn.visibility = View.INVISIBLE
                binding.locationText.visibility = View.INVISIBLE
            }

            // back button icon 이미지 설정
            if(destination.id != R.id.studioFragment && destination.id != R.id.boothFragment && destination.id != R.id.mypageFragment){
                binding.toolbar.setNavigationIcon(R.drawable.ic_back)
            }

            // Toolbar title 설정
            when(destination.id){
                R.id.boothFragment->{
                    binding.title.text = "사진부스 찾기"
                }
                R.id.mypageFragment -> {
                    binding.title.text = "나의 기록"
                }
                R.id.editFragment -> {
                    binding.title.text = "프로필 수정"
                }
                R.id.myinterestsFragment ->{
                    binding.title.text = "관심목록"
                }
                R.id.myreviewFragment -> {
                    binding.title.text = "리뷰관리"
                }
                R.id.moreimageFragment -> {
                    binding.title.text = "사진 더보기"
                }
                else -> {
                    binding.title.text = "오늘의 기록"
                }
            }
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

        /* 지역설정 버튼 관련 */
        binding.locationBtn.setOnClickListener {
            navController.navigate(R.id.action_studioFragment_to_setlocationFragment)
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