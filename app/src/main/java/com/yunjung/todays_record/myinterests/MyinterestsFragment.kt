package com.yunjung.todays_record.myinterests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yunjung.todays_record.R
import com.yunjung.todays_record.databinding.FragmentMyinterestsBinding

class MyinterestsFragment: Fragment(){
    lateinit var binding : FragmentMyinterestsBinding
    lateinit var viewModel: MyinterestsViewModel

    companion object{
        fun newInstance() : MyinterestsFragment {
            return MyinterestsFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_myinterests, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MyinterestsViewModel::class.java)
        binding.viewModel = viewModel
    }
}