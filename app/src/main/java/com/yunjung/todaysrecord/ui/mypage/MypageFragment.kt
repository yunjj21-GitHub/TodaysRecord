package com.yunjung.todaysrecord.ui.mypage

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
        displayUserProfile()

        // 내 정보 수정 버튼 이벤트 설정
        initEditProfileBtn()

        // 관심목록 버튼 클릭 이벤트 설정
        initInterestsBtn()

        // 리뷰관리 버튼 클릭 이벤트 설정
        initReviewBtn()

        // 로그아웃 버튼 클릭 이벤트 설정
        initLogoutBtn()
    }

    private fun displayUserProfile(){
        Glide.with(binding.root.context)
            .load(viewModel.user.value!!.profileImage)
            .circleCrop()
            .fallback(R.drawable.ic_profile)
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
                User("anonymous", null, null, null) // 로그아웃 처리
            saveAutoLoginAndSetAreaInfo()
            Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_mypageFragment_to_startActivity)
        }
    }

    private fun saveAutoLoginAndSetAreaInfo(){
        val autoLoginAndSetArea: SharedPreferences =
            requireContext().getSharedPreferences("autoLoginAndSetArea", Activity.MODE_PRIVATE)
        with(autoLoginAndSetArea.edit()) {
            remove("userId")
            remove("userArea")
            commit()
        }
    }
}