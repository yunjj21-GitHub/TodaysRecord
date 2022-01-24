package com.yunjung.todaysrecord.join_membership

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

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

        // user 업데이트
        viewModel.updateUser(args.email, args.profileImage)

        displayProfileImage()

        // finishBtn 클릭 이벤트 설정
        initFinishBtn()
    }

    private fun displayProfileImage(){
        // profile 이미지 디스플레이
        Glide.with(binding.root.context)
            .load(viewModel.user.value?.profileImage)
            .circleCrop()
            .fallback(R.drawable.ic_profile)
            .into(binding.userProfile)
    }

    private fun initFinishBtn(){
        binding.finishBtn.setOnClickListener {
            val email: String = viewModel.user.value!!.email.toString()
            val profileImage: String = viewModel.user.value!!.profileImage.toString()
            // 유저의 입력값을 받아옴
            val nickname: String = binding.userNickname.text.toString()

            // 유저를 생성
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO){
                    RetrofitManager.service.postUser(email = email, profileImage = profileImage, nickname = nickname)
                }
                // 로그인 처리
                (requireContext().applicationContext as MyApplication).user.value = response
                // myPageFragment로 이동
                findNavController().navigate(JoinMembershipFragmentDirections.actionJoinMembershipFragmentToMypageFragment())
            }
        }
    }
}