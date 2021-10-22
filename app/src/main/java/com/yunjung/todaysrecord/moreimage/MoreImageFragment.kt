package com.yunjung.todaysrecord.moreimage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentMoreImageBinding
import com.yunjung.todaysrecord.recyclerview.ImageAdapter
import com.yunjung.todaysrecord.recyclerview.PhotoStudioAdapter
import com.yunjung.todaysrecord.recyclerview.ReviewAdapter

class MoreImageFragment : Fragment(){
    lateinit var binding : FragmentMoreImageBinding
    lateinit var viewModel: MoreImageViewModel

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

        // 리사이클러뷰 적용
        initRecycler()
        subscribeStudioList()
    }

    // 리사이클러뷰 초기설정
    private fun initRecycler(){
        binding.recyclerImage.adapter = ImageAdapter()
        // binding.recyclerImage.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerImage.layoutManager = GridLayoutManager(requireContext(), 3)

    }

    // 리사이클러뷰에 보여지는 데이터가 변경시 어댑터에게 알림
    private fun subscribeStudioList() {
        viewModel.reviewList.observe(viewLifecycleOwner, {
            (binding.recyclerImage.adapter as ImageAdapter).submitList(it)
        })
    }
}