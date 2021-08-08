package com.yunjung.todays_record.studio

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yunjung.todays_record.R
import com.yunjung.todays_record.Studio
import com.yunjung.todays_record.databinding.FragmentStudioBinding

class StudioFragment : Fragment() {
    lateinit var binding : FragmentStudioBinding
    var studioList = ArrayList<Studio>()

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

        var studio1 = Studio("유명사진관", 15000, "권선구 탑동 00-00, 000호")
        this.studioList.add(studio1)

        var studio2 = Studio("인기사진관", 8000, "영통구 매탄동 00-0")
        this.studioList.add(studio2)

        var studio3 = Studio("무지개사진관", 12000, "팔달구 인계동 00-00, (시청역 부근)")
        this.studioList.add(studio3)

        var studio4 = Studio("태양사진관", 10000, "팔달구 화서동 00-00, 0층")
        this.studioList.add(studio4)

        var studio5 = Studio("사랑사진관", 5000, "장안구 정자동 00-00, 00빌딩 000호")
        this.studioList.add(studio5)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(StudioViewModel::class.java)
        binding.viewModel = viewModel
    }
}