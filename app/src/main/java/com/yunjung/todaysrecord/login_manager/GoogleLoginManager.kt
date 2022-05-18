package com.yunjung.todaysrecord.login_manager

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.ui.login.LoginFragmentDirections
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.SharedPreferences

class GoogleLoginManager(val loginFragment : Fragment) {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val context = loginFragment.context

    init {
        initGoogleSignInClientAndAuth()
        initActivityResultLauncher()
    }

    // googleSignInClient, auth 객체 초기화
    private fun initGoogleSignInClientAndAuth(){
        // GoogleSignInClient 객체 얻기
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(loginFragment.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)

        // FirebaseAuth 객체 얻기
        auth = FirebaseAuth.getInstance()
    }

    // 구글 로그인 화면의 결과를 처리하는 런처 초기화
    private fun initActivityResultLauncher(){
        activityResultLauncher = loginFragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result : ActivityResult ->
            if(result.resultCode == Activity.RESULT_OK){ // 구글 로그인 화면의 결과를 처리
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account!!)
                } catch (e: ApiException) {
                    Log.e(ContentValues.TAG, e.toString())
                }
            }
        }
    }

    // 구글 로그인 버튼 클릭 이벤트 설정
    fun setGoogleSignInBtn(googleLoginBtn: SignInButton){
        googleLoginBtn.setOnClickListener {
            signIn()
        }
    }

    // 구글 로그인 실행
    private fun signIn() {
        // 구글 로그인 화면을 띄움
        val signInIntent = googleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(loginFragment.requireActivity()) { task ->
                if (task.isSuccessful) { // 구글 로그인이 성공했다면
                    todaysRecordLoginOrSignIn(acct.email, acct.photoUrl.toString())
                } else { // 구글 로그인이 실패했다면
                    Toast.makeText(context, "로그인을 실패하였습니다.", Toast.LENGTH_SHORT).show()
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
                (loginFragment.requireContext().applicationContext as MyApplication).user.value = response // 로그인
                saveAutoLoginInfo(response._id!!) // 추후 자동로그인을 위해 로그인 정보 저장
                Toast.makeText(context, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                findNavController(loginFragment).navigate(R.id.action_loginFragment_to_mainActivity)

                // StartActivity 종료
                loginFragment.requireActivity().finish()
            }else{ // 가입되어 있지 않은 이메일이라면 회원가입처리
                // 약관 동의 화면으로 이동
                val direction = LoginFragmentDirections.actionLoginFragmentToConsentFragment(email, profileImage)
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