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

class WriteReivewFragment : Fragment() {
    // DataBinding & ViewModel 관련 변수
    lateinit var binding : FragmentWriteReviewBinding
    lateinit var viewModel : WriteReviewViewModel

    // Navigaion component safe args 관련 변수
    val args : WriteReivewFragmentArgs by navArgs()
    private lateinit var psId: String

    // 사진 업로드 관련
    lateinit var filePath: String

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
            uploadReview(userId)

            it.findNavController().navigateUp()
        }

        // '취소' 버튼 클릭 이벤트 설정 (뒤로 감)
        binding.cancelBtn.setOnClickListener {
            it.findNavController().navigateUp()
        }

        // '사진 첨부하기' 버튼 클릭 이벤트 설정
        binding.attachImageBtn.setOnClickListener {
            getPicture()
        }
    }

    fun getPicture(){
        // 디바이스에서 갤러리에 접근
        val intent = Intent(Intent.ACTION_PICK)
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setType("image/*")
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1000){
            val uri : Uri = data!!.data!! // data!!.data!! : 사진파일의 상대주소
            binding.selectedImage.setImageURI(uri) // 이미지뷰에 디스플레이
            filePath = getImageFilePath(uri)
        }
    }

    fun getImageFilePath(contentUri : Uri) : String { // 사진 파일의 절대경로를 찾아줌
        var columnIndex = 0
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val curosor = requireActivity().contentResolver.query(contentUri, projection, null, null, null)
        if(curosor!!.moveToFirst()){
            columnIndex = curosor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        return curosor.getString(columnIndex)
    }

    // (단, 무조건 이미지가 선택되어 있어야 한다.)
    fun uploadReview(userId : String){
        val file = File(filePath)
        val fileRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val part = MultipartBody.Part.createFormData("image", file.name, fileRequestBody)

        val psId = RequestBody.create("text/plain".toMediaTypeOrNull(), psId)
        val userId = RequestBody.create("text/plain".toMediaTypeOrNull(), userId)
        val rating = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.ratingBar.numStars.toString()
        ) // Int형 이여야 함
        val content = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.reviewContent.text.toString()
        )

        // 서버에 전달
        val call : Call<Review>? = RetrofitManager.iRetrofit?.uploadReview(
            psId = psId,
            userId = userId,
            rating = rating,
            content = content,
            image = part
        )
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

        // 서버가 이미지를 저장할 수 있도록 셋팅해야 함
    }

    // 이미지를 bitmap (String)으로 변경
    private fun bitmapToString(uri: Uri): String {
        // ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().contentResolver(uri))

        val bitmap = BitmapFactory.decodeFile(filePath)
        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}