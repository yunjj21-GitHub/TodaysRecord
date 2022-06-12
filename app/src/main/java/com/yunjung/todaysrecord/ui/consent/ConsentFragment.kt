package com.yunjung.todaysrecord.ui.consent

import android.graphics.Color
import android.graphics.ColorSpace
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentConsentBinding

class ConsentFragment : Fragment(){
    lateinit var binding : FragmentConsentBinding
    lateinit var viewModel : ConsentViewModel

    private val args : ConsentFragmentArgs by navArgs()

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_consent, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성되었을 때 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        viewModel = ViewModelProvider(this).get(ConsentViewModel::class.java)
        binding.viewModel = viewModel

        val isFirstConsentChecked: MutableLiveData<Boolean> = MutableLiveData(false)
        val isSecondConsentChecked: MutableLiveData<Boolean> = MutableLiveData(false)

        // '오늘의 기록 이용약관 동의 (필수)' 체크박스 클릭 이벤트 설정
        binding.firstConsent.setOnCheckedChangeListener { view , isChecked ->
            isFirstConsentChecked.value = isChecked
        }

        // '개인정보 수집이용 동의 (필수)' 체크박스 클릭 이벤트 설정
        binding.secondConsent.setOnCheckedChangeListener { view , isChecked ->
            isSecondConsentChecked.value = isChecked
        }

        // '전체동의' 체크박스 클릭 이벤트 설정
        binding.allConsent.setOnClickListener {
            val isAllConsentChecked = binding.allConsent.isChecked

            binding.firstConsent.isChecked = isAllConsentChecked
            binding.secondConsent.isChecked = isAllConsentChecked

            isFirstConsentChecked.value = isAllConsentChecked
            isSecondConsentChecked.value = isAllConsentChecked
        }

        isFirstConsentChecked.observe(viewLifecycleOwner, Observer {
            if(isFirstConsentChecked.value == true
                && isSecondConsentChecked.value == true) {
                binding.allConsent.isChecked = true
                binding.nextStepBtn.setBackgroundColor(Color.parseColor("#512771"))
                binding.nextStepBtn.isClickable = true
            }else{
                binding.allConsent.isChecked = false
                binding.nextStepBtn.setBackgroundColor(Color.parseColor("#AAAAAA"))
                binding.nextStepBtn.isClickable = false // 아무런 동작을 하지 않음
            }
        })

        isSecondConsentChecked.observe(viewLifecycleOwner, Observer {
            if(isFirstConsentChecked.value == true
                && isSecondConsentChecked.value == true) {
                binding.allConsent.isChecked = true
                binding.nextStepBtn.setBackgroundColor(Color.parseColor("#512771"))
                binding.nextStepBtn.isClickable = true
            }else{
                binding.allConsent.isChecked = false
                binding.nextStepBtn.setBackgroundColor(Color.parseColor("#AAAAAA"))
                binding.nextStepBtn.isClickable = false // 아무런 동작을 하지 않음
            }
        })

        // '다음' 버튼 클릭 이벤트 설정
        binding.nextStepBtn.setOnClickListener {
            // 회원가입 화면으로 이동
            val direction = ConsentFragmentDirections.actionConsentFragmentToJoinMembershipFragment(args.email, args.profileImage)
            findNavController().navigate(direction)
        }

        // 각각의 약관 더보기 클릭 이벤트 설정
        binding.moreFirstConsent.setOnClickListener {
            // 약관 더보기 화면으로 이동
            val direction = ConsentFragmentDirections.actionConsentFragmentToConsentDetailFragment("First")
            findNavController().navigate(direction)
        }

        binding.moreSecondConsent.setOnClickListener {
            // 약관 더보기 화면으로 이동
            val direction = ConsentFragmentDirections.actionConsentFragmentToConsentDetailFragment("Second")
            findNavController().navigate(direction)
        }
    }
}