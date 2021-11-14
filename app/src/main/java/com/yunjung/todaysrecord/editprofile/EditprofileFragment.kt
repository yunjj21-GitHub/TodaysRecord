package com.yunjung.todaysrecord.editprofile

import android.content.ContentValues
import android.content.ContentValues.TAG
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
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentEditprofileBinding
import com.yunjung.todaysrecord.detail.DetailFragmentArgs
import com.yunjung.todaysrecord.main.MainActivity
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import retrofit2.Call
import retrofit2.Response

class EditprofileFragment : Fragment(){
    lateinit var binding : FragmentEditprofileBinding
    lateinit var viewModel: EditprofileViewModel

    // Navigaion component safe args 관련 변수
    val args : EditprofileFragmentArgs by navArgs()

    companion object{
        fun newInstance() : EditprofileFragment {
            return EditprofileFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_editprofile, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId : String = (requireContext().applicationContext as MyApplication).userId.value.toString() // 로그인된 user의 _id를 가져옴

        viewModel = ViewModelProvider(this).get(EditprofileViewModel::class.java)

        // viewModel 업데이트
        viewModel.updateUserNickname(args.userNickname)

        // userProfile 이미지 디스플레이
        Glide.with(binding.root.context)
            .load(args.userProfile)
            .fallback(R.drawable.ic_profile)
            .into(binding.userProfile)

        binding.viewModel = viewModel

        // 완료버튼 이벤트 설정
        binding.finishBtn.setOnClickListener {
            // editText의 입력값을 받아옴
            val newUserNickname : String = binding.editTextUserNickname.text.toString()

            // 로그인된 userId와 일치하는 _id을 가진 User의 nickname을 변경
            val call : Call<User> = RetrofitManager.iRetrofit.patchUserNicknameById(_id = userId, nickname = newUserNickname)
            call.enqueue(object : retrofit2.Callback<User>{
                // 응답 성공시
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    Log.e(TAG, "수정 완료")
                }

                // 응답 실패시
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.localizedMessage)
                }
            })

            findNavController().navigateUp() // 뒤로감
        }
    }
}
