package com.yunjung.todaysrecord.writereivew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentWriteReviewBinding

class WriteReivewFragment : Fragment() {
    lateinit var binding : FragmentWriteReviewBinding
    lateinit var viewModel : WriteReviewViewModel

    companion object{
        fun  newInstance() : WriteReivewFragment{
            return WriteReivewFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_review, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WriteReviewViewModel::class.java)

        // '등록' 버튼 클릭 이벤트 설정
        binding.finishBtn.setOnClickListener {
            it.findNavController().navigateUp()
        }

        // '취소' 버튼 클릭 이벤트 설정
        binding.cancelBtn.setOnClickListener {
            it.findNavController().navigateUp()
        }
    }
}