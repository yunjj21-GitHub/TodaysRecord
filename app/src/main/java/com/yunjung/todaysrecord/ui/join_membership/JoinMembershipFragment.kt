package com.yunjung.todaysrecord.ui.join_membership

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import com.yunjung.todaysrecord.ui.writereivew.WriteReivewFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.io.ByteArrayOutputStream
import java.io.InputStream

class JoinMembershipFragment : Fragment(){
    lateinit var binding : FragmentJoinMembershipBinding
    lateinit var viewModel: JoinMembershipViewModel

    private val args : JoinMembershipFragmentArgs by navArgs()

    // 사진 업로드 관련
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>

    var newProfileImageBitmap : Bitmap? = null

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

        initActivityResultLauncher() // 이미지를 얻어오는 화면의 결과를 처리하는 런처 초기화

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
            Glide.with(binding.root.context)
                .load(viewModel.userProfileImg.value)
                .circleCrop()
                .into(binding.userProfile)
        }

        // 다양한 에디트 텍스트 값 변경 이벤트 설정
        setEditTextValueChangeEvent()

        // 중복확인 버큰 클릭 이벤트 설정
        initUserIdVerificationBtn()

        // 회원가입 완료 버튼 클릭 이벤트 설정
        initFinishBtn()

        // 프로필 이미지 변경 버튼 클릭 이벤트 설정
        initChangeProfileImgBtn()
    }

    // 프로필 이미지 변경 버튼 클릭 이벤트 설정
    private fun initChangeProfileImgBtn(){
        binding.changeProfileImgBtn.setOnClickListener {
            getPicture() // 디바이스의 갤러리에서 이미지를 얻어옴
        }
    }

    private fun initActivityResultLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri : Uri? ->
            // 새로 선택한 프로필 이미지로 bitmap 생성
            val inputStream : InputStream = requireActivity().contentResolver.openInputStream(uri!!)!!
            var bitmap : Bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap = Bitmap.createScaledBitmap(bitmap,500,500,true) // bitmap 리사이즈

            // 새로 선택한 이미지 임시 저장
            newProfileImageBitmap = bitmap

            // 새로 선택한 이미지 디스플레이
            Glide.with(binding.root.context)
                .load(newProfileImageBitmap)
                .circleCrop()
                .into(binding.userProfile)
        }
    }

    private fun getPicture(){
        activityResultLauncher.launch("image/*") // 이미지를 얻어옴
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

    // 회원가입 완료 버튼 클릭 이벤트 설정
    private fun initFinishBtn(){
        binding.finishBtn.setOnClickListener {
            // 사용가능한 이메일인지 확인
            if(checkUserEmailInputValid() && checkUserPwdInputValid() && checkUserNicknameInputValid()) {
                // 사용자의 입력값을 받아옴
                val email: String = binding.userid.text.toString()
                val nickname: String = binding.userNickname.text.toString()
                val password : String = binding.userPwd.text.toString()
                var profileImage: String = getProfileImage()

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

    // 회원가입을 완료할 프로필 이미지를 가져온다.
    private fun getProfileImage() : String {
        if(newProfileImageBitmap == null) { // 새로 선택된 이미지가 없다면
            return viewModel.userProfileImg.value.toString()
        }else { // 새로 선택된 이미지가 있다면
            // 업로드할 이미지명
            val profileImageName =
                "anonymous" + System.currentTimeMillis().toString() + ".jpg"
            // bitmap으로 MultipartBody.Part 생성
            val bitmapRequestBody =
                newProfileImageBitmap.let { WriteReivewFragment.BitmapRequestBody(it !!) }
            val bitmapMultipartBody = MultipartBody.Part.createFormData(
                "profileImage" ,
                profileImageName ,
                bitmapRequestBody
            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    // 서버에 선택된 이미지 업로드
                    RetrofitManager.service.profileImageUpload(bitmapMultipartBody)
                }
            }

            return "http://13.209.25.227/$profileImageName"
        }
    }
}