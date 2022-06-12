package com.yunjung.todaysrecord.ui.review

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentReviewBinding
import com.yunjung.todaysrecord.ui.detail.DetailFragmentArgs
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.ui.moreimage.MoreImageFragmentDirections
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.recyclerview.ReviewAdapter
import com.yunjung.todaysrecord.ui.writereivew.WriteReivewFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewFragment : Fragment() {
    lateinit var binding: FragmentReviewBinding
    lateinit var viewModel: ReviewViewModel

    companion object {
        private lateinit var photoStudio: PhotoStudio
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

    // 뷰가 완전히 생성 되었을 때 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)
        binding.lifecycleOwner = this

        initObserver()

        // user 업데이트
        viewModel.updateUser((requireContext().applicationContext as MyApplication).user.value!!)

        // photoStudio 업데이트
        viewModel.updatePhotoStudio(photoStudio)

        // reviewList 업데이트
        viewModel.updateReviewList()

        // phtoReviewList 업데이트
        viewModel.updatePhotoReviewList()

        // 사진리뷰 프리뷰 화면 초기설정
        initImageReviewPreview()

        // 리사이클러뷰 초기 설정
        initRecycler()
        subscribeStudioList()

        // moreImageBtn 클릭 이벤트 설정
        initMoreImageBtn()

        // writeReviewBtn 클릭 이벤트 설정
        initWriteReviewBtn()
    }

    // 실시간으로 변하는 값 옵저버
    private fun initObserver(){
        viewModel.reviewNum.observe(viewLifecycleOwner, Observer {
            binding.reviewNum.text = it.toString()
        })

        viewModel.reviewAvg.observe(viewLifecycleOwner, Observer {
            binding.reviewAvg.text = it.toString()
            binding.ratingBar.rating = it.toFloat()
        })

        viewModel.starRatio.observe(viewLifecycleOwner, Observer {
            binding.fiveStar.progress = it[0]
            binding.fourStar.progress = it[1]
            binding.threeStar.progress = it[2]
            binding.twoStar.progress = it[3]
            binding.oneStar.progress = it[4]
        })
    }

    // 리사이클러뷰에 어댑터를 부착
    private fun initRecycler(){
        binding.recyclerViewReview.adapter = ReviewAdapter(parentFragmentManager, this)
        binding.recyclerViewReview.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    // 어댑터가 reviewList를 옵저버
    private fun subscribeStudioList() {
        viewModel.reviewList.observe(viewLifecycleOwner, {
            (binding.recyclerViewReview.adapter as ReviewAdapter).submitList(it)
        })
    }

    private fun initMoreImageBtn(){
        binding.moreImageBtn.setOnClickListener {
            val direction = MoreImageFragmentDirections.actionGlobalMoreimageFragment(
                photoStudio)
            findNavController().navigate(direction)
        }
    }

    private fun initWriteReviewBtn(){
        binding.writeReviewBtn.setOnClickListener {
            val directions = WriteReivewFragmentDirections.actionGlobalWriteReivewFragment(photoStudio._id.toString())
            it.findNavController().navigate(directions)
        }
    }

    private fun initImageReviewPreview(){
        viewModel.photoReviewList.observe(viewLifecycleOwner, {
            Log.e(TAG, "실행")
            if(it.isNotEmpty()){
                val preImage1 = it[0].image.toString()
                Glide.with(binding.root.context)
                    .load(preImage1)
                    .into(binding.preImageView1)
            }
            if(it.size >= 2){
                val preImage2 = it[1].image.toString()
                Glide.with(binding.root.context)
                    .load(preImage2)
                    .into(binding.preImageView2)
            }
        })
    }
}