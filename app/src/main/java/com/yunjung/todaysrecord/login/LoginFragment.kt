package com.yunjung.todaysrecord.login

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentLoginBinding
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.*
import org.json.JSONObject

class LoginFragment : Fragment() {
    // DataBinding & ViewModel 관련 변수
    lateinit var binding : FragmentLoginBinding
    lateinit var viewModel: LoginViewModel

    // Firebase 구글 로그인 관련 변수
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    // 네이버 로그인 관련 변수
    private lateinit var naverLoginModule : OAuthLogin // 네이버 로그인 인스턴스
    private lateinit var naverLoginHandler: OAuthLoginHandler // 네이버 로그인 화면의 결과를 처리할 핸들러

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

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

        /* 구글 로그인 관련 */
        // googleSignInClient, auth 객체 초기화
        initGoogleSignInClientAndAuth()

        // 구글 로그인 화면의 결과를 처리하는 런처 초기화
        initActivityResultLauncher()

        /* 네이버 로그인 관련 */
        initNaverLoginModule() // 네이버 로그인 인스턴스 초기화
        initNaverLoginHandler() // 네이버 로그인 핸들러 초기화

        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel

        // googleSignInBtn 클릭 이벤트 설정
        initGoogleSignInBtn()

        // naverSignInBtn에 핸들러 부착
        initNaverLoginBtn()
    }

    /* 구글 로그인 관련 */
    private fun initGoogleSignInClientAndAuth(){
        // GoogleSignInClient 객체 얻기
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken((getString(R.string.default_web_client_id)))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)

        // FirebaseAuth 객체 얻기
        auth = FirebaseAuth.getInstance()
    }

    private fun initActivityResultLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result : ActivityResult ->
            if(result.resultCode == Activity.RESULT_OK){ // 구글 로그인 화면의 결과를 처리
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account!!)
                } catch (e: ApiException) {
                    Log.e(TAG, e.toString())
                }
            }
        }
    }

    private fun initGoogleSignInBtn(){
        binding.googleSignInBtn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        // 구글 로그인 화면을 띄움
        val signInIntent = googleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) { // 구글 로그인이 성공했다면
                    todaysRecordLoginOrSignIn(acct.email, acct.photoUrl.toString())
                } else { // 구글 로그인이 실패했다면
                    Toast.makeText(context, "로그인을 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /* 네이버 로그인 관련 */
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
                    Log.e(TAG, "errorCode : $errorCode, errorDesc : $errorDesc")
                }
            }
        }
    }

    // 네이버 로그인 버튼에 핸들러 부착
    private fun initNaverLoginBtn(){
        binding.naverLoginBtn.setOAuthLoginHandler(naverLoginHandler)
    }

    // 로그인된 계정의 임시 토큰을 이용하여 사용자의 계정 정보를 가져옴
    private fun getNaverUserInfo(){
        val token = naverLoginModule.getAccessToken(context)
        val url = "https://openapi.naver.com/v1/nid/me"

        lifecycleScope.launch {
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
        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO){
                try{
                    RetrofitManager.service.checkIfEmailAlreadySingedUp(email)
                }catch (e : Throwable){
                    null
                }
            }
            if(response != null){ // 가입되어 있는 이메일이라면 로그인처리
                (requireContext().applicationContext as MyApplication).user.value = response // 로그인
                Toast.makeText(context, "성공적으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }else{ // 가입되어 있지 않은 이메일이라면 회원가입처리
                // 회원가입 화면으로 이동
                val direction = LoginFragmentDirections.actionLoginFragmentToJoinMembershipFragment(email, profileImage)
                findNavController().navigate(direction)
            }
        }
    }
}