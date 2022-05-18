package com.yunjung.todaysrecord.ui.consent_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentConsentDetailBinding

class ConsentDetailFragment : Fragment() {
    lateinit var binding : FragmentConsentDetailBinding
    lateinit var viewModel : ConsentDetailViewModel

    private val args : ConsentDetailFragmentArgs by navArgs()

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_consent_detail, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성되었을 때 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ConsentDetailViewModel::class.java)
        binding.viewModel = viewModel

        // 어느 약관의 상세 페이지인지 확인
        val name = args.name

        if(name == "First"){
            binding.Title.setText(R.string.first_consent_title)
            binding.content.setText(R.string.first_consent_content)
        }else{ // name == "Second"
            binding.Title.setText(R.string.second_consent_title)
            binding.content.setText(R.string.second_consent_content)
        }
    }
}