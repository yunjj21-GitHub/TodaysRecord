package com.yunjung.todaysrecord.ui.email_login

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentEmailLoginBinding
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.ui.login.LoginFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmailLoginFragment : Fragment() {
    lateinit var binding : FragmentEmailLoginBinding
    lateinit var viewModel : EmailLoginViewModel

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_email_login, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성되었을 때 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(EmailLoginViewModel::class.java)
        binding.viewModel = viewModel

        // 로그인 버튼 클릭 이벤트 설정
        setLoginBtn()

        // 회원가입 버튼 클릭 이벤트 설정
        setSignUpBtn()
    }

    private fun setLoginBtn(){
        binding.loginBtn.setOnClickListener {
            // 사용자 입력값이 올바른지 확인
            if(checkUserInputValid()){
                // 이메일로 로그인
                emailLogin()
            }
        }
    }

    // 사용자 입력값이 올바른지 확인
    private fun checkUserInputValid() : Boolean{
        return when {
            binding.idInput.text.isBlank() -> {
                Toast.makeText(context, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                false
            }
            binding.pwdInput.text.isBlank() -> {
                Toast.makeText(context, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                viewModel.updateIdInputAndPwdInput(binding.idInput.text.toString(), binding.pwdInput.text.toString())
                true
            }
        }
    }

    // 이메일로 로그인
    private fun emailLogin(){
        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO){
                try {
                    RetrofitManager.service.emailLogin(viewModel.idInput.value.toString(),
                        viewModel.pwdInput.value.toString())
                } catch (e : Throwable){
                    null
                }
            }

            if(response != null){ // 로그인 성공 시
                (requireContext().applicationContext as MyApplication).user.value = response // 로그인 처리
                saveAutoLoginInfo(response._id!!) // 추후 자동로그인을 위해 로그인 정보 저장 (자동 로그인 처리)
                Toast.makeText(context, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_emailLoginFragment_to_mainActivity)

                // StartActivity 종료
                requireActivity().finish()
            }else{ // 로그인 실패 시
                Toast.makeText(context, "일치하는 회원정보가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 자동 로그인 정보 저장
    private fun saveAutoLoginInfo(userId : String){
        val autoLogin : SharedPreferences =
            requireContext().getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        with(autoLogin.edit()){
            putString("userId", userId)
            commit()
        }
    }

    // 회원가입 버튼 클릭 이벤트 설정
    private fun setSignUpBtn(){
        binding.emailSignUp.setOnClickListener {
            // 이메일로 회원가입 화면으로 이동
            val direction = EmailLoginFragmentDirections.actionEmailLoginFragmentToJoinMembershipFragment(null, null)
            findNavController().navigate(direction)
        }
    }
}