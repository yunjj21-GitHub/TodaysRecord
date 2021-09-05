package com.yunjung.todaysrecord.information

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentInformationBinding
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavArgs
import com.yunjung.todaysrecord.detail.DetailFragment
import com.yunjung.todaysrecord.detail.DetailFragmentArgs
import com.yunjung.todaysrecord.models.PhotoStudio

class InformationFragment : Fragment(){
    lateinit var binding : FragmentInformationBinding
    lateinit var viewModel: InformationViewModel

    companion object{
        // Fragment가 생성될 때 DetailFragmentArgs 객체를 전달 받음
        lateinit var photoStudio: PhotoStudio
        fun newInstance(args: DetailFragmentArgs) : InformationFragment {
            photoStudio = args.photoStudio!!
            return InformationFragment()
        }
    }

    // 프래그먼트가 생성될 때 실행
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_information, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(InformationViewModel::class.java)
        binding.viewModel = viewModel

        // photoStudio 객체 사용가능
    }
}