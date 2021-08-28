package com.yunjung.todays_record.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yunjung.todays_record.R
import com.yunjung.todays_record.databinding.FragmentMypageBinding

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

        // 내 정보 수정 버튼 이벤트 설정
        binding.editProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mypageFragment_to_editFragment)
        }

        // 관심목록 버튼 이벤트 설정
        binding.interestsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mypageFragment_to_myinterestsFragment)
        }

        // 리뷰관리 버튼 이벤트 설정
        binding.reviewBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mypageFragment_to_myreviewFragment)
        }
    }
}