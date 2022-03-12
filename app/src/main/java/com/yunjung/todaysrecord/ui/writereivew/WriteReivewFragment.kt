package com.yunjung.todaysrecord.ui.writereivew

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentWriteReviewBinding
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream

class WriteReivewFragment : Fragment() {
    // DataBinding & ViewModel 관련 변수
    private lateinit var binding : FragmentWriteReviewBinding
    private lateinit var viewModel : WriteReviewViewModel

    // Navigation component safe args 관련 변수
    private val args : WriteReivewFragmentArgs by navArgs()

    // 사진 업로드 관련
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>

    companion object{
        fun  newInstance() : WriteReivewFragment{
            return WriteReivewFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_review, container, false)

        initActivityResultLauncher() // 이미지를 얻어오는 화면의 결과를 처리하는 런처 초기화

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WriteReviewViewModel::class.java)

        // user 업데이트
        viewModel.updateUser((requireActivity().applicationContext as MyApplication).user.value!!)

        // psId 업데이트
        viewModel.updatePsId(args.psId)

        // finishBtn 클릭 이벤트 설정 (해당 사진관에 리뷰 등록 & 뒤로 감)
        initFinishBtn()

        // cancelBtn 클릭 이벤트 설정 (뒤로 감)
        initCancelBtn()

        // attachImageBtn 클릭 이벤트 설정 (디바이스에서 이미지를 얻어옴)
        initAttachImageBtn()
    }

    private fun initFinishBtn(){
        binding.finishBtn.setOnClickListener {
            val rating : Int = binding.ratingBar.numStars
            val content : String = binding.reviewContent.text.toString()

            // 리뷰 등록
            lifecycleScope.launch(Dispatchers.IO) {
                RetrofitManager.service.postReview(
                    viewModel.psId.value, viewModel.user.value!!._id, rating, content,
                    viewModel.reviewImage.value)
            }
            it.findNavController().navigateUp() // 뒤로 감
        }
    }

    private fun initCancelBtn(){
        binding.cancelBtn.setOnClickListener {
            it.findNavController().navigateUp() // 아무런 동작없이 뒤로 감
        }
    }

    private fun initAttachImageBtn(){
        binding.attachImageBtn.setOnClickListener {
            getPicture() // 디바이스의 갤러리에서 이미지를 얻어옴
        }
    }

    private fun initActivityResultLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri : Uri? ->
            // 선택한 이미지의 bitmap 생성
            val inputStream : InputStream = requireActivity().contentResolver.openInputStream(uri!!)!!
            var bitmap : Bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            // bitmap 이미지 사이즈 지정
            bitmap = Bitmap.createScaledBitmap(bitmap,150,150,true)

            // selectedImage에 선택된 이미지 디스플레이
            binding.selectedImage.setImageBitmap(bitmap)

            // 선택된 이미지(Bitmap)을 문자열로 변환
            viewModel.updateReviewImage(bitmapToString(bitmap))
        }
    }

    private fun getPicture(){
        activityResultLauncher.launch("image/*") // 이미지를 얻어옴
    }

    // bitmap 이미지를 문자열로 변환
    private fun bitmapToString(bitmap : Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}