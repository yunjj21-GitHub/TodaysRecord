package com.yunjung.todays_record.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yunjung.todays_record.R
import com.yunjung.todays_record.databinding.FragmentImageBinding

class ImageFragment : Fragment(){
    lateinit var binding : FragmentImageBinding
    lateinit var viewModel: ImageViewModel

    companion object{
        fun newInstance() : ImageFragment {
            return ImageFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        binding.viewModel = viewModel
    }
}