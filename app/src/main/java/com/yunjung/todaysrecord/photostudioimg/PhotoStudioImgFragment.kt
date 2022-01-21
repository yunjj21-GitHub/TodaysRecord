package com.yunjung.todaysrecord.photostudioimg

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentPhotostudioimgBinding

class PhotoStudioImgFragment(val photoStudioImg: String) : Fragment() {
    // DataBinding & ViewModle 관련 변수
    lateinit var binding : FragmentPhotostudioimgBinding
    lateinit var viewModel: PhotoStudioImgViewModel

    companion object {
        fun newInstance(photoStudioImg: String): PhotoStudioImgFragment {
            return PhotoStudioImgFragment(photoStudioImg)
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photostudioimg, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotoStudioImgViewModel::class.java)
        binding.viewModel = viewModel

        // 뷰모델 업데이트
        Log.e(TAG, photoStudioImg + " PhotoStudioImg")
        viewModel.updatePhotoSudioImg(photoStudioImg)

        // 뷰모델의 photoStudioImg 옵저버
        viewModel.photoStudioImg.observe(viewLifecycleOwner, Observer {
            //뷰 갱신
            Glide.with(binding.root.context)
                .load(viewModel.photoStudioImg.value)
                .fallback(R.drawable.sample_image)
                .into(binding.photoStudioImg)
        })
    }
}