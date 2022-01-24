package com.yunjung.todaysrecord.main

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.yunjung.todaysrecord.MainViewModel
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.ActivityMainBinding
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(){
    // DataBinding & ViewModel 관련 변수
    private lateinit var binding : ActivityMainBinding
    lateinit var viewModel: MainViewModel

    // Navigation Component 관련 변수
    private lateinit var host : NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel

        // 자동 로그인과 자동 지역설정
        autoLoginAndSetArea()

        viewModel.userArea.observe(this, Observer {
            saveAutoSetAreaInfo()
        })

        setSupportActionBar(binding.toolbar) // 액션바 지정
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바의 타이틀 제거

        // NavController 객체 얻기
        host = supportFragmentManager.findFragmentById(R.id.fragment_frame) as NavHostFragment? ?: return
        navController = host.navController

        // 최상위 수준의 화면 지정
        appBarConfiguration = AppBarConfiguration(setOf(R.id.studioFragment, R.id.boothFragment, R.id.mypageFragment))

        setupActionBar() // 액션바와 NavComponent를 연결

        setupBottomNavMenu() // BottomNav와 NavComponent를 연결

        // 탐색이 수행 될 때 마다 실행
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // 지역설정 버튼 & 텍스트 보임 여부 설정
            setSetLocationBtnAndText(destination)

            // BackButton 아이콘 설정
            setBackButtonIcon(destination)

            // Toolbar title 설정
            setToolbarText(destination)
        }

        // Bottom Navigation 클릭 이벤트 설정
        initBottomNavigation()

        // 지역설정 버튼 클릭 이벤트 설정
        initSetLocationBtn()
    }

    private fun setupActionBar() {
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupBottomNavMenu() {
        binding.bottomNavigation?.setupWithNavController(navController)
    }

    // 뒤로가기 버튼을 동작하도록 함
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setSetLocationBtnAndText(destination : NavDestination){
        if(destination.id == R.id.studioFragment){
            binding.setLocationBtn.visibility = View.VISIBLE
            binding.locationText.visibility = View.VISIBLE
        }else{
            binding.setLocationBtn.visibility = View.INVISIBLE
            binding.locationText.visibility = View.INVISIBLE
        }
    }

    private fun setBackButtonIcon(destination : NavDestination){
        if(destination.id != R.id.studioFragment && destination.id != R.id.boothFragment && destination.id != R.id.mypageFragment){
            binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        }
    }

    private fun setToolbarText(destination : NavDestination){
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
            R.id.loginFragment -> {
                binding.title.text = "로그인"
            }
            R.id.joinMembershipFragment -> {
                binding.title.text = "회원가입"
            }
            else -> {
                binding.title.text = "오늘의 기록"
            }
        }
    }

    private fun initBottomNavigation(){
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

    private fun initSetLocationBtn(){
        binding.setLocationBtn.setOnClickListener {
            navController.navigate(R.id.action_studioFragment_to_setlocationFragment)
        }
    }

    private fun autoLoginAndSetArea(){
        // 자동 로그인
        val autoLoginAndSetArea = getSharedPreferences("autoLoginAndSetArea", MODE_PRIVATE)
        val userId = autoLoginAndSetArea.getString("userId", null)
        if(userId != null) {
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    try {
                        RetrofitManager.service.getUserById(userId)
                    } catch (e: Throwable) {
                        User("anonymous", "로그인해주세요", null, null)
                    }
                }
                (applicationContext as MyApplication).user.value = response
            }
        }

        // 자동 지역 설정
        val userArea = autoLoginAndSetArea.getString("userArea", null)
        if(userArea != null){
            viewModel.updateUerArea(userArea)
        }
    }

    private fun saveAutoSetAreaInfo() {
        val autoLoginAndSetArea: SharedPreferences =
            getSharedPreferences("autoLoginAndSetArea", Activity.MODE_PRIVATE)
        with(autoLoginAndSetArea.edit()) {
            putString("userArea", viewModel.userArea.value)
            commit()
        }
    }
}