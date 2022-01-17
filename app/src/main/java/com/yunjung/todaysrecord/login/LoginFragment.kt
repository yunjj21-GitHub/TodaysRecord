package com.yunjung.todaysrecord.login

import android.app.Activity
import android.content.ContentValues
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
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentLoginBinding
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import retrofit2.Call
import retrofit2.Response

class LoginFragment : Fragment() {
    // DataBinding & ViewModel 관련 변수
    lateinit var binding : FragmentLoginBinding
    lateinit var viewModel: LoginViewModel

    // Firebase 구글 로그인 관련 변수
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

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

        // googleSignInClient, auth 객체 초기화
        initGoogleSignInClientAndAuth()

        // 구글 로그인 화면의 결과를 처리하는 런처 초기화
        initActivityResultLauncher()

        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel

        // googleSignInBtn 클릭 이벤트 설정
        initGoogleSignInBtn()
    }

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
                    todaysRecordSignUp(acct)
                } else { // 구글 로그인이 실패했다면
                    Toast.makeText(context, "로그인을 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun todaysRecordSignUp(acct : GoogleSignInAccount){
        // 가입된 이메일인지 확인
        val call : Call<User> = RetrofitManager.iRetrofit.checkIfEmailAlreadySingedUp(email = acct.email)
        call.enqueue(object : retrofit2.Callback<User>{
            // 서버 응답 성공시
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                if(response.body() != null) { // 가입되어 있는 이메일이라면 로그인처리
                    (requireContext().applicationContext as MyApplication).user.value = response.body()!! // 로그인
                    Toast.makeText(context, "성공적으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }else{ // 가입되어 있지 않은 이메일이라면 회원가입처리
                    // 회원가입 화면으로 이동
                    val direction = LoginFragmentDirections.actionLoginFragmentToJoinMembershipFragment(acct.email, acct.photoUrl.toString())
                    findNavController().navigate(direction)
                }
            }

            // 서버 응답 실패시
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e(TAG, "여기서 에러")
                Log.e(TAG, t.localizedMessage)
            }
        })
    }
}