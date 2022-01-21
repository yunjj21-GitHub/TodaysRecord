package com.yunjung.todaysrecord.login_manager

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.login.LoginFragment
import com.yunjung.todaysrecord.login.LoginFragmentDirections
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class NaverLoginManager(val loginFragment: LoginFragment) {
    private lateinit var naverLoginModule : OAuthLogin // 네이버 로그인 인스턴스
    private lateinit var naverLoginHandler: OAuthLoginHandler // 네이버 로그인 화면의 결과를 처리할 핸들러

    private val context = loginFragment.requireContext()

    init{
        initNaverLoginModule()
        initNaverLoginHandler()
    }

    // 네이버 로그인 인스턴스 초기화
    private fun initNaverLoginModule(){
        naverLoginModule = OAuthLogin.getInstance()
        naverLoginModule.init(
            context,
            "CSPZa2NyPqOC4yAIpynz",
            "YUI_ol1v4w",
            "TodaysRecord"
        )
    }

    // 네이버 로그인 핸들러 초기화
    private fun initNaverLoginHandler(){
        naverLoginHandler = object : OAuthLoginHandler(){
            override fun run(success: Boolean) {
                if(success){ // 로그인을 성공했다면
                    getNaverUserInfo()
                } else{ // 로그인을 실패했다면
                    val errorCode = naverLoginModule.getLastErrorCode(context).code
                    val errorDesc = naverLoginModule.getLastErrorDesc(context)
                    Log.e(ContentValues.TAG, "errorCode : $errorCode, errorDesc : $errorDesc")
                }
            }
        }
    }

    // 네이버 로그인 버튼에 핸들러 부착
    fun setNaverLoginBtn(naverLogin : OAuthLoginButton){
        naverLogin.setOAuthLoginHandler(naverLoginHandler)
    }

    // 로그인된 계정의 임시 토큰을 이용하여 사용자의 계정 정보를 가져옴
    private fun getNaverUserInfo(){
        val token = naverLoginModule.getAccessToken(context)
        val url = "https://openapi.naver.com/v1/nid/me"

        loginFragment.lifecycleScope.launch {
            val response = withContext(Dispatchers.IO){
                // 사용자에 대한 정보를 JSON String 타입으로 반환
                naverLoginModule.requestApi(context, token, url)
            }
            // JSON String 타입을 JSON Object로 변환
            val userInfo = JSONObject(response)

            // JSON Object 타입에서 필요한 정보를 가져옴
            val email = userInfo.getJSONObject("response").getString("email")
            val profileImage = userInfo.getJSONObject("response").getString("profile_image")
            todaysRecordLoginOrSignIn(email, profileImage)
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
                Toast.makeText(context, "성공적으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                findNavController(loginFragment).navigateUp()
            }else{ // 가입되어 있지 않은 이메일이라면 회원가입처리
                // 회원가입 화면으로 이동
                val direction = LoginFragmentDirections.actionLoginFragmentToJoinMembershipFragment(email, profileImage)
                findNavController(loginFragment).navigate(direction)
            }
        }
    }
}