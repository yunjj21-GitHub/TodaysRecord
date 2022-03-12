package com.yunjung.todaysrecord.ui.myinterests

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
import com.yunjung.todaysrecord.databinding.FragmentMyinterestsBinding
import com.yunjung.todaysrecord.recyclerview.PhotoStudioAdapter

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

    // 뷰가 완전히 생성 되었을 때 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyinterestsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // 실시간으로 변하는 값 옵저버 (interestsNum)
        initObserver()

        // user 업데이트
        viewModel.updateUser((requireContext().applicationContext as MyApplication).user.value!!)

        // 로그인된 유저가 찜한 사진관의 리스트를 가져옴
        viewModel.updateInterestsList()

        // 리사이클러뷰 관련 설정
        initRecycler()
        subscribeStudioList()
    }

    // 실시간으로 변하는 값 옵저버
    private fun initObserver(){
        viewModel.interestsNum.observe(viewLifecycleOwner, Observer {
            binding.interestsNum.text = it.toString()
        })
    }

    // 리사이클러뷰에 어댑터를 부착
    private fun initRecycler(){
        binding.recyclerView.adapter = PhotoStudioAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    // 어댑터가 interestsList를 옵저버
    private fun subscribeStudioList() {
        viewModel.interestsList.observe(viewLifecycleOwner, {
            (binding.recyclerView.adapter as PhotoStudioAdapter).submitList(it)
        })
    }
}