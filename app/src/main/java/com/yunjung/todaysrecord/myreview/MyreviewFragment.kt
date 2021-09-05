package com.yunjung.todaysrecord.myreview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentMyreviewBinding

class MyreviewFragment : Fragment(){
    lateinit var binding : FragmentMyreviewBinding
    lateinit var viewModel: MyreviewViewModel

    companion object{
        fun newInstance() : MyreviewFragment {
            return MyreviewFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_myreview, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MyreviewViewModel::class.java)
        binding.viewModel = viewModel
    }
}