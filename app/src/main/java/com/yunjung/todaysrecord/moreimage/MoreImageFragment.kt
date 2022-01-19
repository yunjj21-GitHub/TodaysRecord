package com.yunjung.todaysrecord.moreimage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentMoreImageBinding
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.recyclerview.ImageAdapter

class MoreImageFragment : Fragment(){
    // DataBinding & ViewModel 관련 변수
    lateinit var binding : FragmentMoreImageBinding
    lateinit var viewModel: MoreImageViewModel

    // Navigation component safe args
    private val args : MoreImageFragmentArgs by navArgs()

    companion object{
        fun newInstance() : MoreImageFragment {
            return MoreImageFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more_image, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MoreImageViewModel::class.java)
        binding.viewModel = viewModel

        // photoStudio 업데이트
        viewModel.updatePhotoStudio(args.photoStudio!!)

        // imageReviewList 업데이트
        viewModel.getImageReviewList()

        // 리사이클러뷰 관련 설정
        initRecycler()
        subscribeStudioList()
    }

    // 리사이클러뷰에 어댑터 설정
    private fun initRecycler(){
        binding.recyclerImage.adapter = ImageAdapter()
        binding.recyclerImage.layoutManager = GridLayoutManager(requireContext(), 3)

    }

    // 어댑터가 imageReviewList를 옵저버
    private fun subscribeStudioList() {
        viewModel.imageReviewList.observe(viewLifecycleOwner, {
            (binding.recyclerImage.adapter as ImageAdapter).submitList(it)
        })
    }
}