package com.yunjung.todaysrecord.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentSearchBinding
import com.yunjung.todaysrecord.recyclerview.PhotoStudioAdapter

class SearchFragment : Fragment() {
    lateinit var binding : FragmentSearchBinding
    lateinit var viewModel : SearchViewModel

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성되었을 때 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        binding.viewModel = viewModel

        // 검색 버튼 클릭 이벤트 설정
        initSearchBtn()

        // 리사이클러뷰에 어댑터 부착
        initRecycler()

        // 리사이클러뷰 어댑터가 searchResults를 옵저버
        subscribeSearchResults()
    }

    private fun initSearchBtn(){
        binding.searchBtn.setOnClickListener {
            if(binding.searchWordInput.text.isNotBlank()){
                val searchWord = binding.searchWordInput.text.toString() // 사용자가 입력한 검색어

                // 서버에서 검색어와 일치하는 사진관 리스트를 받아옴
                viewModel.updateSearchResults(searchWord)
            }
        }
    }

    // 리사이클러뷰에 어댑터 부착
    private fun initRecycler() {
        binding.searchResultRecycler.adapter = PhotoStudioAdapter()
        binding.searchResultRecycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    // 리사이클러뷰 어댑터가 searchResults를 옵저버
    private fun subscribeSearchResults() {
        viewModel.searchResults.observe(viewLifecycleOwner, {
            (binding.searchResultRecycler.adapter as PhotoStudioAdapter).submitList(it)
        })
    }
}