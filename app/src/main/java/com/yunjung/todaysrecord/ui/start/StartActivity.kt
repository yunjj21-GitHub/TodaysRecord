package com.yunjung.todaysrecord.ui.start

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.ActivityStartBinding
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartActivity : AppCompatActivity() {
    // DataBinding & ViewModel 관련 변수
    lateinit var binding : ActivityStartBinding
    lateinit var viewModel : StartViewModel

    // Navigation Component 관련 변수
    private lateinit var host : NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start) // 레이아웃과 액티비티를 바인딩
        binding.lifecycleOwner = this // 해당 액티비티의 라이프사이클 오너를 알려줌
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        binding.viewModel = viewModel // 레이아웃 뷰모델에게 옵저버할 뷰모델을 넘겨줌

        /* Navigation Component 관련 */
        setSupportActionBar(binding.toolbar) // 레이아웃의 툴바를 액션바로 지정
        supportActionBar?.setDisplayShowTitleEnabled(false) // 안드로이드에서 자동으로 넣는 툴바의 타이틀 제거

        // NavController 객체 얻기
        host = supportFragmentManager.findFragmentById(R.id.fragment_frame) as NavHostFragment? ?: return
        navController = host.navController

        // 최상위 수준의 화면 지정
        appBarConfiguration = AppBarConfiguration(navController.graph)

        // 액션바와 NavComponent를 연결
        setupActionBar()

        // 전환되는 Fragment에 따라 적절한 Title과 BackButton이 보이도록 함
        setTitleAndBackButton(navController)

        // 자동 로그인 처리
        autoLogin()
    }

    // 자동 로그인 처리
    private fun autoLogin() {
        val autoLogin = getSharedPreferences("autoLogin", MODE_PRIVATE)
        val userId = autoLogin.getString("userId", null)

        if(userId != null) { // 자동 로그인 정보가 있다면 (이전에 로그인 했던 상태라면)
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    try {
                        RetrofitManager.service.getUserById(userId)
                    } catch (e: Throwable) {
                        User(null, null, null, null)
                    }
                }
                (applicationContext as MyApplication).user.value = response // 로그인 처리
            }

            // MainActivity로 이동
            navController.navigate(R.id.action_loginFragment_to_mainActivity)

            // 현재 액티비티 종료
            finish()
        }
    }


    // 액션바와 NavComponent를 연결
    private fun setupActionBar() {
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // 뒤로가기 버튼을 동작하도록 함
    override fun onSupportNavigateUp(): Boolean {
        return host.navController.navigateUp() || super.onSupportNavigateUp()
    }

    // 전환되는 Fragment에 따라 적절한 Title과 BackButton이 보이도록 함
    private fun setTitleAndBackButton(navController : NavController){
        // 탐색이 수행 될 마다 실행됨
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // 뒤로가기 버튼 및 타이틀 설정
            if(destination.id == R.id.loginFragment){
                binding.title.visibility = View.INVISIBLE
            }else{
                binding.title.visibility = View.VISIBLE
                binding.toolbar.setNavigationIcon(R.drawable.ic_back_purple)

                if(destination.id == R.id.joinMembershipFragment) binding.title.text = "이메일로 회원가입"
                else if(destination.id == R.id.emailLoginFragment) binding.title.text = "이메일 로그인"
                else if(destination.id == R.id.consentFragment ||
                    destination.id == R.id.consentDetailFragment) binding.title.text = "약관동의"
            }
        }
    }
}