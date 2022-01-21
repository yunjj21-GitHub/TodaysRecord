package com.yunjung.todaysrecord.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentLoginBinding
import com.yunjung.todaysrecord.login_manager.GoogleLoginManager
import com.yunjung.todaysrecord.login_manager.KaKaoLoginManager
import com.yunjung.todaysrecord.login_manager.NaverLoginManager

class LoginFragment : Fragment() {
    // DataBinding & ViewModel 관련 변수
    lateinit var binding : FragmentLoginBinding
    lateinit var viewModel: LoginViewModel

    private lateinit var googleLoginManager : GoogleLoginManager // 구글 로그인 관련
    private lateinit var naverLoginManager : NaverLoginManager // 네이버 로그인 관련
    private lateinit var kakaoLoginManager: KaKaoLoginManager // 카카오 로그인 관련

    companion object{
        fun newInstance() : LoginFragment{
            return LoginFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        // 구글 로그인
        googleLoginManager = GoogleLoginManager(this)

        // 네이버 로그인
        naverLoginManager = NaverLoginManager(this)

        // 카카오 로그인
        kakaoLoginManager = KaKaoLoginManager(this)

        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel

        // googleSignInBtn 클릭 이벤트 설정
        googleLoginManager.setGoogleSignInBtn(binding.googleSignInBtn)

        // naverSignInBtn에 핸들러 부착
        naverLoginManager.setNaverLoginBtn(binding.naverLoginBtn)

        // 카카오 로그인 버튼에 클릭 이벤트 설정
        kakaoLoginManager.setKakaoLoginBtn(binding.kakaoLoginBtn)
    }
}