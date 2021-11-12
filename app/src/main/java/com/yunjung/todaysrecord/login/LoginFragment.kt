package com.yunjung.todaysrecord.login

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.yunjung.todaysrecord.MainViewModel
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentLoginBinding
import com.yunjung.todaysrecord.main.MainActivity
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import retrofit2.Call
import retrofit2.Response

class LoginFragment : Fragment() {
    lateinit var binding : FragmentLoginBinding
    lateinit var viewModel: LoginViewModel

    // FireBase 구글 로그인 연동 관련
    private lateinit var auth: FirebaseAuth // Firebase 인증 객체
    private lateinit var googleSignInClient : GoogleSignInClient // 구글 API 클라이언트 객체

    companion object{
        fun newInstance() : LoginFragment{
            return LoginFragment()
        }

        const val RC_SIGN_IN = 1903 // 구글 로그인 결과 코드
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        // 구글 Sing In 버튼 동작의 기본적인 옵션을 설정
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // ? 좀 다름
        googleSignInClient = GoogleSignIn.getClient(context, gso)

        // Firebase 인증 객체 초기화
        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel

        // 로그인 버튼 클릭 이벤트 설정
        binding.signInBtn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent // 구글이 인증한 액티비티 화면
        startActivityForResult(signInIntent, RC_SIGN_IN) // 인증을 거친 후 결과를 돌려 받음
    }

    // 구글 로그인 인증을 요청했을 때 결과 값을 되돌려 받는 곳
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) { // RC_SIGN_IN 코드를 통해서 되돌아 온것인지 확인
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try { // 인증 결과가 성공이라면
                val account = task.getResult(ApiException::class.java)!! // 구글 로그인 정보를 담고 있음
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) { // 인증 결과가 실패라면

            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) { // 구글 로그인이 성공했다면

                    // 이미 가입된 계정인지 확인
                    val call : Call<List<User>> = RetrofitManager.iRetrofit.checkIfEmailAlreadySingedUp(email = acct.email)
                    call.enqueue(object : retrofit2.Callback<List<User>>{

                        // 서버 응답 성공시
                        override fun onResponse(
                            call: Call<List<User>>,
                            response: Response<List<User>>
                        ) {

                            if(!response.body().isNullOrEmpty()){ // 이미 가입된 계정이라면
                                Toast.makeText(context, "성공적으로 로그인 되었습니다.", Toast.LENGTH_SHORT)
                                (requireActivity() as MainActivity).viewModel.updateUserId(response.body()!![0]._id.toString())
                                findNavController().navigateUp()
                            }else{ // 아직 가입되지 않은 계정이라면
                                // 회원가입 화면으로 이동
                                val direction = LoginFragmentDirections.actionLoginFragmentToJoinMembershipFragment(acct.email, acct.photoUrl.toString())
                                findNavController().navigate(direction)
                            }
                        }

                        // 서버 응답 실패시
                        override fun onFailure(call: Call<List<User>>, t: Throwable) {
                            Log.e(ContentValues.TAG, t.localizedMessage)
                        }
                    })
                } else { // 구글 로그인이 실패했다면
                    Toast.makeText(context, "로그인을 실패하였습니다.", Toast.LENGTH_SHORT)
                }
            }
    }
}