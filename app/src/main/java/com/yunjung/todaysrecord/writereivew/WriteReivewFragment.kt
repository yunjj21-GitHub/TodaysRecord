package com.yunjung.todaysrecord.writereivew

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentWriteReviewBinding
import com.yunjung.todaysrecord.detail.DetailFragmentArgs
import com.yunjung.todaysrecord.main.MainActivity
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.network.RetrofitManager
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class WriteReivewFragment : Fragment() {
    // DataBinding & ViewModel 관련 변수
    lateinit var binding : FragmentWriteReviewBinding
    lateinit var viewModel : WriteReviewViewModel

    // Navigaion component safe args 관련 변수
    val args : WriteReivewFragmentArgs by navArgs()
    private lateinit var psId: String

    // 사진 업로드 관련
    var reviewImage : String? = null

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WriteReviewViewModel::class.java)

        // 로그인된 userId
        val userId : String = (requireActivity().applicationContext as MyApplication).userId.value.toString()

        // Navigaion component safe args 관련
        psId = args.psId!!

        // '등록' 버튼 클릭 이벤트 설정 (해당 사진관에 리뷰 등록 & 뒤로 감)
        binding.finishBtn.setOnClickListener {
            val rating : Int = binding.ratingBar.numStars
            val content : String = binding.reviewContent.text.toString()
            // userId, psId, reviewImage

            // 서버에 전달
            val call : Call<Review>? = RetrofitManager.iRetrofit?.postReview(psId, userId, rating, content, reviewImage)
            call?.enqueue(object : retrofit2.Callback<Review>{
                // 응답 성공시
                override fun onResponse(call: Call<Review>, response: Response<Review>) {
                    Log.e(TAG, "응답 성공")
                }

                // 응답 실패시
                override fun onFailure(call: Call<Review>, t: Throwable) {
                    Log.e(TAG, "응답 실패")
                }
            })

            it.findNavController().navigateUp() // 뒤로 감
        }

        // '취소' 버튼 클릭 이벤트 설정 (뒤로 감)
        binding.cancelBtn.setOnClickListener {
            it.findNavController().navigateUp() // 아무런 동작없이 뒤로 감
        }

        // '사진 첨부하기' 버튼 클릭 이벤트 설정
        binding.attachImageBtn.setOnClickListener {
            getPicture() // 디바이스의 갤러리에서 이미지를 얻어옴
        }
    }

    fun getPicture(){ // 디바이스에서 갤러리를 접근하여 이미지를 얻어옴
        val intent = Intent(Intent.ACTION_PICK)
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setType("image/*")
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1000){
            val inputStream : InputStream = requireActivity().contentResolver.openInputStream(data!!.data!!)!!
            var bitmap : Bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            // bitamp 이미지 resize
            bitmap = Bitmap.createScaledBitmap(bitmap,150,150,true);

            // selectedImage에 선택된 이미지 디스플레이
            binding.selectedImage.setImageBitmap(bitmap)

            // 선택된 이미지(Bitmap)을 String으로 변환
            reviewImage = bitmapToString(bitmap)
            Log.e("bitmap", reviewImage.toString())
        }
    }

    // bitmap 이미지를 (String)으로 변경
    private fun bitmapToString(bitmap : Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}