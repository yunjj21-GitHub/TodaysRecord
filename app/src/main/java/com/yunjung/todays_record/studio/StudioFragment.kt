package com.yunjung.todays_record.studio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todays_record.R
import com.yunjung.todays_record.databinding.FragmentStudioBinding
import com.yunjung.todays_record.recyclerview.RecyclerAdapter

class StudioFragment : Fragment() {
    lateinit var binding : FragmentStudioBinding
    lateinit var viewModel : StudioViewModel

    // 자기 자신의 인스턴스를 반환하는 메소드 (companion object : 동반자 객체)
    companion object{
        fun newIstance() : StudioFragment{
            return StudioFragment()
        }
    }

    override fun onCreateView( // 뷰가 생성될 때 실행
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_studio, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(StudioViewModel::class.java)
        binding.viewModel = viewModel

        initRecycler()
        subscribeStudioList()
    }

    private fun initRecycler(){
        binding.recyclerViewStudio.adapter = RecyclerAdapter()
        binding.recyclerViewStudio.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun subscribeStudioList() {
        viewModel.photoStudioList.observe(viewLifecycleOwner, {
            (binding.recyclerViewStudio.adapter as RecyclerAdapter).submitList(it)
        })
    }
}