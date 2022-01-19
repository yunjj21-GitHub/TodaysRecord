package com.yunjung.todaysrecord.photostudid

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
import com.yunjung.todaysrecord.databinding.FragmentPhotostudioIdBinding
import com.yunjung.todaysrecord.main.MainActivity
import com.yunjung.todaysrecord.recyclerview.PhotoStudioAdapter

class PhotostudioIdFragment : Fragment(){
    lateinit var binding : FragmentPhotostudioIdBinding
    lateinit var viewModel : PhotostudioIdViewModel

    companion object{
        fun newInstance() : PhotostudioIdFragment{
            return PhotostudioIdFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photostudio_id, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(PhotostudioIdViewModel::class.java)
        binding.viewModel = viewModel

        // userArea 업데이트
        viewModel.updateUserArea((requireActivity() as MainActivity).viewModel.userArea.value!!)

        // photoStudioList 업데이트
        viewModel.updatePhotoStudioList()

        // 리사이클러뷰에 어댑터를 부착
        initRecycler()

        // 리사이클러뷰 어댑터가 photoStudioList를 옵저버
        subscribeUserList()
    }

    // 리사이클러뷰에 어댑터를 부착
    private fun initRecycler() {
        binding.recyclerView.adapter = PhotoStudioAdapter()
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    // photoStudioList가 변경되면 실행
    private fun subscribeUserList(){
        viewModel.photoStudioList.observe(viewLifecycleOwner, {
            (binding.recyclerView.adapter as PhotoStudioAdapter).submitList(it)
        })
    }
}