package com.neisha.trashhub.view.TrashDetection

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.neisha.trashhub.R
import com.neisha.trashhub.data.UserRepository
import com.neisha.trashhub.data.pref.UserPreference
import com.neisha.trashhub.data.pref.dataStore
import com.neisha.trashhub.data.pref.response.ErrorUploadResponse
import com.neisha.trashhub.data.pref.response.PredictionResponse
import com.neisha.trashhub.data.retrofit.ApiService
import com.neisha.trashhub.data.retrofit.ApiConfig
import com.neisha.trashhub.databinding.ActivityPredictionBinding
import com.neisha.trashhub.view.main.LoginActivity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PredictionActivity : AppCompatActivity() {

    private lateinit var userPreference: UserPreference
    private lateinit var userRepository: UserRepository
    private lateinit var apiService: ApiService
    private lateinit var binding: ActivityPredictionBinding
    private var currentImageUri: Uri? = null
    private lateinit var photoPart: MultipartBody.Part

    private val requestCameraPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show()
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
            }
        }

    private val requestStoragePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_LONG).show()
                startGallery()
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_LONG).show()
            }
        }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(this@PredictionActivity.dataStore)
        userRepository = UserRepository.getInstance(userPreference, ApiConfig.getApiServiceWithoutToken())
        apiService = ApiConfig.getApiServiceWithoutToken()

        binding.galleryButton.setOnClickListener { checkGalleryPermission() }
        binding.cameraButton.setOnClickListener { checkCameraPermission() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun checkGalleryPermission() {
        val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            startGallery()
        } else {
            requestStoragePermissionLauncher.launch(permission)
        }
    }

    private val CAMERA_PERMISSION_REQUEST_CODE = 1001

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera()
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch("image/*")
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            Log.d("Photo Picker", "Selected Uri: $uri")
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
            showToast("No media selected")
        }
    }

    private fun startCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.resolveActivity(packageManager)?.also {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        } ?: run {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            imageBitmap?.let {
                binding.previewImageView.setImageBitmap(it)
                currentImageUri = getImageUriFromBitmap(it)
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "JPEG_${timeStamp}",
            null
        )
        return Uri.parse(path)
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = getFileFromUri(uri)
            imageFile?.let {
                val reducedFile = it // Anda dapat mereduksi ukuran gambar jika diperlukan di sini
                photoPart = prepareImageFileForUpload(reducedFile)

                // Simulating the result
                val result = """
                 recyclePercentage: 100%
                    "
                """.trimIndent()

                binding.resultTextView.text = result
                showToast("Prediction simulated. Check result below.")
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun getFileFromUri(uri: Uri): File? {
        val contentResolver = contentResolver ?: return null

        val inputStream = contentResolver.openInputStream(uri) ?: return null

        val tempFile = File(cacheDir, "temp_image.jpg")
        tempFile.deleteOnExit()
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        return tempFile
    }

    private fun prepareImageFileForUpload(file: File): MultipartBody.Part {
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
