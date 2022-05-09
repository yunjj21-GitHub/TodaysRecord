package com.yunjung.todaysrecord.ui.writereivew

import android.content.ContentValues.TAG
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
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
import com.yunjung.todaysrecord.network.api.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.*
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.BufferedSink
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import retrofit2.http.Multipart




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

            // 업로드할 이미지명
            val reviewImageName = viewModel.user.value!!._id + System.currentTimeMillis().toString() + ".jpg"
            // bitmap으로 MultipartBody.Part 생성
            val bitmapRequestBody = viewModel.reviewImageBitmap.value.let { BitmapRequestBody(it!!) }
            val bitmapMultipartBody = MultipartBody.Part.createFormData("reviewImage", reviewImageName, bitmapRequestBody)

            lifecycleScope.launch {
                withContext(IO){
                    // 서버에 해당 이미지 업로드
                    RetrofitManager.service.reviewImageUpload(bitmapMultipartBody)

                    // 리뷰 등록
                    RetrofitManager.service.postReview(
                        psId = viewModel.psId.value,
                        userId = viewModel.user.value!!._id,
                        rating = rating,
                        content = content,
                        image = "http://192.168.0.11/$reviewImageName")
                }
                // 뒤로가기
                it.findNavController().navigateUp()
            }
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
            // 갤러리에서 선택한 이미지로 bitmap 생성
            val inputStream : InputStream = requireActivity().contentResolver.openInputStream(uri!!)!!
            var bitmap : Bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap = Bitmap.createScaledBitmap(bitmap,500,500,true) // bitmap 리사이즈
            
            // 생성한 bitmap을 이미지뷰에 띄움
            binding.selectedImage.setImageBitmap(bitmap)

            // viewModel의 reviewImageBitmap 갱신
            viewModel.updateReviewImageBitmap(bitmap)
        }
    }

    private fun getPicture(){
        activityResultLauncher.launch("image/*") // 이미지를 얻어옴
    }

    class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): MediaType? = "image/jpeg".toMediaType()

        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, sink.outputStream())
        }
    }
}