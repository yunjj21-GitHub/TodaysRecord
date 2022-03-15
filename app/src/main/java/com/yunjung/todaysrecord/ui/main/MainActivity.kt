package com.yunjung.todaysrecord.ui.main

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.ActivityMainBinding
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
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

        // 자동 지역설정
        autoSetArea()

        viewModel.userArea.observe(this, Observer {
            // 자동 지역설정 정보 저장
            saveAutoSetAreaInfo()
        })

        // 지역설정 버튼 클릭 이벤트 설정
        initSetLocationBtn()

        setSupportActionBar(binding.toolbar) // 레이아웃의 툴바를 액션바로 지정
        supportActionBar?.setDisplayShowTitleEnabled(false) // 안드로이드에서 자동으로 넣는 툴바의 타이틀 제거

        // NavController 객체 얻기
        host = supportFragmentManager.findFragmentById(R.id.fragment_frame) as NavHostFragment? ?: return
        navController = host.navController

        // 최상위 수준의 화면 지정
        appBarConfiguration = AppBarConfiguration(setOf(R.id.studioFragment, R.id.boothFragment, R.id.mypageFragment))

        setupActionBar() // 액션바와 NavComponent를 연결

        // 탐색이 수행 될 때 마다 실행
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // 지역설정 버튼 & 텍스트 보임 여부 설정
            setSetLocationBtnAndText(destination)

            // BackButton 아이콘 설정
            setBackButtonIcon(destination)

            // Toolbar title 설정
            setToolbarText(destination)
        }

        setupBottomNavMenu() // BottomNav와 NavComponent를 연결

        // Bottom Navigation 클릭 이벤트 설정
        initBottomNavigation()
    }

    // 액션바와 NavComponent를 연결
    private fun setupActionBar() {
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // BottomNav와 NavComponent를 연결
    private fun setupBottomNavMenu() {
        binding.bottomNavigation?.setupWithNavController(navController)
    }

    // 뒤로가기 버튼을 동작하도록 함
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // 지역설정 버튼 & 텍스트 보임 여부 설정
    private fun setSetLocationBtnAndText(destination : NavDestination){
        if(destination.id == R.id.studioFragment){
            binding.setLocationBtn.visibility = View.VISIBLE
            binding.locationText.visibility = View.VISIBLE
        }else{
            binding.setLocationBtn.visibility = View.INVISIBLE
            binding.locationText.visibility = View.INVISIBLE
        }
    }

    // 전환되는 프래그먼트에 따라 뒤로가기 버튼 아이콘 설정
    private fun setBackButtonIcon(destination : NavDestination){
        if(destination.id != R.id.studioFragment && destination.id != R.id.boothFragment && destination.id != R.id.mypageFragment){
            binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        }
    }

    // 전환되는 프래그먼트에 따라 타이틀 설정
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
            else -> {
                binding.title.text = "오늘의 기록"
            }
        }
    }

    // Bottom Navigation 클릭 이벤트 설정
    private fun initBottomNavigation(){
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.studio->{
                    // popUpTo와 popUpToInclusive 옵션 설정
                    val builder = NavOptions.Builder()
                    val options = builder.setPopUpTo(R.id.studioFragment, true).build()

                    navController.navigate(R.id.action_global_studioFragment, null, options)
                }
                R.id.booth -> {
                    val builder = NavOptions.Builder()
                    val options = builder.setPopUpTo(R.id.boothFragment, true).build()
                    navController.navigate(R.id.action_global_boothFragment, null, options)
                }

                R.id.myPage -> {
                    val builder = NavOptions.Builder()
                    val options = builder.setPopUpTo(R.id.mypageFragment, true).build()
                    navController.navigate(R.id.action_global_mypageFragment, null, options)
                }
            }
            true
        }
    }

    // 지역설정 버튼 클릭 이벤트 설정
    private fun initSetLocationBtn(){
        binding.setLocationBtn.setOnClickListener {
            navController.navigate(R.id.action_studioFragment_to_setlocationFragment)
        }
    }

    // 자동 지역설정
    private fun autoSetArea(){
        val autoSetArea = getSharedPreferences("autoSetArea", MODE_PRIVATE)
        val userArea = autoSetArea.getString("userArea", null)
        if(userArea != null){
            viewModel.updateUerArea(userArea)
        }
    }

    // 자동 지역설정 정보 저장
    private fun saveAutoSetAreaInfo() {
        val autoSetArea: SharedPreferences =
            getSharedPreferences("autoSetArea", Activity.MODE_PRIVATE)
        with(autoSetArea.edit()) {
            putString("userArea", viewModel.userArea.value)
            commit()
        }
    }
}