package com.yunjung.todaysrecord.ui.main

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
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
class MainActivity : AppCompatActivity() , PopupMenu.OnMenuItemClickListener {
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
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.studioFragment, R.id.boothFragment, R.id.mypageFragment, R.id.searchFragment))

        setupActionBar() // 액션바와 NavComponent를 연결

        // 탐색이 수행 될 때 마다 실행
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // 사진관 찾기 옵션 버튼 보임 유무 설정
            setOptionBtn(destination)

            // BackButton 아이콘 설정
            setBackButtonIcon(destination)

            // Toolbar title 설정
            setToolbarText(destination)
        }

        setupBottomNavMenu() // BottomNav와 NavComponent를 연결

        // Bottom Navigation 클릭 이벤트 설정
        initBottomNavigation()

        // 정렬 버튼 클릭 이벤트 설정
        initSortBtn()
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

    // 사진관 찾기 옵션 버튼(지역설정 & 정렬) 보임 유무 설정
    private fun setOptionBtn(destination : NavDestination){
        if(destination.id == R.id.studioFragment){
            // 지역설정 관련
            binding.setLocationBtn.visibility = View.VISIBLE
            binding.locationText.visibility = View.VISIBLE

            // 정렬 관련
            binding.sortBtn.visibility = View.VISIBLE
            binding.sortText.visibility = View.VISIBLE
        }else{
            // 지역설정 관련
            binding.setLocationBtn.visibility = View.INVISIBLE
            binding.locationText.visibility = View.INVISIBLE

            // 정렬 관련
            binding.sortBtn.visibility = View.INVISIBLE
            binding.sortText.visibility = View.INVISIBLE
        }
    }

    // 전환되는 프래그먼트에 따라 뒤로가기 버튼 아이콘 설정
    private fun setBackButtonIcon(destination : NavDestination){
        if(destination.id != R.id.studioFragment &&
            destination.id != R.id.boothFragment &&
            destination.id != R.id.mypageFragment &&
            destination.id != R.id.searchFragment){
            binding.toolbar.setNavigationIcon(R.drawable.ic_back_white)
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
            R.id.searchFragment ->{
                binding.title.text = "검색하기"
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
                R.id.search -> {
                    val builder = NavOptions.Builder()
                    val options = builder.setPopUpTo(R.id.searchFragment, true).build()
                    navController.navigate(R.id.action_global_searchFragment, null, options)
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

    // 정렬 버튼 클릭 이벤트 설정
    private fun initSortBtn(){
        binding.sortBtn.setOnClickListener {
            showSorPopUpMenu(it) // 정렬 메뉴를 보여줌
        }
    }

    // 정렬 메뉴를 보여줌
    private fun showSorPopUpMenu(v : View) {
        PopupMenu(v.context, v).apply {
            setOnMenuItemClickListener(this@MainActivity)
            inflate(R.menu.photostudio_sort_menu)
            show()
        }
    }

    // 팝업 메뉴 아이템 클릭 이벤트 설정
    override fun onMenuItemClick(item: MenuItem): Boolean {
        viewModel.updateSortOption(item.toString())
        return when(item.itemId){
            R.id.basic -> { // 기본순 정렬
                true
            }
            R.id.popularity -> { // 인기순(찜 많은 순) 정렬
                true
            }
            R.id.cost -> { // 가격순(가격이 낮은 순)정렬
                true
            }
            else -> false
        }
    }
}