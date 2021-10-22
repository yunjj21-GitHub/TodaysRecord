package com.yunjung.todaysrecord.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentReviewBinding
import com.yunjung.todaysrecord.detail.DetailFragmentArgs
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.recyclerview.ReviewAdapter

class ReviewFragment : Fragment() {
    lateinit var binding: FragmentReviewBinding
    lateinit var viewModel: ReviewViewModel

    companion object {
        // Fragment가 생성될 때 DetailFragmentArgs 객체를 전달 받음
        lateinit var photoStudio: PhotoStudio
        fun newInstance(args : DetailFragmentArgs): ReviewFragment {
            photoStudio = args.photoStudio!!
            return ReviewFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)
        binding.viewModel = viewModel

        // 리사이클러뷰 적용
        initRecycler()
        subscribeStudioList()

        // '리뷰 작성하기' 버튼 클릭 이벤트 설정
        binding.writeReviewBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_global_writeReivewFragment)
        }

        // '사진 더 보기' 버튼 클릭 이벤트 설정
        binding.moreImageBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_global_imageFragment)
        }
    }

    // 리사이클러뷰 초기설정
    private fun initRecycler(){
        binding.recyclerViewReview.adapter = ReviewAdapter()
        binding.recyclerViewReview.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    // 리사이클러뷰에 보여지는 데이터가 변경시 어댑터에게 알림
    private fun subscribeStudioList() {
        viewModel.reviewList.observe(viewLifecycleOwner, {
            (binding.recyclerViewReview.adapter as ReviewAdapter).submitList(it)
        })
    }
}