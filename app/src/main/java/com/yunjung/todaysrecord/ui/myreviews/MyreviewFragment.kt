package com.yunjung.todaysrecord.ui.myreviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentMyreviewsBinding
import com.yunjung.todaysrecord.recyclerview.ReviewAdapter

class MyreviewFragment : Fragment(){
    lateinit var binding : FragmentMyreviewsBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_myreviews, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyreviewViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // 실시간으로 변하는 값 옵저버 (reviewNum)
        initObserver()

        // user 업데이트
        viewModel.updateUser((requireContext().applicationContext as MyApplication).user.value!!)

        // Review의 userId가 로그인된 userId와 일치하는 것을 가져옴
        viewModel.updateReviewList()

        initRecycler() // 리사이클러뷰에 어댑터 부착
        subscribeStudioList() // 어댑터가 reviewList 옵저버
    }

    // 실시간으로 변하는 값 옵저버
    private fun initObserver(){
        viewModel.reviewNum.observe(viewLifecycleOwner, Observer {
            binding.reviewNum.text = it.toString()
        })
    }

    // 리사이클러뷰 초기설정
    private fun initRecycler(){
        binding.recyclerView.adapter = ReviewAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    // 리사이클러뷰에 보여지는 데이터가 변경시 어댑터에게 알림
    private fun subscribeStudioList() {
        viewModel.reviewList.observe(viewLifecycleOwner, {
            (binding.recyclerView.adapter as ReviewAdapter).submitList(it)
        })
    }
}