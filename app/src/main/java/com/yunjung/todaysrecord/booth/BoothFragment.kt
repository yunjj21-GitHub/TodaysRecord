package com.yunjung.todaysrecord.booth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentBoothBinding

class BoothFragment : Fragment(){
    lateinit var binding : FragmentBoothBinding
    lateinit var viewModel: BoothViewModel

    companion object{
        fun newInstance() : BoothFragment{
            return BoothFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booth, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(BoothViewModel::class.java)
        binding.viewModel = viewModel
    }
}