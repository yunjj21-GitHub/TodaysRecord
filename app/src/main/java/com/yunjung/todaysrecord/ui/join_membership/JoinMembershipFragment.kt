package com.yunjung.todaysrecord.ui.join_membership

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.set
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentJoinMembershipBinding
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JoinMembershipFragment : Fragment(){
    lateinit var binding : FragmentJoinMembershipBinding
    lateinit var viewModel: JoinMembershipViewModel

    private val args : JoinMembershipFragmentArgs by navArgs()

    companion object{
        fun newInstance() : JoinMembershipFragment{
            return JoinMembershipFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_join_membership, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성된 후에 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(JoinMembershipViewModel::class.java)
        binding.viewModel = viewModel

        // 유저 아이디 및 프로필 이미지 업데이트
        viewModel.updateUserId(args.email)
        viewModel.updateUserProfileImg(args.profileImage)

        // 유저 아이디 및 프로필 이미지 디스플레이
        if(viewModel.userId.value != null){ // 다양한 로그인 API를 통한 회원가입이라면
            binding.userid.setText(viewModel.userId.value)
            viewModel.updateUserIdValid(true)
            binding.userIdVerificationResult.text = "사용 가능한 이메일 입니다."
        }
        if(viewModel.userProfileImg.value != null) {
            Log.e(TAG, viewModel.userProfileImg.value.toString())
            displayProfileImage()
        }

        // 다양한 에디트 텍스트 값 변경 이벤트 설정
        setEditTextValueChangeEvent()

        // 중복확인 버큰 클릭 이벤트 설정
        initUserIdVerificationBtn()

        // 회원가입 완료 버튼 클릭 이벤트 설정
        initFinishBtn()
    }

    // 다양한 에디트 텍스트 값 변경 이벤트 설정
    private fun setEditTextValueChangeEvent(){
        // 유저 아이디 입력 값이 달라진다면
        binding.userid.doOnTextChanged { text, start, before, count ->
            viewModel.updateUserIdValid(false)
            binding.userIdVerificationResult.text = ""
        }

        // 유저 비밀번호 또는 비밀번호 재입력 값이 달라진다면
        binding.userPwd.doOnTextChanged { text, start, before, count ->
            viewModel.updateUserPwdValid(false)
            binding.userPwdVerificationResult.text = ""
        }

        binding.userPwdVerification.doOnTextChanged { text, start, before, count ->
            viewModel.updateUserPwdValid(false)
            binding.userPwdVerificationResult.text = ""
        }

        // 비밀번호 재입력 값의 입력이 완료 되었다면
        binding.userPwdVerification.doAfterTextChanged {
            // 비밀번호와 비밀번호 재입력 값이 일치하는지 확인
            if(binding.userPwd.text.toString() == binding.userPwdVerification.text.toString()){
                viewModel.updateUserPwdValid(true)
                binding.userPwdVerificationResult.text ="비밀번호가 일치합니다."
            }else{
                binding.userPwdVerificationResult.text ="비밀번호가 일치하지 않습니다."
            }
        }

        // 유저 닉네임 입력 값이 달라진다면
        binding.userNickname.doOnTextChanged { text, start, before, count ->
            binding.userNicknameVerificationResult.text = ""
        }
    }

    // 프로필 이미지 디스플레이
    private fun displayProfileImage(){
        Glide.with(binding.root.context)
            .load(viewModel.userProfileImg.value)
            .fallback(R.drawable.ic_profile)
            .circleCrop()
            .into(binding.userProfile)
    }

    // 중복확인 버튼 클릭 이벤트
    private fun initUserIdVerificationBtn() {
        // 이미 가입된 이메일인지 확인
        binding.userIdValificationBtn.setOnClickListener {
            // 입력된 값이 없다면
            if(binding.userid.text.isBlank()) binding.userIdVerificationResult.text = "이메일을 입력해주세요."
            else {
                lifecycleScope.launch {
                    val response = withContext(Dispatchers.IO){
                        try{
                            RetrofitManager.service.checkIfEmailAlreadySingedUp(binding.userid.text.toString())
                        }catch (e : Throwable){
                            null
                        }
                    }

                    if(response != null){ // 이미 가입된 이메일이라면
                        binding.userIdVerificationResult.text = "이미 가입된 이메일 입니다."
                    }else{ // 가입된적 없는 이메일이라면
                        binding.userIdVerificationResult.text = "사용 가능한 이메일 입니다."
                        viewModel.updateUserIdValid(true)
                    }
                }
            }
        }
    }

    // 회원가입 완료 버튼 클릭 이벤트 설정
    private fun initFinishBtn(){
        binding.finishBtn.setOnClickListener {
            // 사용가능한 이메일인지 확인
            if(checkUserEmailInputValid() && checkUserPwdInputValid() && checkUserNicknameInputValid()) {
                // 사용자의 입력값을 받아옴
                val email: String = binding.userid.text.toString()
                val profileImage: String = viewModel.userProfileImg.value.toString()
                val nickname: String = binding.userNickname.text.toString()
                val password : String = binding.userPwd.text.toString()

                // 유저를 생성
                lifecycleScope.launch {
                    withContext(Dispatchers.IO){
                        RetrofitManager.service.postUser(email = email, profileImage = profileImage, nickname = nickname, pwd = password)
                    }
                    Toast.makeText(context, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    // 로그인 화면으로 이동
                    findNavController().navigateUp()
                }
            }
        }
    }

    // 사용자가 입력한 이메일이 유효한지 확인
    private fun checkUserEmailInputValid() : Boolean{
        if(binding.userid.text.isBlank()) { // 이메일이 입력되었는지 확인
            binding.userIdVerificationResult.text = "이메일을 입력해주세요."
            return false
        } else if(viewModel.userIdValid.value == false){ // 중복확인이 되었는지 확인
            binding.userIdVerificationResult.text = "아이디 중복확인을 해주세요."
            return false
        }

        return true
    }

    // 사용자가 입력한 비밀번호가 유효한지 확인
    private fun checkUserPwdInputValid() : Boolean{
        if(binding.userPwd.text.isBlank()){ // 비밀번호가 입력되었는지 확인
            binding.userPwdVerificationResult.text = "비밀번호를 입력해주세요."
            return false
        }else if(binding.userPwdVerification.text.isBlank()) { // 비밀번호 재입력이 입력되었는지 확인
            binding.userPwdVerificationResult.text = "비밀번호를 재입력해주세요."
            return false
        } else if(viewModel.userPwdValid.value == false){
            // 비밀번호와 비밀번호 재입력 값이 일치하지 않는다면
            return false
        }

        return true
    }

    // 사용자가 입력한 닉네임이 유효한지 확인
    private fun checkUserNicknameInputValid() : Boolean {
        if (binding.userNickname.text.isBlank()) {
            binding.userNicknameVerificationResult.text = "닉네임을 입력해주세요."
            return false
        }
        return true
    }
}