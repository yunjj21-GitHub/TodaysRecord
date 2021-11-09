package com.yunjung.todaysrecord.mypage

import android.content.ContentValues
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
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentMypageBinding
import com.yunjung.todaysrecord.main.MainActivity
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.recyclerview.PhotoStudioAdapter
import retrofit2.Call
import retrofit2.Response

class MypageFragment : Fragment(){
    lateinit var binding : FragmentMypageBinding
    lateinit var viewModel: MypageViewModel

    // var userId : String = (requireActivity() as MainActivity).viewModel.userId.value ?: "anonymous"
    var userId : String = "616be2b08346b820364b82b1"

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

        if(userId != "anonymous"){
            val call : Call<String> = RetrofitManager.iRetrofit?.getUserNicknameById(_id = userId)
            call?.enqueue(object : retrofit2.Callback<String>{
                // 응답 성공시
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    viewModel.updateUserNickname(response.body().toString())
                    binding.viewModel = viewModel
                }

                // 응답 실패시
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.localizedMessage)
                }
            })
        }else{
            viewModel.updateUserNickname("로그인해주세요")
            binding.viewModel = viewModel
        }

        // 내 정보 수정 버튼 이벤트 설정
        binding.editProfileBtn.setOnClickListener {
            if(userId == "anonymous"){ // 로그인이 되어 있지 않을 때
                findNavController().navigate(R.id.action_mypageFragment_to_loginFragment)
            }else { // 로그인이 되어 있을 때
                val direction = MypageFragmentDirections.actionMypageFragmentToEditFragment(viewModel.userNickname.value.toString())
                findNavController().navigate(direction)
            }
        }

        // 관심목록 버튼 이벤트 설정
        binding.interestsBtn.setOnClickListener {
            if(userId == "anonymous"){ // 로그인이 되어 있지 않을 때
                Toast.makeText(context, "먼저 로그인을 해주세요", Toast.LENGTH_LONG)
            }else { // 로그인이 되어 있을 때
                findNavController().navigate(R.id.action_mypageFragment_to_myinterestsFragment)
            }
        }

        // 리뷰관리 버튼 이벤트 설정
        binding.reviewBtn.setOnClickListener {
            if(userId == "anonymous"){ // 로그인이 되어 있지 않을 때
                Toast.makeText(context, "먼저 로그인을 해주세요", Toast.LENGTH_LONG)
            }else { // 로그인이 되어 있을 때
                findNavController().navigate(R.id.action_mypageFragment_to_myreviewFragment)
            }
        }
    }
}