package com.yunjung.todaysrecord.ui.photostudiofamily

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
import com.yunjung.todaysrecord.databinding.FragmentPhotostudioFamilyBinding
import com.yunjung.todaysrecord.ui.main.MainActivity
import com.yunjung.todaysrecord.recyclerview.PhotoStudioAdapter

class PhotostudioFamilyFragment : Fragment(){
    lateinit var binding : FragmentPhotostudioFamilyBinding
    lateinit var viewModel : PhotostudioFamilyViewModel

    companion object{
        fun newInstance() : PhotostudioFamilyFragment {
            return PhotostudioFamilyFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photostudio_family, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(PhotostudioFamilyViewModel::class.java)
        binding.viewModel = viewModel

        // userArea 업데이트
        viewModel.updateUserArea((requireActivity() as MainActivity).viewModel.userArea.value!!)

        // photoStudioList 업데이트
        viewModel.updatePhotoStudioList()

        // 리사이클러뷰에 어댑터 부착
        initRecycler()

        // 리사이클러뷰 어댑터가 photoStudioList 옵저버
        subscribePhotoStudioList()
    }

    private fun initRecycler(){
        binding.recyclerView.adapter = PhotoStudioAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun subscribePhotoStudioList(){
        viewModel.photoStudioList.observe(viewLifecycleOwner, {
            (binding.recyclerView.adapter as PhotoStudioAdapter).submitList(it)
        })
    }
}