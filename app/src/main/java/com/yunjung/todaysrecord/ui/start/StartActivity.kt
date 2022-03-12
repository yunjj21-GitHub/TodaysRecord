package com.yunjung.todaysrecord.ui.start

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    // Navigation Component 관련 변수
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var host : NavHostFragment

    // DataBinding & ViewModel 관련 변수
    lateinit var binding : ActivityStartBinding
    lateinit var viewModel : StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start) // 레이아웃과 액티비티를 바인딩
        binding.lifecycleOwner = this // 해당 액티비티의 라이프사이클 오너를 알려줌
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        binding.viewModel = viewModel // 레이아웃 뷰모델에게 옵저버할 뷰모델을 넘겨줌

        /* Navigation Component 관련 */
        // 레이아웃의 툴바를 액션바로 등록
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // 안드로이드에서 자동으로 넣는 타이틀을 제거

        // NavHost를 이용하여 NavController가져오기
        host = supportFragmentManager.findFragmentById(R.id.fragment_frame) as NavHostFragment? ?: return
        val navController = host.navController

        // 최상위 수준의 화면 지정
        appBarConfiguration = AppBarConfiguration(navController.graph)

        // 액션바에 navController와 appBarConfiguration객체를 설정
        setupActionBar(navController, appBarConfiguration)

        setTitleAndBackButton(navController)
    }

    // 툴바를 액션바로 등록
    private fun setupActionBar(navController: NavController,
                               appBarConfig : AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    // back button이 동작하도록 함
    override fun onSupportNavigateUp(): Boolean {
        return host.navController.navigateUp() || super.onSupportNavigateUp()
    }

    // 전환되는 Fragment에 따라 적절한 Title과 BackButton이 보이도록 함
    private fun setTitleAndBackButton(navController : NavController){
        // 탐색이 수행 될 마다 실행됨
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // 뒤로가기 버튼 및 타이틀 설정
            setBackButtonIcon(destination)
        }
    }

    private fun setBackButtonIcon(destination : NavDestination){
        // 뒤로가기 버튼 및 타이틀 설정
        if(destination.id == R.id.loginFragment){
            binding.title.visibility = View.INVISIBLE
        }else{
            binding.title.visibility = View.VISIBLE
            binding.toolbar.setNavigationIcon(R.drawable.ic_back_s)

            if(destination.id == R.id.joinMembershipFragment) binding.title.text = "이메일로 회원가입"
            else if(destination.id == R.id.emailLoginFragment) binding.title.text = "이메일 로그인"
        }
    }
}