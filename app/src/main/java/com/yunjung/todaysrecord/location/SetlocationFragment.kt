package com.yunjung.todaysrecord.location

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
import com.yunjung.todaysrecord.databinding.FragmentSetlocationBinding
import com.yunjung.todaysrecord.recyclerview.LocationAdapter

class SetlocationFragment : Fragment(){
    lateinit var binding : FragmentSetlocationBinding
    lateinit var viewModel: SetlocationViewModel

    companion object{
        fun newInstance() : SetlocationFragment {
            return SetlocationFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setlocation, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SetlocationViewModel::class.java)
        binding.viewModel = viewModel

        // 리사이클러뷰 적용
        initRecycler()
        subscribeLocationList()

        // '완료' 버튼 동작 설정
        binding.finishBtn.setOnClickListener {
            it.findNavController().navigateUp() // 뒤로가기 동작
        }
    }

    private fun initRecycler(){
        binding.recyclerLocation1.adapter = LocationAdapter()
        binding.recyclerLocation1.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun subscribeLocationList(){
        viewModel.locationList.observe(viewLifecycleOwner,{
            (binding.recyclerLocation1.adapter as LocationAdapter).submitList(it)
        })
    }
}