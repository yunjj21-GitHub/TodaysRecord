package com.yunjung.todaysrecord.join_membership

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentJoinMembershipBinding
import com.yunjung.todaysrecord.main.MainActivity
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import retrofit2.Call
import retrofit2.Response

class JoinMembershipFragment : Fragment(){
    lateinit var binding : FragmentJoinMembershipBinding
    lateinit var viewModel: JoinMembershipViewModel

    val args : JoinMembershipFragmentArgs by navArgs()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(JoinMembershipViewModel::class.java)

        binding.viewModel = viewModel

        // userProfile 이미지 디스플레이
        Glide.with(binding.root.context)
            .load(args.profileImage)
            .fallback(R.drawable.ic_profile)
            .into(binding.userProfile)

        // 입력된 정보를 바탕으로 회원가입 완료
        binding.finishBtn.setOnClickListener {
            val email : String = args.email
            val profileImage : String = args.profileImage
            val nickname : String = binding.userNickname.text.toString()

            // 서버에 입력된 정보를 넘겨줌
            val call : Call<User> = RetrofitManager.iRetrofit?.postUser(email = email, profileImage = profileImage, nickname = nickname)
            call?.enqueue(object : retrofit2.Callback<User>{
                // 응답 성공시
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    // MainViewModel의 userId 업데이트 (어플 내 로그인 처리)
                    // (requireActivity() as MainActivity).viewModel.updateUserId(response.body()!!._id.toString())

                    // myPageFragment로 이동
                    findNavController().navigate(JoinMembershipFragmentDirections.actionJoinMembershipFragmentToMypageFragment())
                }

                // 응답 실패시
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.localizedMessage)
                }
            })
        }
    }
}