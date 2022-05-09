package com.yunjung.todaysrecord.ui.mypage

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentMypageBinding
import com.yunjung.todaysrecord.models.User

class MypageFragment : Fragment(){
    lateinit var binding : FragmentMypageBinding
    lateinit var viewModel: MypageViewModel

    companion object{
        fun newInstance() : MypageFragment {
            return MypageFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MypageViewModel::class.java)
        binding.viewModel = viewModel

        // user 업데이트
        viewModel.updateUser((requireContext().applicationContext as MyApplication).user.value!!)

        // 유저 프로필 이미지 설정
        displayProfileImage()

        // 내 정보 수정 버튼 이벤트 설정
        initEditProfileBtn()

        // 관심목록 버튼 클릭 이벤트 설정
        initInterestsBtn()

        // 리뷰관리 버튼 클릭 이벤트 설정
        initReviewBtn()

        // 로그아웃 버튼 클릭 이벤트 설정
        initLogoutBtn()
    }

    // 프로필 이미지 디스플레이
    private fun displayProfileImage(){
        Glide.with(binding.root.context)
            .load(viewModel.user.value!!.profileImage)
            .fallback(R.drawable.ic_profile)
            .circleCrop()
            .into(binding.userProfile)
    }

    private fun initEditProfileBtn(){
        binding.editProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mypageFragment_to_editFragment)
        }
    }

    private fun initInterestsBtn(){
        binding.interestsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mypageFragment_to_myinterestsFragment)
        }
    }

    private fun initReviewBtn(){
        binding.reviewBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mypageFragment_to_myreviewFragment)
        }
    }

    private fun initLogoutBtn(){
        binding.logoutBtn.setOnClickListener {
            (requireContext().applicationContext as MyApplication).user.value=
                User(null, null, null, null) // 로그아웃 처리
            removeAutoLoginAndSetAreaInfo()
            Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_mypageFragment_to_startActivity)

            // MainActivity 종료
            requireActivity().finish()
        }
    }

    // 자동 로그인 & 자동 지역설정 정보 초기화
    private fun removeAutoLoginAndSetAreaInfo(){
        // 자동 로그인 정보 초기화
        val autoLogin: SharedPreferences =
            requireContext().getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        with(autoLogin.edit()) {
            remove("userId")
            commit()
        }

        // 자동 지역설정 정보 초기화
        val autoSetArea: SharedPreferences =
            requireContext().getSharedPreferences("autoSetArea", Activity.MODE_PRIVATE)
        with(autoSetArea.edit()) {
            remove("userArea")
            commit()
        }
    }
}