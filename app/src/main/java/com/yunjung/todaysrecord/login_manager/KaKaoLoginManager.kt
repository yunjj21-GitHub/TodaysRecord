package com.yunjung.todaysrecord.login_manager

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.ui.login.LoginFragment
import com.yunjung.todaysrecord.ui.login.LoginFragmentDirections
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KaKaoLoginManager(val loginFragment: LoginFragment) {
    private val context = loginFragment.requireContext()

    // 카카오 로그인 버튼 클릭 이벤트 설정
    fun setKakaoLoginBtn(kakaoLoginBtn : AppCompatButton){
        kakaoLoginBtn.setOnClickListener {
            loginWithKakaoTalk()
        }
    }

    // 카카오톡으로 로그인
    private fun loginWithKakaoTalk(){
        if(UserApiClient.instance.isKakaoTalkLoginAvailable(context)){
            UserApiClient.instance.loginWithKakaoTalk(context){ token, error ->
                if(error != null){ // 카카오톡으로 로그인을 실패했다면
                    Log.e(ContentValues.TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리
                    if(error is ClientError && error.reason == ClientErrorCause.Cancelled){
                        findNavController(loginFragment).navigateUp() // 뒤로가기
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    loginWithKakaoAccount()
                } else if(token != null){ // 카카오톡으로 로그인을 성공했다면
                    getKakaoUserInfo()
                }
            }
        }
    }

    // 카카오 계정으로 로그인
    private fun loginWithKakaoAccount(){
        UserApiClient.instance.loginWithKakaoAccount(context){ token, error ->
            if (error != null) { // 카카오 계정으로 로그인을 실패 했다면
                Log.e(ContentValues.TAG, "로그인 실패", error)
            }
            else if (token != null) { // 카카오 계정으로 로그인을 성공했다면
                getKakaoUserInfo()
            }
        }
    }

    // 로그인된 카카오 사용자의 정보를 요청
    private fun getKakaoUserInfo(){
        UserApiClient.instance.me { user, error ->
            if (error != null) { // 사용자 정보 요청을 실패 했다면
                Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) { // 사용자 정보 요청에 성공했다면
                // 어플의 실질적인 로그인 처리
                todaysRecordLoginOrSignIn(
                    user.kakaoAccount!!.email.toString(),
                    user.kakaoAccount!!.profile?.thumbnailImageUrl.toString()
                )
            }
        }
    }


    private fun todaysRecordLoginOrSignIn(email : String, profileImage : String){
        // 가입된 이메일인지 확인
        loginFragment.lifecycleScope.launch {
            val response = withContext(Dispatchers.IO){
                try{
                    RetrofitManager.service.checkIfEmailAlreadySingedUp(email)
                }catch (e : Throwable){
                    null
                }
            }
            if(response != null){ // 가입되어 있는 이메일이라면 로그인처리
                (context.applicationContext as MyApplication).user.value = response // 로그인
                saveAutoLoginInfo(response._id!!) // 추후 자동로그인을 위해 로그인 정보 저장
                Toast.makeText(context, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                findNavController(loginFragment).navigate(R.id.action_loginFragment_to_mainActivity)

                // StartActivity 종료
                loginFragment.requireActivity().finish()
            }else{ // 가입되어 있지 않은 이메일이라면 회원가입처리
                // 회원가입 화면으로 이동
                val direction = LoginFragmentDirections.actionLoginFragmentToJoinMembershipFragment(email, profileImage)
                findNavController(loginFragment).navigate(direction)
            }
        }
    }

    // 자동 로그인 정보 저장
    private fun saveAutoLoginInfo(userId : String){
        val autoLogin : SharedPreferences =
            loginFragment.requireContext().getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        with(autoLogin.edit()){
            putString("userId", userId)
            commit()
        }
    }
}